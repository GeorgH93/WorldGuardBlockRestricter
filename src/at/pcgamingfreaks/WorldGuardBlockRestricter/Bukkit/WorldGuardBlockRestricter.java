package at.pcgamingfreaks.WorldGuardBlockRestricter.Bukkit;

import at.pcgamingfreaks.Bukkit.Updater;
import at.pcgamingfreaks.Configuration;
import at.pcgamingfreaks.StringUtils;
import at.pcgamingfreaks.Bukkit.Language;
import at.pcgamingfreaks.Updater.UpdateProviders.JenkinsUpdateProvider;
import at.pcgamingfreaks.WorldGuardBlockRestricter.Bukkit.Flags.BlockMaterialFlag;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.SetFlag;

import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.AccessLevel;
import lombok.Getter;

public class WorldGuardBlockRestricter extends JavaPlugin
{
	@Getter private static WorldGuardBlockRestricter instance;
	public static final BlockMaterialFlag BLOCK_TYPE_FLAG = new BlockMaterialFlag("block-type");
	public static final SetFlag<Material> ALLOW_BLOCK_FLAG = new SetFlag<>("allow-blocks", BLOCK_TYPE_FLAG);
	public static final SetFlag<Material> DENY_BLOCK_FLAG = new SetFlag<>("deny-blocks", BLOCK_TYPE_FLAG);
	public static final SetFlag<Material> ALLOW_PLACE_FLAG = new SetFlag<>("allow-place", BLOCK_TYPE_FLAG);
	public static final SetFlag<Material> DENY_PLACE_FLAG = new SetFlag<>("deny-place", BLOCK_TYPE_FLAG);
	public static final SetFlag<Material> ALLOW_BREAK_FLAG = new SetFlag<>("allow-break", BLOCK_TYPE_FLAG);
	public static final SetFlag<Material> DENY_BREAK_FLAG = new SetFlag<>("deny-break", BLOCK_TYPE_FLAG);
	private WorldGuard wg = null;
	private WGBlockRestricterChecker checker = null;
	@Getter(AccessLevel.PACKAGE) private Language language;
	private Configuration config;

	public String messageUnknownMaterial;

	@Override
	public void onLoad()
	{
		wg = WorldGuard.getInstance();
		wg.getFlagRegistry().register(ALLOW_BLOCK_FLAG);
		wg.getFlagRegistry().register(DENY_BLOCK_FLAG);
		wg.getFlagRegistry().register(ALLOW_PLACE_FLAG);
		wg.getFlagRegistry().register(DENY_PLACE_FLAG);
		wg.getFlagRegistry().register(ALLOW_BREAK_FLAG);
		wg.getFlagRegistry().register(DENY_BREAK_FLAG);
		getLogger().info("Registered flags with WorldGuard.");
	}

	@Override
	public void onEnable()
	{
		config = new at.pcgamingfreaks.Bukkit.Configuration(this, 1);
		language = new Language(this, 1);
		language.load(config);

		messageUnknownMaterial = language.getTranslated("UnknownMaterial").replaceAll("\\{Input}", "%s");

		checker = new WGBlockRestricterChecker(wg);
		getServer().getPluginManager().registerEvents(new BlockListener(this, checker), this);
		instance = this;
		getLogger().info(StringUtils.getPluginEnabledMessage(getDescription().getFullName()));
	}

	@Override
	public void onDisable()
	{
		Updater updater = new Updater(this, this.getFile(), true, new JenkinsUpdateProvider("https://ci.pcgamingfreaks.at", "WorldGuardBlockRestricter", this.getLogger()));
		updater.update();
		language = null;
		wg = null;
		checker = null;
		HandlerList.unregisterAll(this);
		updater.waitForAsyncOperation();
		instance = null;
		getLogger().info(StringUtils.getPluginDisabledMessage(getDescription().getFullName()));
	}
}