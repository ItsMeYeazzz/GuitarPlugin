package com.gmail.perhapsitisyeazz;

import com.gmail.perhapsitisyeazz.command.GiveGuitarCommand;
import com.gmail.perhapsitisyeazz.listener.PlayerGuitar;
import com.moderocky.mask.template.BukkitPlugin;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class GuitarPlugin extends BukkitPlugin {

	public static Plugin plugin;

	private static GuitarPlugin instance;

	public static GuitarPlugin getInstance() {
		return instance;
	}

	public static HashMap<UUID, SongPlayer> playerCurrentSong = new HashMap<>();

	@Override
	public void startup() {

		plugin = this;
		instance = this;

		getLogger().info("====================================");
		getLogger().info("Plugin initialization in progess ...");
		if ( !plugin.getDataFolder().exists() ) {
			getLogger().info("The plugins/PluginTest/ was not found, creation in progress ...");
			plugin.getDataFolder().mkdir();
			getLogger().info("Data folder created.");
			AddSongs.addSongs("Hurt.nbs");
			getLogger().info("Hurt song added by default.");
			AddSongs.addSongs("Ring_Of_Fire.nbs");
			getLogger().info("Ring Of Fire song added by default.");
		}
		register(
				new GiveGuitarCommand()
		);
		getLogger().info("Command register successfully.");
		register(
				new PlayerGuitar()
		);
		getLogger().info("Listener register successfully.");
		getLogger().info("====================================");
	}

	@Override
	public void disable() {
		instance = null;
	}
}
