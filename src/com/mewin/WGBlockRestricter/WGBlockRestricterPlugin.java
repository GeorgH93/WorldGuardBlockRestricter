/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.mewin.WGCustomFlags.WGCustomFlagsPlugin
 *  com.mewin.WGCustomFlags.flags.CustomSetFlag
 *  com.sk89q.worldguard.bukkit.WorldGuardPlugin
 *  com.sk89q.worldguard.protection.flags.Flag
 *  org.bukkit.Server
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.mewin.WGBlockRestricter;

import com.mewin.WGBlockRestricter.Database.Config;
import com.mewin.WGBlockRestricter.Flags.BlockMaterialFlag;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.SetFlag;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WGBlockRestricterPlugin extends JavaPlugin
{
	public static final BlockMaterialFlag BLOCK_TYPE_FLAG = new BlockMaterialFlag("block-type");
	public static final SetFlag<Material> ALLOW_BLOCK_FLAG = new SetFlag<>("allow-blocks", BLOCK_TYPE_FLAG);
	public static final SetFlag<Material> DENY_BLOCK_FLAG = new SetFlag<>("deny-blocks", BLOCK_TYPE_FLAG);
	public static final SetFlag<Material> ALLOW_PLACE_FLAG = new SetFlag<>("allow-place", BLOCK_TYPE_FLAG);
	public static final SetFlag<Material> DENY_PLACE_FLAG = new SetFlag<>("deny-place", BLOCK_TYPE_FLAG);
	public static final SetFlag<Material> ALLOW_BREAK_FLAG = new SetFlag<>("allow-break", BLOCK_TYPE_FLAG);
	public static final SetFlag<Material> DENY_BREAK_FLAG = new SetFlag<>("deny-break", BLOCK_TYPE_FLAG);
	private BlockListener listener;
	private WorldGuardPlugin wgPlugin;
	public Config config;

	@Override
	public void onLoad()
	{
		this.wgPlugin = this.getWorldGuard();
		if(this.wgPlugin == null)
		{
			this.getLogger().warning("This plugin requires WorldGuard, disabling.");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		wgPlugin.getFlagRegistry().register(ALLOW_BLOCK_FLAG);
		wgPlugin.getFlagRegistry().register(DENY_BLOCK_FLAG);
		wgPlugin.getFlagRegistry().register(ALLOW_PLACE_FLAG);
		wgPlugin.getFlagRegistry().register(DENY_PLACE_FLAG);
		wgPlugin.getFlagRegistry().register(ALLOW_BREAK_FLAG);
		wgPlugin.getFlagRegistry().register(DENY_BREAK_FLAG);
	}

	@Override
	public void onEnable()
	{
		config = new Config(this);
		this.listener = new BlockListener(this, this.wgPlugin);
		this.getServer().getPluginManager().registerEvents(this.listener, this);
		Utils.init();
	}

	private WorldGuardPlugin getWorldGuard()
	{
		Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
		if(plugin == null || !(plugin instanceof WorldGuardPlugin))
		{
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}
}