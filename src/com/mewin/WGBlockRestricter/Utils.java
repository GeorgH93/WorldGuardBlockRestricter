package com.mewin.WGBlockRestricter;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;

public final class Utils
{
	public static final EnumMap<Material, Material> baseMaterials = new EnumMap<>(Material.class);
	public static final HashMap<String, Material> aliases = new HashMap<>();

	@Deprecated
	public static boolean blockAllowedAtLocation(WorldGuardPlugin wgp, Material mat, Location loc)
	{
		RegionManager rm;
		Material blockType = mat;
		if(baseMaterials.containsKey(blockType))
		{
			blockType = baseMaterials.get(blockType);
		}
		if((rm = wgp.getRegionManager(loc.getWorld())) == null)
		{
			return true;
		}
		ApplicableRegionSet regions = rm.getApplicableRegions(loc);
		Iterator itr = regions.iterator();
		HashMap<ProtectedRegion, Boolean> regionsToCheck = new HashMap<>();
		HashSet<ProtectedRegion> ignoredRegions = new HashSet<>();
		while(itr.hasNext())
		{
			Object allowed;
			ProtectedRegion region = (ProtectedRegion) itr.next();
			if(ignoredRegions.contains(region) || (allowed = Utils.blockAllowedInRegion(region, blockType)) == null) continue;
			ProtectedRegion parent = region.getParent();
			while(parent != null)
			{
				ignoredRegions.add(parent);
				parent = parent.getParent();
			}
			regionsToCheck.put(region, (Boolean) allowed);
		}
		if(regionsToCheck.size() >= 1)
		{
			for(Map.Entry entry : regionsToCheck.entrySet())
			{
				ProtectedRegion region = (ProtectedRegion) entry.getKey();
				boolean value = (Boolean) entry.getValue();
				if(ignoredRegions.contains(region) || !value) continue;
				return true;
			}
			return false;
		}
		Object allowed = Utils.blockAllowedInRegion(rm.getRegion("__global__"), blockType);
		if(allowed != null)
		{
			return (Boolean) allowed;
		}
		return true;
	}

	public static Object blockAllowedInRegion(ProtectedRegion region, Material blockType)
	{
		if(region == null)
		{
			return null;
		}
		Set allowedBlocks = region.getFlag(WGBlockRestricterPlugin.ALLOW_BLOCK_FLAG);
		Set blockedBlocks = region.getFlag(WGBlockRestricterPlugin.DENY_BLOCK_FLAG);
		boolean denied = false;
		if(allowedBlocks != null && (allowedBlocks.contains(blockType) || allowedBlocks.contains(Material.AIR)))
		{
			return true;
		}
		if(blockedBlocks != null && (blockedBlocks.contains(blockType) || blockedBlocks.contains(Material.AIR)))
		{
			denied = true;
		}
		if(!denied)
		{
			return null;
		}
		return false;
	}

	public static boolean placeAllowedAtLocation(WorldGuardPlugin wgp, Material mat, Location loc)
	{
		RegionManager rm;
		Material blockType = mat;
		if(baseMaterials.containsKey(blockType))
		{
			blockType = baseMaterials.get(blockType);
		}
		if((rm = wgp.getRegionManager(loc.getWorld())) == null)
		{
			return true;
		}
		ApplicableRegionSet regions = rm.getApplicableRegions(loc);
		Iterator itr = regions.iterator();
		HashMap<ProtectedRegion, Boolean> regionsToCheck = new HashMap<>();
		HashSet<ProtectedRegion> ignoredRegions = new HashSet<>();
		while(itr.hasNext())
		{
			Object allowed;
			ProtectedRegion region = (ProtectedRegion) itr.next();
			if(ignoredRegions.contains(region) || (allowed = Utils.placeAllowedInRegion(region, blockType)) == null) continue;
			ProtectedRegion parent = region.getParent();
			while(parent != null)
			{
				ignoredRegions.add(parent);
				parent = parent.getParent();
			}
			regionsToCheck.put(region, (Boolean) allowed);
		}
		if(regionsToCheck.size() >= 1)
		{
			for(Map.Entry entry : regionsToCheck.entrySet())
			{
				ProtectedRegion region = (ProtectedRegion) entry.getKey();
				boolean value = (Boolean) entry.getValue();
				if(ignoredRegions.contains(region) || !value) continue;
				return true;
			}
			return false;
		}
		Object allowed = Utils.placeAllowedInRegion(rm.getRegion("__global__"), blockType);
		if(allowed != null)
		{
			return (Boolean) allowed;
		}
		return true;
	}

