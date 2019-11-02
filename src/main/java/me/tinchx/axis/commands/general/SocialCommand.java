package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.axis.utilities.item.ItemMaker;
import me.tinchx.axis.inventory.InventoryMaker;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SocialCommand extends AxisCommand {

    public SocialCommand() {
        super("social");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        Profile profile = AxisAPI.getProfile(player);
        InventoryMaker inventoryMaker = new InventoryMaker("&c&lSocials", 2);

        inventoryMaker.setItem(2, new InventoryMaker.ClickableItem() {
            @Override
            public void onClick(InventoryClickEvent event) {

            }

            @Override
            public ItemStack getItemStack() {
                return new ItemMaker(Material.WOOL).setDurability(1).setDisplayname("&6&lReddit").addLore("&7Link: " + (profile.getProfileReddit() == null ? "&fNone" : "&6" + profile.getProfileReddit())).create();
            }
        });

        inventoryMaker.setItem(4, new InventoryMaker.ClickableItem() {
            @Override
            public void onClick(InventoryClickEvent event) {

            }

            @Override
            public ItemStack getItemStack() {
                return new ItemMaker(Material.WOOL).setDurability(14).setDisplayname("&4&lYou&f&lTube").addLore("&7Link: " + (profile.getProfileYoutube() == null ? "&fNone" : "&6" + profile.getProfileYoutube())).create();
            }
        });

        inventoryMaker.setItem(6, new InventoryMaker.ClickableItem() {
            @Override
            public void onClick(InventoryClickEvent event) {

            }

            @Override
            public ItemStack getItemStack() {
                return new ItemMaker(Material.WOOL).setDurability(8).setDisplayname("&8&lGithub").addLore("&7Link: " + (profile.getProfileGithub() == null ? "&fNone" : "&6" + profile.getProfileGithub())).create();
            }
        });

        inventoryMaker.setItem(12, new InventoryMaker.ClickableItem() {
            @Override
            public void onClick(InventoryClickEvent event) {

            }

            @Override
            public ItemStack getItemStack() {
                return new ItemMaker(Material.WOOL).setDurability(9).setDisplayname("&3&lDiscord").addLore("&7Discord ID: " + (profile.getProfileDiscord() == null ? "&fNone" : "&6" + profile.getProfileDiscord())).create();
            }
        });

        inventoryMaker.setItem(14, new InventoryMaker.ClickableItem() {
            @Override
            public void onClick(InventoryClickEvent event) {

            }

            @Override
            public ItemStack getItemStack() {
                return new ItemMaker(Material.WOOL).setDurability(3).setDisplayname("&b&lTwitter").addLore("&7Link: " + (profile.getProfileTwitter() == null ? "&fNone" : "&6" + profile.getProfileTwitter())).create();
            }
        });

        player.openInventory(inventoryMaker.getCurrentPage());
        return true;
    }
}