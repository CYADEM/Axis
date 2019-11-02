package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.listeners.events.PlayerToggleModEvent;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.axis.utilities.item.ItemMaker;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class StaffCommand extends AxisCommand {

    public StaffCommand() {
        super("staff", null, "staffmode", "mod", "modmode", "h");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        Profile profile = AxisAPI.getProfile(player);
        PlayerInventory inventory = player.getInventory();
        profile.setStaffMode(!profile.isStaffMode());
        if (profile.isStaffMode()) {
            profile.getArmorContents().put(profile.getUuid(), inventory.getArmorContents());
            profile.getInventoryContents().put(profile.getUuid(), inventory.getContents());

            inventory.clear();
            inventory.setArmorContents(null);

            inventory.setItem(0, new ItemMaker(Material.COMPASS).setDisplayname("&e&lTeleport Tool").create());
            inventory.setItem(1, new ItemMaker(Material.BOOK).setDisplayname("&b&lInventory Viewer").create());
            inventory.setItem(2, new ItemMaker(Material.BLAZE_ROD).setDisplayname("&4&lHalt Cheater").create());

            if (player.isOp()) {
                inventory.setItem(4, new ItemMaker(Material.WOOD_AXE).setDisplayname("&c&lWorldEdit Wand").create());
            }

            inventory.setItem(6, new ItemMaker(Material.DIAMOND_PICKAXE).setDisplayname("&b&lX-Ray Finder").create());
            inventory.setItem(7, new ItemMaker(Material.SKULL_ITEM).setDurability(3).setDisplayname("&d&lOnline Staff").create());
            inventory.setItem(8, new ItemMaker(Material.INK_SACK).setDurability(8).setDisplayname("&a&lBecome Visible").create());

            profile.setVanished(true);

            player.setGameMode((player.isOp() ? GameMode.CREATIVE : GameMode.ADVENTURE));
            player.setFoodLevel(20);
            player.setHealth(player.getMaxHealth());

            player.setAllowFlight(true);
            player.setFlying(true);

            player.updateInventory();

        } else {
            player.setGameMode((player.isOp() ? GameMode.CREATIVE : GameMode.SURVIVAL));

            profile.setVanished(false);

            profile.rollbackInventory();
        }
        new PlayerToggleModEvent(player, profile.isStaffMode()).call();
        player.sendMessage(AxisConfiguration.getString("staff_mode_" + (profile.isStaffMode() ? "en" : "dis") + "abled"));
        return true;
    }
}