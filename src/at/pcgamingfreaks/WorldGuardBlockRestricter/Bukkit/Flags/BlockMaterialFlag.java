package at.pcgamingfreaks.WorldGuardBlockRestricter.Bukkit.Flags;

import at.pcgamingfreaks.Bukkit.Message.Message;
import at.pcgamingfreaks.WorldGuardBlockRestricter.Bukkit.WorldGuardBlockRestricter;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.item.ItemType;
import com.sk89q.worldedit.world.registry.LegacyMapper;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;

import org.bukkit.Material;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockMaterialFlag extends Flag<Material>
{
	private static final Pattern OLD_ITEM_PATTERN = Pattern.compile("^(?<id>\\d+)(:(?<data>\\d+))?$");
	private static final Map<String, Material> ALIASES = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	public BlockMaterialFlag(String name)
	{
		super(name);
	}

	@Override
	public Material parseInput(FlagContext flagContext) throws InvalidFlagFormat
	{
		String input = flagContext.getUserInput();
		Material mat = ALIASES.get(input);
		if(mat == null)
		{
			Matcher matcher = OLD_ITEM_PATTERN.matcher(input);
			if(matcher.matches())
			{
				int id = Integer.parseUnsignedInt(matcher.group("id")), data = 0;
				String dataString = matcher.group("data");
				if(dataString != null && !dataString.isEmpty()) data = Integer.parseUnsignedInt(dataString);
				ItemType itemType = LegacyMapper.getInstance().getItemFromLegacy(id, data);
				if(itemType != null) mat = BukkitAdapter.adapt(itemType);
			}
			else
			{
				mat = Material.matchMaterial(input);
				if(mat == null) mat = Material.matchMaterial(input, true);
			}
		}

		if(mat == null) throw new InvalidFlagFormat(String.format(WorldGuardBlockRestricter.getInstance().messageUnknownMaterial, input));
		return mat;
	}

	@Override
	public Material unmarshal(Object o)
	{
		String str = (String) o;
		Material mat = Material.getMaterial(str);
		if(mat == null) mat = Material.getMaterial(str, true);
		return mat;
	}

	@Override
	public Object marshal(Material t)
	{
		return t.name();
	}

	static
	{
		ALIASES.put("piston_head", Material.PISTON);
		ALIASES.put("redstone_lamp", Material.REDSTONE_LAMP);
		ALIASES.put("stone_brick", Material.STONE_BRICKS);
		ALIASES.put("painting", Material.PAINTING);
		ALIASES.put("item_frame", Material.ITEM_FRAME);
		ALIASES.put("any", Material.AIR);
	}
}