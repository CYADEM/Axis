package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.axis.utilities.item.ItemMaker;
import me.tinchx.axis.inventory.InventoryMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WorldCommand extends AxisCommand {

    public WorldCommand() {
        super("world");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        InventoryMaker inventoryMaker = new InventoryMaker("&6&lWorld GUI", 1);

        Bukkit.getWorlds().forEach(world -> inventoryMaker.addItem(new InventoryMaker.ClickableItem() {
            @Override
            public void onClick(InventoryClickEvent event) {
                player.closeInventory();
                if (player.getWorld() != world) {
                    player.teleport(world.getSpawnLocation());
                    player.sendMessage(ColorText.translate("&6You are now in world &f" + world.getName() + "&6!"));
                }
            }

            @Override
            public ItemStack getItemStack() {
                boolean isNether = world.getEnvironment() == World.Environment.NETHER;
                boolean isEnd = world.getEnvironment() == World.Environment.THE_END;
                return new ItemMaker((isNether ? Material.NETHERRACK : isEnd ? Material.ENDER_STONE : Material.GRASS)).setDisplayname((isNether ? "&c" : isEnd ? "&b" : "&a") + world.getName()).addLore((player.getWorld() == world ? "&cYou are already in that world!" : "&7&oClick to teleport")).create();
            }
        }));

        player.openInventory(inventoryMaker.getCurrentPage());
        return true;
    }
}