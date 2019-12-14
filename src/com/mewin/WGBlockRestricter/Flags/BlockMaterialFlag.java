package com.mewin.WGBlockRestricter.Flags;

import com.mewin.WGBlockRestricter.Utils;
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
		Material mat;
		block5:
		{
			if(Utils.aliases.containsKey(input.trim().toLowerCase().replace(" ", "_")))
			{
				return Utils.aliases.get(input);
			}
			try
			{
				int i = Integer.valueOf(input);
				mat = Material.getMaterial(i);
				if(mat == null)
				{
					throw new InvalidFlagFormat(String.valueOf(input) + " is not a valid material id.");
				}
			}
			catch(NumberFormatException ex)
			{
				mat = Material.getMaterial(input.toUpperCase());
				if(mat != null) break block5;
				throw new InvalidFlagFormat(String.valueOf(input) + " is not a valid material name.");
			}
		}
		if(!mat.isBlock())
		{
			throw new InvalidFlagFormat(String.valueOf(mat.name()) + " is not a block.");
		}
		return mat;
	}

	public Material unmarshal(Object o)
	{
		return this.loadFromDb((String) o);
	}

	public Object marshal(Material t)
	{
		return this.saveToDb(t);
	}

	public Material loadFromDb(String str)
	{
		return Material.getMaterial(str);
	}

	public String saveToDb(Material o)
	{
		return o.name();
	}
}

