package me.tinchx.axis.cosmetic;

import com.google.common.primitives.Ints;
import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisPlugin;
import me.tinchx.axis.configuration.Config;
import me.tinchx.axis.inventory.InventoryMaker;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.axis.utilities.item.ItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PrefixCommand extends AxisCommand {

    private Config settings = new Config(AxisPlugin.getPlugin(), "settings.yml");

    public PrefixCommand() {
        super("prefix", null, "tags", "titles", "prefixes");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1 || !player.isOp()) {
            player.performCommand("prefix " + player.getName());
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                player.sendMessage(ColorText.translate("&c" + args[0] + " has never played before."));
                return false;
            }

            Profile profile = AxisAPI.getProfile(target.getUniqueId());

            InventoryMaker inventoryMaker = new InventoryMaker(settings.getString("prefixes-inventory.title"), settings.getInt("prefixes-inventory.rows"));

            for (String path : settings.getConfigurationSection("prefixes-section").getKeys(false)) {
                String pathName = "prefixes-section." + path + '.';
                String[] slots = settings.getString(pathName + "slots").split(",");

                for (String slot : slots) {
                    try {
                        Integer integer = Ints.tryParse(slot);

                        inventoryMaker.setItem((integer - 1), new InventoryMaker.ClickableItem() {
                            @Override
                            public void onClick(InventoryClickEvent event) {
                                if (settings.getBoolean(pathName + "clickable")) {
                                    InventoryMaker maker = new InventoryMaker(settings.getString(pathName + "section.title"), settings.getInt(pathName + "section.rows"));

                                    for (Tag tag : Tag.getTagsBySection(settings.getString(pathName + "section.name").toUpperCase())) {
                                        if (tag.getPrefix() == null) {
                                            continue;
                                        }
                                        maker.addItem(new InventoryMaker.ClickableItem() {
                                            @Override
                                            public void onClick(InventoryClickEvent event) {
                                                if (tag.getPermission() != null && !player.hasPermission(tag.getPermission())) {
                                                    return;
                                                }

                                                if (profile.getTag() != null && profile.getTag().equals(tag)) {
                                                    return;
                                                }

                                                player.closeInventory();

                                                profile.setTag(tag);
                                                profile.updateProfile();
                                                profile.save();

                                                if (player == target) {
                                                    player.sendMessage(ColorText.translate("&aYour tag has been changed to " + tag.getPrefix() + "&a."));
                                                } else {
                                                    player.sendMessage(ColorText.translate("&aYou've updated " + target.getName() + '\'' + (target.getName().endsWith("s") ? "" : "s") + " tag updated to " + tag.getPrefix() + "&a."));
                                                }
                                            }

                                            @Override
                                            public ItemStack getItemStack() {
                                                List<String> lore = new ArrayList<>();

                                                for (String s : settings.getStringList(pathName + "tag.material.lore")) {
                                                    lore.add(new MessageUtil().setVariable("{TAG_NAME}", tag.getName()).setVariable("{TAG_PREFIX}", tag.getPrefix()).setVariable("{TAG_PREFIX}", tag.getPrefix()).setVariable("{TAG_STATUS}", (profile.getTag() == tag ? settings.getString(pathName + "tag.selected") : tag.getPermission() != null && !player.hasPermission(tag.getPermission()) ? settings.getString(pathName + "tag.no-perms") : settings.getString(pathName + "tag.select"))).format(s));
                                                }

                                                return new ItemMaker(Material.getMaterial(settings.getInt(pathName + "tag.material.type"))).setDurability(settings.getInt(pathName + "tag.material.data")).setDisplayname(new MessageUtil().setVariable("{TAG_NAME}", tag.getName()).setVariable("{TAG_PREFIX}", tag.getPrefix()).setVariable("{TAG_STATUS}", (profile.getTag() == tag ? settings.getString(pathName + "tag.selected") : tag.getPermission() != null && !player.hasPermission(tag.getPermission()) ? settings.getString(pathName + "tag.no-perms") : settings.getString(pathName + "tag.select"))).format(settings.getString(pathName + "tag.material.display-name"))).addLore(lore).create();
                                            }
                                        });
                                    }

                                    player.openInventory(maker.getCurrentPage());
                                }
                            }

                            @Override
                            public ItemStack getItemStack() {
                                return new ItemMaker(Material.getMaterial(settings.getInt(pathName + "material.type"))).setDurability(settings.getInt(pathName + "material.data")).setDisplayname(settings.getString(pathName + "material.display-name")).addLore(settings.getStringList(pathName + "material.lore")).create();
                            }
                        });

                    } catch (Exception ignored) {

                    }
                }
            }

            player.openInventory(inventoryMaker.getCurrentPage());

            /*InventoryMaker maker = new InventoryMaker("&d&lSelect a Prefix", 5);

            for (Tag tag : Tag.getTags()) {
                if (tag.getPrefix() == null) {
                    continue;
                }
                maker.addItem(new InventoryMaker.ClickableItem() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        if (tag.getPermission() != null && !player.hasPermission(tag.getPermission())) {
                            return;
                        }

                        if (profile.getTag() != null && profile.getTag().equals(tag)) {
                            return;
                        }

                        player.closeInventory();

                        profile.setTag(tag);
                        profile.updateProfile();
                        profile.save();

                        player.sendMessage(ColorText.translate("&aTag changed to " + tag.getPrefix() + "&a!"));
                    }

                    @Override
                    public ItemStack getItemStack() {
                        boolean isEqual = profile.getTag() != null && profile.getTag().equals(tag);
                        List<String> lore = new ArrayList<>();
                        lore.add("&7Shows up as:");
                        lore.add(tag.getPrefix());
                        lore.add("");
                        if (isEqual) {
                            lore.add("&aTag selected.");
                        } else if (tag.getPermission() == null || player.hasPermission(tag.getPermission())) {
                            lore.add("&aClick to activate.");
                        } else {
                            lore.add("&cYou don't own this tag.");
                        }
                        return new ItemMaker(Material.NAME_TAG).setDisplayname("&e" + tag.getName() + (isEqual ? " &7(Selected)" : "")).addLore(lore).create();
                    }
                });
            }

            if (profile.getTag() != null) {
                maker.setItem(40, new InventoryMaker.ClickableItem() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        player.closeInventory();
                        profile.setTag(null);
                        profile.updateProfile();
                        profile.save();

                        player.sendMessage(new MessageUtil().setVariable("{0}", target.getName()).format(AxisConfiguration.getString("tag" + (profile.getPlayerName() != null && profile.getPlayerName().equals(sender.getName()) ? "" : "_target") + "_removed")));
                    }

                    @Override
                    public ItemStack getItemStack() {
                        String lore = (profile.getPlayerName() != null && profile.getPlayerName().equals(sender.getName()) ? "Click here to remove your current tag" : "Click here to remove " + target.getName() + "'s prefix");
                        return new ItemMaker(Material.REDSTONE).setDisplayname("&c&lRemove Title").addLore("&c" + lore).create();
                    }
                });
            }

            player.openInventory(maker.getCurrentPage());*/
        }
        return true;
    }
}