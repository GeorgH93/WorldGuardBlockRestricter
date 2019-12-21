package at.pcgamingfreaks.WorldGuardBlockRestricter.Bukkit.Flags;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.item.ItemType;
import com.sk89q.worldedit.world.registry.LegacyMapper;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;

import org.bukkit.Material;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockMaterialFlag extends Flag<Material>
{
	private static final Pattern OLD_ITEM_PATTERN = Pattern.compile("^(?<id>\\d+)(:(?<data>\\d+))?$");
	public BlockMaterialFlag(String name)
	{
		super(name);
	}

	@Override
	public Material parseInput(FlagContext flagContext) throws InvalidFlagFormat
	{
		String input = flagContext.getUserInput();
		Material mat = null;
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

		if(mat == null) throw new InvalidFlagFormat("The material \"" + input + "\" is unknown.");
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
}