	public static Object placeAllowedInRegion(ProtectedRegion region, Material blockType)
	{
		if(region == null)
		{
			return null;
		}
		if(Utils.blockAllowedInRegion(region, blockType) == Boolean.FALSE)
		{
			return false;
		}
		HashSet allowedBlocks = (HashSet) region.getFlag((Flag) WGBlockRestricterPlugin.ALLOW_PLACE_FLAG);
		HashSet blockedBlocks = (HashSet) region.getFlag((Flag) WGBlockRestricterPlugin.DENY_PLACE_FLAG);
		boolean denied = false;
		if(allowedBlocks != null && (allowedBlocks.contains(blockType) || allowedBlocks.contains(Material.AIR)))
		{
			return true;
		}
		if(blockedBlocks != null && (blockedBlocks.contains(blockType) || blockedBlocks.contains(Material.AIR)))
		{
			denied = true;
		}
		if(!denied)
		{
			return null;
		}
		return false;
	}

	public static boolean breakAllowedAtLocation(WorldGuardPlugin wgp, Material mat, Location loc)
	{
		RegionManager rm;
		Material blockType = mat;
		if(baseMaterials.containsKey(blockType))
		{
			blockType = baseMaterials.get(blockType);
		}
		if((rm = wgp.getRegionManager(loc.getWorld())) == null)
		{
			return true;
		}
		ApplicableRegionSet regions = rm.getApplicableRegions(loc);
		Iterator itr = regions.iterator();
		HashMap<ProtectedRegion, Boolean> regionsToCheck = new HashMap<>();
		HashSet<ProtectedRegion> ignoredRegions = new HashSet<>();
		while(itr.hasNext())
		{
			Object allowed;
			ProtectedRegion region = (ProtectedRegion) itr.next();
			if(ignoredRegions.contains(region) || (allowed = Utils.breakAllowedInRegion(region, blockType)) == null) continue;
			ProtectedRegion parent = region.getParent();
			while(parent != null)
			{
				ignoredRegions.add(parent);
				parent = parent.getParent();
			}
			regionsToCheck.put(region, (Boolean) allowed);
		}
		if(regionsToCheck.size() >= 1)
		{
			for(Map.Entry entry : regionsToCheck.entrySet())
			{
				ProtectedRegion region = (ProtectedRegion) entry.getKey();
				boolean value = (Boolean) entry.getValue();
				if(ignoredRegions.contains(region) || !value) continue;
				return true;
			}
			return false;
		}
		Object allowed = Utils.breakAllowedInRegion(rm.getRegion("__global__"), blockType);
		if(allowed != null)
		{
			return (Boolean) allowed;
		}
		return true;
	}

	public static Object breakAllowedInRegion(ProtectedRegion region, Material blockType)
	{
		if(region == null)
		{
			return null;
		}
		if(Utils.blockAllowedInRegion(region, blockType) == Boolean.FALSE)
		{
			return false;
		}
		HashSet allowedBlocks = (HashSet) region.getFlag((Flag) WGBlockRestricterPlugin.ALLOW_BREAK_FLAG);
		HashSet blockedBlocks = (HashSet) region.getFlag((Flag) WGBlockRestricterPlugin.DENY_BREAK_FLAG);
		boolean denied = false;
		if(allowedBlocks != null && (allowedBlocks.contains(blockType) || allowedBlocks.contains(Material.AIR)))
		{
			return true;
		}
		if(blockedBlocks != null && (blockedBlocks.contains(blockType) || blockedBlocks.contains(Material.AIR)))
		{
			denied = true;
		}
		if(!denied)
		{
			return null;
		}
		return false;
	}

