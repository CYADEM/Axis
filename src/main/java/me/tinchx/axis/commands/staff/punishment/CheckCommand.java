package me.tinchx.axis.commands.staff.punishment;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.punishment.Punishment;
import me.tinchx.axis.punishment.PunishmentType;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.axis.utilities.item.ItemMaker;
import me.tinchx.axis.utilities.time.TimeUtil;
import me.tinchx.axis.inventory.InventoryMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckCommand extends AxisCommand {

    public CheckCommand() {
        super("check", null, "history", "hist");
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
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                sender.sendMessage(ColorText.translate("&c" + args[0] + " has never played before."));
                return false;
            }
            Profile profile = AxisAPI.getProfile(target.getUniqueId());
            InventoryMaker inventoryMaker = new InventoryMaker("&6Punishments of " + target.getName(), 1);

            inventoryMaker.setItem(1, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    InventoryMaker newInventory = new InventoryMaker("&6Punishments of " + target.getName(), 4);

                    for (Punishment punishment : profile.getPunishmentByType(PunishmentType.WARN)) {
                        newInventory.addItem(new InventoryMaker.ClickableItem() {
                            @Override
                            public void onClick(InventoryClickEvent event) {
                            }

                            @Override
                            public ItemStack getItemStack() {
                                List<String> lore = new ArrayList<>();
                                boolean addLine = punishment.isRemoved() || punishment.isActive();
                                int line = 0;
                                lore.add("&7&m--------------------");
                                lore.add("&eAdded at&7: &c" + TimeUtil.dateToString(new Date(punishment.getAddedAt())));
                                lore.add("&eAdded by&7: &c" + (punishment.getAddedBy() == null ? "Console" : Bukkit.getOfflinePlayer(punishment.getAddedBy()).getName()));
                                lore.add("&eAdded for&7: &c" + punishment.getAddedReason());
                                if (punishment.isActive()) {
                                    line = lore.size();
                                    lore.add("&eExpires at&7: &c" + punishment.getRemaining());
                                }
                                if (addLine) {
                                    lore.add(line, "&7&m--------------------");
                                }
                                lore.add("&7&m--------------------");
                                return new ItemMaker(Material.NAME_TAG).setDisplayname("&aWarn &7(" + punishment.getRemaining() + ')').addLore(lore).create();
                            }
                        });
                    }

                    player.openInventory(newInventory.getCurrentPage());
                }

                @Override
                public ItemStack getItemStack() {
                    return new ItemMaker(Material.STONE_AXE).setDisplayname("&a&lWarns &7(" + profile.getPunishmentByType(PunishmentType.WARN).size() + ')').create();
                }
            });

            inventoryMaker.setItem(3, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    InventoryMaker newInventory = new InventoryMaker("&6Punishments of " + target.getName(), 4);

                    for (Punishment punishment : profile.getPunishmentByType(PunishmentType.KICK)) {
                        newInventory.addItem(new InventoryMaker.ClickableItem() {
                            @Override
                            public void onClick(InventoryClickEvent event) {
                            }

                            @Override
                            public ItemStack getItemStack() {
                                List<String> lore = new ArrayList<>();
                                lore.add("&7&m--------------------");
                                lore.add("&eAdded at&7: &c" + TimeUtil.dateToString(new Date(punishment.getAddedAt())));
                                lore.add("&eAdded by&7: &c" + (punishment.getAddedBy() == null ? "Console" : Bukkit.getOfflinePlayer(punishment.getAddedBy()).getName()));
                                lore.add("&eAdded for&7: &c" + punishment.getAddedReason());
                                lore.add("&7&m--------------------");
                                return new ItemMaker(Material.NAME_TAG).setDisplayname("&eKick &7(" + punishment.getRemaining() + ')').addLore(lore).create();
                            }
                        });
                    }

                    player.openInventory(newInventory.getCurrentPage());
                }

                @Override
                public ItemStack getItemStack() {
                    return new ItemMaker(Material.IRON_AXE).setDisplayname("&e&lKicks &7(" + profile.getPunishmentByType(PunishmentType.KICK).size() + ')').create();
                }
            });

            inventoryMaker.setItem(5, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    InventoryMaker newInventory = new InventoryMaker("&6Punishments of " + target.getName(), 4);

                    for (Punishment punishment : profile.getPunishmentByType(PunishmentType.MUTE)) {
                        newInventory.addItem(new InventoryMaker.ClickableItem() {
                            @Override
                            public void onClick(InventoryClickEvent event) {
                                if (punishment.isActive()) {
                                    player.closeInventory();
                                    player.performCommand("unmute " + target.getName() + " GUI");
                                }
                            }

                            @Override
                            public ItemStack getItemStack() {
                                List<String> lore = new ArrayList<>();
                                boolean addLine = punishment.isRemoved() || punishment.isActive();
                                int line = 0;
                                lore.add("&7&m--------------------");
                                lore.add("&eAdded at&7: &c" + TimeUtil.dateToString(new Date(punishment.getAddedAt())));
                                lore.add("&eAdded by&7: &c" + (punishment.getAddedBy() == null ? "Console" : Bukkit.getOfflinePlayer(punishment.getAddedBy()).getName()));
                                lore.add("&eAdded for&7: &c" + punishment.getAddedReason());
                                if (punishment.isRemoved()) {
                                    line = lore.size();
                                    lore.add("&ePardoned at: &c" + TimeUtil.dateToString(new Date(punishment.getRemovedAt())));
                                    lore.add("&eRemoved by&7: &c" + (punishment.getRemovedBy() == null ? "Console" : Bukkit.getOfflinePlayer(punishment.getRemovedBy()).getName()));
                                    lore.add("&eRemoved for&7: &c" + punishment.getRemovedReason());
                                } else if (punishment.isActive()) {
                                    line = lore.size();
                                    lore.add("&eExpires at&7: &c" + punishment.getRemaining());
                                }
                                if (addLine) {
                                    lore.add(line, "&7&m--------------------");
                                }
                                lore.add("&7&m--------------------");
                                return new ItemMaker(Material.NAME_TAG).setDisplayname("&cMute &7(" + punishment.getRemaining() + ')').addLore(lore).create();
                            }
                        });
                    }

                    player.openInventory(newInventory.getCurrentPage());
                }


                @Override
                public ItemStack getItemStack() {
                    return new ItemMaker(Material.GOLD_AXE).setDisplayname("&c&lMutes &7(" + profile.getPunishmentByType(PunishmentType.MUTE).size() + ')').create();
                }
            });

            inventoryMaker.setItem(7, new InventoryMaker.ClickableItem() {
                @Override
                public void onClick(InventoryClickEvent event) {
                    InventoryMaker newInventory = new InventoryMaker("&6Punishments of " + target.getName(), 4);

                    for (Punishment punishment : profile.getPunishments()) {
                        if (!(punishment.isBan())) {
                            continue;
                        }
                        newInventory.addItem(new InventoryMaker.ClickableItem() {
                            @Override
                            public void onClick(InventoryClickEvent event) {
                                if (punishment.isActive()) {
                                    player.closeInventory();
                                    player.performCommand("unban " + target.getName() + " GUI");
                                }
                            }

                            @Override
                            public ItemStack getItemStack() {
                                List<String> lore = new ArrayList<>();
                                boolean addLine = punishment.isRemoved() || punishment.isActive();
                                int line = 0;
                                lore.add("&7&m--------------------");
                                lore.add("&eAdded at&7: &c" + TimeUtil.dateToString(new Date(punishment.getAddedAt())));
                                lore.add("&eAdded by&7: &c" + (punishment.getAddedBy() == null ? "Console" : Bukkit.getOfflinePlayer(punishment.getAddedBy()).getName()));
                                lore.add("&eAdded for&7: &c" + punishment.getAddedReason());
                                if (punishment.isRemoved()) {
                                    line = lore.size();
                                    lore.add("&ePardoned at: &c" + TimeUtil.dateToString(new Date(punishment.getRemovedAt())));
                                    lore.add("&eRemoved by&7: &c" + (punishment.getRemovedBy() == null ? "Console" : Bukkit.getOfflinePlayer(punishment.getRemovedBy()).getName()));
                                    lore.add("&eRemoved for&7: &c" + punishment.getRemovedReason());
                                } else if (punishment.isActive()) {
                                    line = lore.size();
                                    lore.add("&eExpires at&7: &c" + punishment.getRemaining());
                                }
                                if (addLine) {
                                    lore.add(line, "&7&m--------------------");
                                }
                                lore.add("&7&m--------------------");
                                return new ItemMaker(Material.NAME_TAG).setDisplayname("&4" + punishment.getType().name() + " &7(" + punishment.getRemaining() + ')').addLore(lore).create();
                            }
                        });
                    }

                    player.openInventory(newInventory.getCurrentPage());
                }


                @Override
                public ItemStack getItemStack() {
                    int total = 0;
                    for (Punishment punishment : profile.getPunishments()) {
                        if (punishment.isBan()) {
                            total++;
                        }
                    }
                    return new ItemMaker(Material.DIAMOND_AXE).setDisplayname("&4&lBans &7(" + total + ')').create();
                }
            });

            player.openInventory(inventoryMaker.getCurrentPage());
        }
        return true;
    }
}