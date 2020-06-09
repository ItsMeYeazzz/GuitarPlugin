package com.gmail.perhapsitisyeazz.listener;

import com.gmail.perhapsitisyeazz.GuitarPlugin;
import com.moderocky.mask.gui.ItemFactory;
import com.moderocky.mask.gui.VisualGUI;
import com.moderocky.mask.template.CompleteListener;
import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.EntitySongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class PlayerGuitar implements CompleteListener {

	@SuppressWarnings("all")
	@EventHandler
	public void onGuitarClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack item = event.getItem();
		Block block = event.getClickedBlock();
		if (  item != null && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) ) {
			if ( block != null && !block.getBlockData().getAsString().toString().contains("door") ) {
				ItemMeta meta = item.getItemMeta();
				if (item.getType() == Material.JIGSAW && meta.getDisplayName().equals("§7Guitar") && meta.hasCustomModelData() && meta.getCustomModelData() == 1) {
					event.setCancelled(true);
					MusicGui(player, 1);
					guitarStopSong(player);
				}
			}
		}
	}

	@SuppressWarnings("all")
	private void MusicGui(Player player, Integer page) {
		ItemStack RingOfFire = new ItemFactory(Material.MUSIC_DISC_13).addConsumer(itemMeta -> {
			itemMeta.setDisplayName("§eRing of Fire");
			itemMeta.setLore(Arrays.asList("§7", "§7Song made by Johnny Cash"));
		}).create();
		ItemStack FolsomPrisonBlues = new ItemFactory(Material.MUSIC_DISC_WAIT).addConsumer(itemMeta -> {
			itemMeta.setDisplayName("§eFolsom Prison Blues");
			itemMeta.setLore(Arrays.asList("§7", "§7Song made by Johnny Cash"));
		}).create();
		ItemStack Hurt = new ItemFactory(Material.MUSIC_DISC_CAT).addConsumer(itemMeta -> {
			itemMeta.setDisplayName("§eHurt");
			itemMeta.setLore(Arrays.asList("§7", "§7Song made by Johnny Cash"));
		}).create();
		ItemStack DancingWithMyself = new ItemFactory(Material.MUSIC_DISC_STRAD).addConsumer(itemMeta -> {
			itemMeta.setDisplayName("§eDancing With Myself");
			itemMeta.setLore(Arrays.asList("§7", "§7Song made by Billy Idol"));
		}).create();
		ItemStack BigIron = new ItemFactory(Material.MUSIC_DISC_BLOCKS).addConsumer(itemMeta -> {
			itemMeta.setDisplayName("§eBig Iron");
			itemMeta.setLore(Arrays.asList("§7", "§7Song made by Marty Robbins"));
		}).create();
		VisualGUI gui = new VisualGUI(GuitarPlugin.getInstance(), 27, "Wanna play a song ? - Page " + page)
				.setLayout(new String[]{
						"#########",
						"#ABCDEFG#",
						"###1#2###"
				});
		if (page == 1) {
			gui.createButton('A', RingOfFire, (clicker, event) -> {
				Bukkit.getScheduler().runTaskLater(GuitarPlugin.getInstance(), () -> clicker.closeInventory(InventoryCloseEvent.Reason.PLUGIN), 0);
				guitarPlayMusic(clicker, "Ring Of Fire");
			});
			gui.createButton('B', FolsomPrisonBlues, (clicker, event) -> {
				Bukkit.getScheduler().runTaskLater(GuitarPlugin.getInstance(), () -> clicker.closeInventory(InventoryCloseEvent.Reason.PLUGIN), 0);
				guitarPlayMusic(clicker, "Folsom Prison Blues");
			});
			gui.createButton('C', Hurt, (clicker, event) -> {
				Bukkit.getScheduler().runTaskLater(GuitarPlugin.getInstance(), () -> clicker.closeInventory(InventoryCloseEvent.Reason.PLUGIN), 0);
				guitarPlayMusic(clicker, "Hurt");
			});
			gui.createButton('2', new ItemFactory(Material.ARROW)
					.addConsumer(itemMeta -> {
						itemMeta.setDisplayName("§7Next page " + (page + 1) + "/10");
					}).create(), (clicker, event) -> {
				Bukkit.getScheduler().runTaskLater(GuitarPlugin.getInstance(), () -> MusicGui(clicker, page + 1), 1);
			});
		} else if (page == 2) {
			gui.createButton('A', DancingWithMyself, (clicker, event) -> {
				Bukkit.getScheduler().runTaskLater(GuitarPlugin.getInstance(), () -> clicker.closeInventory(InventoryCloseEvent.Reason.PLUGIN), 0);
				guitarPlayMusic(clicker, "Dancing With Myself");
			});
			gui.createButton('B', BigIron, (clicker, event) -> {
				Bukkit.getScheduler().runTaskLater(GuitarPlugin.getInstance(), () -> clicker.closeInventory(InventoryCloseEvent.Reason.PLUGIN), 0);
				guitarPlayMusic(clicker, "Big Iron");
			});
			gui.createButton('1', new ItemFactory(Material.ARROW)
					.addConsumer(itemMeta -> {
						itemMeta.setDisplayName("§7Next page " + (page - 1) + "/10");
					}).create(), (clicker, event) -> {
				Bukkit.getScheduler().runTaskLater(GuitarPlugin.getInstance(), () -> MusicGui(clicker, page - 1), 1);
			});
		}
		gui.finalise();
		gui.open(player);
	}

	private void guitarPlayMusic(Player player, String string) {
		Song song = NBSDecoder.parse(new File(GuitarPlugin.plugin.getDataFolder(), string + ".nbs"));
		EntitySongPlayer esp = new EntitySongPlayer(song);
		esp.setEntity(player);
		esp.setDistance(16);
		esp.addPlayer(player);
		for( Player allPlayers : Bukkit.getOnlinePlayers() ) {
			esp.addPlayer(allPlayers);
		}
		esp.setPlaying(true);
		player.sendActionBar("§aNow playing : §e" + string);
		GuitarPlugin.playerCurrentSong.put(player.getUniqueId(), esp);
	}

	private void guitarStopSong(Player player) {
		if ( GuitarPlugin.playerCurrentSong.containsKey(player.getUniqueId()) ) {
			SongPlayer sp = GuitarPlugin.playerCurrentSong.get(player.getUniqueId());
			sp.setPlaying(false);
			player.sendActionBar("§aMusic Stopped");
			GuitarPlugin.playerCurrentSong.remove(player.getUniqueId());
		}
	}

	@EventHandler
	public void onTakeGuitar(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		if ( item != null) {
			ItemMeta meta = item.getItemMeta();
			if ( item.getType() == Material.JIGSAW && meta.getDisplayName().equals("§7Guitar") && meta.hasCustomModelData() && meta.getCustomModelData() == 1 ) {
				if ( GuitarPlugin.playerCurrentSong.containsKey(player.getUniqueId()) ) {
					BukkitRunnable runnable;
					runnable = new BukkitRunnable() {
						@Override
						public void run() {
							ItemStack mainItem = player.getInventory().getItemInMainHand();
							ItemStack offItem = player.getInventory().getItemInOffHand();
							if ( mainItem.getType() != Material.JIGSAW && offItem.getType() != Material.JIGSAW ) {
								guitarStopSong(player);
							}
						}
					};
					runnable.runTaskLater(GuitarPlugin.getInstance(), 1);
				}
			}
		}
	}

	@SuppressWarnings("all")
	@EventHandler
	public void onHandChange(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		ItemMeta meta = item.getItemMeta();
		if ( item.getType() == Material.JIGSAW && meta.getDisplayName().equals("§7Guitar") && meta.hasCustomModelData() && meta.getCustomModelData() == 1 ) {
			BukkitRunnable runnable;
			runnable = new BukkitRunnable() {
				@Override
				public void run() {
					ItemStack offItem = player.getInventory().getItemInOffHand();
					if ( offItem.getType() != Material.JIGSAW ) {
						guitarStopSong(player);
					}
				}
			};
			runnable.runTaskLater(GuitarPlugin.getInstance(), 1);
		}
	}

	@EventHandler
	private void onSongEnd(SongEndEvent event) {
		SongPlayer sp = event.getSongPlayer();
		for ( UUID uuid : sp.getPlayerUUIDs() ) {
			GuitarPlugin.playerCurrentSong.remove(uuid);
		}
	}
}