	public static void init()
	{
		baseMaterials.put(Material.DIODE_BLOCK_OFF, Material.DIODE);
		baseMaterials.put(Material.DIODE_BLOCK_ON, Material.DIODE);
		baseMaterials.put(Material.STATIONARY_LAVA, Material.LAVA);
		baseMaterials.put(Material.LAVA_BUCKET, Material.LAVA);
		baseMaterials.put(Material.LEAVES_2, Material.LEAVES);
		baseMaterials.put(Material.LOG_2, Material.LOG);
		baseMaterials.put(Material.PISTON_EXTENSION, Material.PISTON_BASE);
		baseMaterials.put(Material.PISTON_MOVING_PIECE, Material.PISTON_BASE);
		baseMaterials.put(Material.PISTON_STICKY_BASE, Material.PISTON_BASE);
		baseMaterials.put(Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR);
		baseMaterials.put(Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_COMPARATOR);
		baseMaterials.put(Material.REDSTONE_LAMP_OFF, Material.REDSTONE_LAMP_ON);
		baseMaterials.put(Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON);
		baseMaterials.put(Material.WALL_SIGN, Material.SIGN);
		baseMaterials.put(Material.SIGN_POST, Material.SIGN);
		baseMaterials.put(Material.SUGAR_CANE_BLOCK, Material.SUGAR_CANE);
		baseMaterials.put(Material.STATIONARY_LAVA, Material.WATER);
		baseMaterials.put(Material.STRING, Material.TRIPWIRE);
		baseMaterials.put(Material.WATER_BUCKET, Material.WATER);
		aliases.put("piston", Material.PISTON_BASE);
		aliases.put("redstone_lamp", Material.REDSTONE_LAMP_ON);
		aliases.put("stone_brick", Material.SMOOTH_BRICK);
		aliases.put("painting", Material.PAINTING);
		aliases.put("item_frame", Material.ITEM_FRAME);
		aliases.put("any", Material.AIR);
		aliases.put("sign", Material.SIGN);
		aliases.put("diode", Material.DIODE);
		File itemCsv = new File("item.csv");
		if(itemCsv.exists() && itemCsv.isFile())
		{
			Bukkit.getLogger().log(Level.INFO, "item.csv found. Attempting to load mod materials.");
			FileInputStream in = null;
			try
			{
				try
				{
					String line;
					in = new FileInputStream(itemCsv);
					while((line = Utils.readLine(in)) != null)
					{
						String[] split = line.split(",");
						if(split.length < 5) continue;
						try
						{
							int id = Integer.parseInt(split[0]);
							String type = split[1];
							String mod = split[2];
							String name = split[3];
							if(!type.equalsIgnoreCase("block") || mod.equalsIgnoreCase("Minecraft") || mod.equalsIgnoreCase("null")) continue;
							aliases.put(String.valueOf(mod) + "." + name, Material.getMaterial(id));
							String shortName = name.substring(name.indexOf(".") + 1);
							if(!aliases.containsKey(shortName))
							{
								aliases.put(shortName, Material.getMaterial(id));
							}
							Bukkit.getLogger().log(Level.INFO, "Added material {0} of mod {1}.", new Object[] { name, mod });
						}
						catch(NumberFormatException ignored) {}
					}
					return;
				}
				catch(IOException ex)
				{
					Bukkit.getLogger().log(Level.WARNING, "Failed to load item.csv", ex);
					try
					{
						if(in == null) return;
						in.close();
						return;
					}
					catch(IOException ignored) {}
				}
				return;
			}
			finally
			{
				try
				{
					if(in != null)
					{
						in.close();
					}
				}
				catch(IOException ignored) {}
			}
		}
		Bukkit.getLogger().log(Level.INFO, "No item.csv found.");
	}

	private static String readLine(InputStream in) throws IOException
	{
		int i;
		LinkedList<Byte> bQ = new LinkedList<>();
		while((i = in.read()) != -1)
		{
			switch(i)
			{
				case 10:
				{
					return Utils.bQToString(bQ);
				}
				case 13:
				{
					break;
				}
				default:
				{
					bQ.add((byte) i);
				}
			}
		}
		if(bQ.size() < 1)
		{
			return null;
		}
		return Utils.bQToString(bQ);
	}

	private static String bQToString(Queue<Byte> bQ)
	{
		byte[] bytes = new byte[bQ.size()];
		int i = 0;
		while(i < bytes.length)
		{
			bytes[i] = bQ.poll();
			++i;
		}
		return new String(bytes);
	}
}