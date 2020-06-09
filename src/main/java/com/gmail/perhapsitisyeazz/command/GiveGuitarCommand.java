package com.gmail.perhapsitisyeazz.command;

import com.moderocky.mask.command.Commander;
import com.moderocky.mask.gui.ItemFactory;
import com.moderocky.mask.template.WrappedCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GiveGuitarCommand extends Commander<Player> implements WrappedCommand {

    @SuppressWarnings("all")
    @Override
    public @NotNull Main create() {
        return command("guitar")
                .arg(
                        (player, input) -> {
                            giveGuitar(player);
                        }
                );
    }

    @Override
    public @NotNull Consumer<Player> getDefault(){
        return this::giveGuitar;
    }

    @Override
    public @NotNull List<String> getAliases(){
        return new ArrayList<>();
    }

    @Override
    public @NotNull String getUsage(){
        return "/" + getCommand();
    }

    @Override
    public @NotNull String getDescription(){
        return "Give you the guitar.";
    }

    @Override
    public @Nullable String getPermission(){
        return null;
    }

    @Override
    public @Nullable String getPermissionMessage(){
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        return execute((Player) sender, args);
    }

    private void giveGuitar(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack item = new ItemFactory(Material.JIGSAW).addConsumer(itemMeta -> {
            itemMeta.setDisplayName("§7Guitar");
            itemMeta.setCustomModelData(1);
        }).create();
        if ( inventory.getItemInMainHand().getType() == Material.AIR )
            inventory.setItemInMainHand(item);
        else
            inventory.addItem(item);
        player.sendMessage("§aYou have received a guitar.");
    }
}
