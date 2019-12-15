package at.pcgamingfreaks.WorldGuardBlockRestricter.Bukkit;

import at.pcgamingfreaks.Bukkit.ItemNameResolver;
import at.pcgamingfreaks.Bukkit.Message.Message;

import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@SuppressWarnings("unused")
public class BlockListener implements Listener
{
	private static final String BYPASS_PERMISSION = "wgblockrestricter.ignore";
	
	private final WorldGuardBlockRestricter plugin;
	private final WGBlockRestricterChecker checker;
	private final ItemNameResolver itemNameResolver;

	private Message messageDenyBlockPlace, messageDenyBlockBreak, messageDenyHangingPlace, messageDenyHangingBreak;

	public BlockListener(WorldGuardBlockRestricter plugin, WGBlockRestricterChecker checker)
	{
		this.plugin = plugin;
		this.checker = checker;

		itemNameResolver = at.pcgamingfreaks.PluginLib.Bukkit.ItemNameResolver.getInstance();

		// Load messages
		messageDenyBlockPlace = plugin.getLanguage().getMessage("DenyBlockPlace").replaceAll("\\{Block}", "%s");
		messageDenyBlockBreak = plugin.getLanguage().getMessage("DenyBlockBreak").replaceAll("\\{Block}", "%s");
		messageDenyHangingPlace = plugin.getLanguage().getMessage("DenyHangingPlace").replaceAll("\\{Block}", "%s");
		messageDenyHangingBreak = plugin.getLanguage().getMessage("DenyHangingBreak").replaceAll("\\{Block}", "%s");
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.hasItem() && checker.baseMaterials.containsKey(e.getItem().getType()) && !e.getPlayer().hasPermission(BYPASS_PERMISSION) && !checker.placeAllowedAtLocation(e.getMaterial(), e.getClickedBlock().getRelative(e.getBlockFace()).getLocation()))
		{
			messageDenyBlockPlace.send(e.getPlayer(), itemNameResolver.getName(e.getItem()));
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if(!e.getPlayer().hasPermission(BYPASS_PERMISSION) && !checker.placeAllowedAtLocation(e.getBlockPlaced().getType(), e.getBlockPlaced().getLocation()))
		{
			messageDenyBlockPlace.send(e.getPlayer(), itemNameResolver.getName(e.getBlock()));
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		if(!e.getPlayer().hasPermission(BYPASS_PERMISSION) && !checker.breakAllowedAtLocation(e.getBlock().getType(), e.getBlock().getLocation()))
		{
			messageDenyBlockBreak.send(e.getPlayer(), itemNameResolver.getName(e.getBlock()));
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onHangingPlace(HangingPlaceEvent e)
	{
		if(e.getPlayer() == null) return;
		Material mat = (e.getEntity() instanceof ItemFrame) ? Material.ITEM_FRAME : Material.PAINTING;
		if(!e.getPlayer().hasPermission(BYPASS_PERMISSION) && !checker.placeAllowedAtLocation(mat, e.getBlock().getRelative(e.getBlockFace()).getLocation()))
		{
			messageDenyHangingPlace.send(e.getPlayer(), itemNameResolver.getName(e.getBlock()));
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onHangingBreakByEntity(HangingBreakByEntityEvent e)
	{
		if(e.getRemover() instanceof Player)
		{
			Player player = (Player) e.getRemover();
			Material mat = (e.getEntity() instanceof ItemFrame) ? Material.ITEM_FRAME : Material.PAINTING;
			if(!player.hasPermission(BYPASS_PERMISSION) && !checker.breakAllowedAtLocation(mat, e.getEntity().getLocation()))
			{
				messageDenyHangingBreak.send(player, itemNameResolver.getName(mat));
				e.setCancelled(true);
			}
		}
	}
}

