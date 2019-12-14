package com.mewin.WGBlockRestricter.Database;

import at.pcgamingfreaks.Bukkit.Configuration;

import org.bukkit.plugin.java.JavaPlugin;

public class Config extends Configuration
{
	private static final int VERSION = 1;

	public Config(JavaPlugin plugin)
	{
		super(plugin, VERSION);
	}
}