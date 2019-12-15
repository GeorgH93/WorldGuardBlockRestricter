package at.pcgamingfreaks.WorldGuardBlockRestricter.Bukkit;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public class WGBlockRestricterChecker
{
	public static final EnumMap<Material, Material> baseMaterials = new EnumMap<>(Material.class);

	private final WorldGuard wg;
	private final RegionContainer container;

	public WGBlockRestricterChecker(WorldGuard worldGuard)
	{
		this.wg = worldGuard;
		container = wg.getPlatform().getRegionContainer();
	}

	public boolean placeAllowedAtLocation(Material mat, Location loc)
	{
		return actionAllowedAtLocation(mat, loc, WorldGuardBlockRestricter.ALLOW_PLACE_FLAG, WorldGuardBlockRestricter.DENY_PLACE_FLAG);
	}

	public boolean breakAllowedAtLocation(Material mat, Location loc)
	{
		return actionAllowedAtLocation(mat, loc, WorldGuardBlockRestricter.ALLOW_BREAK_FLAG, WorldGuardBlockRestricter.DENY_BREAK_FLAG);
	}

	public boolean actionAllowedAtLocation(Material mat, Location loc, SetFlag<Material> allowedMats, SetFlag<Material> blockedMats)
	{
		RegionManager rm;
		Material blockType = mat;
		if(baseMaterials.containsKey(blockType))
		{
			blockType = baseMaterials.get(blockType);
		}
		if((rm = container.get(BukkitAdapter.adapt(loc.getWorld()))) == null)
		{
			return true;
		}
		ApplicableRegionSet regions = rm.getApplicableRegions(BukkitAdapter.asBlockVector(loc));
		Iterator<ProtectedRegion> itr = regions.iterator();
		HashMap<ProtectedRegion, Boolean> regionsToCheck = new HashMap<>();
		HashSet<ProtectedRegion> ignoredRegions = new HashSet<>();
		while(itr.hasNext())
		{
			Object allowed;
			ProtectedRegion region = itr.next();
			if(ignoredRegions.contains(region) || (allowed = actionAllowedInRegion(region, blockType, allowedMats, blockedMats)) == null) continue;
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
			for(Map.Entry<ProtectedRegion, Boolean> entry : regionsToCheck.entrySet())
			{
				ProtectedRegion region = entry.getKey();
				boolean value = entry.getValue();
				if(ignoredRegions.contains(region) || !value) continue;
				return true;
			}
			return false;
		}
		Object allowed = actionAllowedInRegion(rm.getRegion("__global__"), blockType, allowedMats, blockedMats);
		if(allowed != null)
		{
			return (Boolean) allowed;
		}
		return true;
	}

	public static Object blockAllowedInRegion(ProtectedRegion region, Material blockType)
	{
		if(region == null) return null;
		Set<Material> allowedBlocks = region.getFlag(WorldGuardBlockRestricter.ALLOW_BLOCK_FLAG);
		Set<Material> blockedBlocks = region.getFlag(WorldGuardBlockRestricter.DENY_BLOCK_FLAG);
		if(allowedBlocks != null && (allowedBlocks.contains(blockType) || allowedBlocks.contains(Material.AIR))) return true;
		if(blockedBlocks != null && (blockedBlocks.contains(blockType) || blockedBlocks.contains(Material.AIR))) return false;
		return null;
	}

	public static Object placeAllowedInRegion(ProtectedRegion region, Material blockType)
	{
		return actionAllowedInRegion(region, blockType, WorldGuardBlockRestricter.ALLOW_PLACE_FLAG, WorldGuardBlockRestricter.DENY_PLACE_FLAG);
	}

	public static Object breakAllowedInRegion(ProtectedRegion region, Material blockType)
	{
		return actionAllowedInRegion(region, blockType, WorldGuardBlockRestricter.ALLOW_BREAK_FLAG, WorldGuardBlockRestricter.DENY_BREAK_FLAG);
	}

	public static Object actionAllowedInRegion(ProtectedRegion region, Material blockType, SetFlag<Material> allowedMats, SetFlag<Material> blockedMats)
	{
		if(region == null) return null;
		if(blockAllowedInRegion(region, blockType) == Boolean.FALSE) return false;
		Set<Material> allowedBlocks = region.getFlag(allowedMats);
		Set<Material> blockedBlocks = region.getFlag(blockedMats);
		if(allowedBlocks != null && (allowedBlocks.contains(blockType) || allowedBlocks.contains(Material.AIR))) return true;
		if(blockedBlocks != null && (blockedBlocks.contains(blockType) || blockedBlocks.contains(Material.AIR))) return false;
		return null;
	}

	static
	{
		/*baseMaterials.put(Material.DIODE_BLOCK_OFF, Material.DIODE);
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
		aliases.put("diode", Material.DIODE);*/
	}
}