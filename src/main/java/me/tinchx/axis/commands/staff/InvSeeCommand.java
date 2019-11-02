package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.axis.utilities.item.ItemMaker;
import me.tinchx.axis.inventory.InventoryMaker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvSeeCommand extends AxisCommand {

    public InvSeeCommand() {
        super("invsee", null, "inventorysee");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(target)) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }

            if (AxisAPI.getProfile(target).isStaffMode()) {
                return false;
            }

            InventoryMaker inventoryMaker = new InventoryMaker("&7Inventory: " + AxisAPI.getRank(target).getColor() + target.getName(), 6);
            PlayerInventory playerInventory = target.getInventory();

            for (ItemStack itemStack : playerInventory.getContents()) {
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    continue;
                }
                inventoryMaker.addItem(new InventoryMaker.ClickableItem() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        if (target.isOnline()) {
                            target.getInventory().remove(itemStack);
                            target.updateInventory();
                        }
                    }

                    @Override
                    public ItemStack getItemStack() {
                        return itemStack;
                    }
                });
            }

            List<ItemStack> itemStacks = new ArrayList<>(Arrays.asList(playerInventory.getArmorContents()));
            Collections.reverse(itemStacks);

            inventoryMaker.setItem(45, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    if (target.isOnline()) {
                        playerInventory.setHelmet(null);
                        target.updateInventory();
                    }
                }

                @Override
                public ItemStack getItemStack() {
                    return playerInventory.getHelmet();
                }
            });

            inventoryMaker.setItem(46, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    if (target.isOnline()) {
                        playerInventory.setChestplate(null);
                        target.updateInventory();
                    }
                }

                @Override
                public ItemStack getItemStack() {
                    return playerInventory.getChestplate();
                }
            });

            inventoryMaker.setItem(47, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    if (target.isOnline()) {
                        playerInventory.setLeggings(null);
                        target.updateInventory();
                    }
                }

                @Override
                public ItemStack getItemStack() {
                    return playerInventory.getLeggings();
                }
            });

            inventoryMaker.setItem(48, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    if (target.isOnline()) {
                        playerInventory.setBoots(null);
                        target.updateInventory();
                    }
                }

                @Override
                public ItemStack getItemStack() {
                    return playerInventory.getBoots();
                }
            });

            inventoryMaker.setItem(49, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                }

                @Override
                public ItemStack getItemStack() {
                    return new ItemMaker(Material.STAINED_GLASS_PANE).setDisplayname(" ").setDurability(7).create();
                }
            });

            inventoryMaker.setItem(50, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    if (target.isOnline()) {
                        target.getInventory().remove(playerInventory.getItemInHand());
                        target.updateInventory();
                    }
                }

                @Override
                public ItemStack getItemStack() {
                    return playerInventory.getItemInHand();
                }
            });

            inventoryMaker.setItem(51, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                }

                @Override
                public ItemStack getItemStack() {
                    return new ItemMaker(Material.STAINED_GLASS_PANE).setDisplayname(" ").setDurability(7).create();
                }
            });

            inventoryMaker.setItem(52, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    if (target.isOnline()) {
                        player.performCommand("ss " + target.getName());
                    }
                }

                @Override
                public ItemStack getItemStack() {
                    return new ItemMaker(Material.BLAZE_ROD).setDisplayname("&4&lHalt Player").create();
                }
            });

            inventoryMaker.setItem(53, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    if (target.isOnline()) {
                        player.performCommand("tp " + target.getName());
                    }
                }

                @Override
                public ItemStack getItemStack() {
                    Location location = target.getLocation();
                    return new ItemMaker(Material.PAPER).setDisplayname("&9&l» &6&lPlayer Info &9&l«").addLore("&6&lLocation:", " &7* &eWorld: &f" + location.getWorld().getName(), " &7* &eX: &f" + location.getBlock().getX(), " &7* &eY: &f" + location.getBlock().getY(), " &7* &eZ: &f" + location.getBlock().getZ(), "&6&lPing:", " &7* &eLatency: &f" + AxisAPI.getPing(target) + "ms", "&6&lRank:", " &7* &eRank: " + AxisAPI.getRank(target).getColor() + AxisAPI.getRank(target).getName()).create();
                }
            });

            for (int i = 36; i < 45; i++) {
                inventoryMaker.setItem(i, new InventoryMaker.ClickableItem() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                    }

                    @Override
                    public ItemStack getItemStack() {
                        return new ItemMaker(Material.STAINED_GLASS_PANE).setDisplayname(" ").setDurability(7).create();
                    }
                });
            }

            player.openInventory(inventoryMaker.getCurrentPage());
            player.sendMessage(ColorText.translate("&eOpening inventory of: &b" + target.getName()));
        }
        return true;
    }
}