package at.pcgamingfreaks.WorldGuardBlockRestricter.Bukkit.Flags;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;

import org.bukkit.Material;

public class BlockMaterialFlag extends Flag<Material>
{
	public BlockMaterialFlag(String name)
	{
		super(name);
	}

	@Override
	public Material parseInput(FlagContext flagContext) throws InvalidFlagFormat
	{
		//TODO
		String input = flagContext.getUserInput();
		Material mat = Material.getMaterial(input);
		//TODO make it more user friendly to input a material
		return mat;
	}

	@Override
	public Material unmarshal(Object o)
	{
		return this.loadFromDb((String) o);
	}

	@Override
	public Object marshal(Material t)
	{
		return this.saveToDb(t);
	}

	public Material loadFromDb(String str)
	{
		Material mat = Material.getMaterial(str);
		if(mat == null) mat = Material.getMaterial(str, true);
		return mat;
	}

	public String saveToDb(Material o)
	{
		return o.name();
	}
}

