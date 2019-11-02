package me.tinchx.axis.listeners;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.inventory.InventoryMaker;
import me.tinchx.axis.profile.ChatType;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.item.ItemMaker;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StaffListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Rank rank = AxisAPI.getProfile(player).getRank();
        if (player.hasPermission(AxisUtils.STAFF_PERMISSION)) {
            SponsorPlugin.getInstance().getRedisPublisher().write("staffjoin;" + (AxisConfiguration.getBoolean("ranks") ? rank.getColor() + player.getName() : player.getDisplayName()) + ';' + Bukkit.getServerName());
            //axis.getRedisPublisher().write("staffjoin;" + (AxisConfiguration.getBoolean("ranks") ? rank.getColor() + player.getName() : player.getDisplayName()) + ';' + Bukkit.getServerName());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Rank rank = AxisAPI.getProfile(player).getRank();
        if (player.hasPermission(AxisUtils.STAFF_PERMISSION)) {
            SponsorPlugin.getInstance().getRedisPublisher().write("staffleave;" + (AxisConfiguration.getBoolean("ranks") ? rank.getColor() + player.getName() : player.getDisplayName()) + ';' + Bukkit.getServerName());
            //axis.getRedisPublisher().write("staffleave;" + (AxisConfiguration.getBoolean("ranks") ? rank.getColor() + player.getName() : player.getDisplayName()) + ';' + Bukkit.getServerName());
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = AxisAPI.getProfile(player);
        if (profile.getChatType() == ChatType.FROZEN || profile.isFrozen()) {

            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (AxisAPI.getProfile(online).getChatType() == ChatType.FROZEN || AxisAPI.getProfile(online).isFrozen() || online.hasPermission(AxisUtils.STAFF_PERMISSION)) {
                    online.sendMessage(ColorText.translate("&c[Frozen] " + player.getDisplayName() + "&7: &f" + event.getMessage()));
                }
            }
        } else if (profile.getChatType() == ChatType.STAFF && player.hasPermission(AxisUtils.STAFF_PERMISSION)) {
            //axis.getRedisPublisher().write("staffchat;" + Bukkit.getServerName() + ';' + player.getName() + ';' + event.getMessage());
            SponsorPlugin.getInstance().getRedisPublisher().write("staffchat;" + Bukkit.getServerName() + ';' + event.getMessage());
        } else if (profile.getChatType() == ChatType.ADMIN && player.hasPermission(AxisUtils.STAFF_PERMISSION + ".admin")) {
            SponsorPlugin.getInstance().getRedisPublisher().write("adminchat;" + Bukkit.getServerName() + ';' + event.getMessage());
        } else if (profile.getChatType() == ChatType.OWNER && player.isOp()) {
            SponsorPlugin.getInstance().getRedisPublisher().write("ownerchat;" + Bukkit.getServerName() + ';' + event.getMessage());
        } else {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (AxisAPI.getProfile(event.getPlayer()).isStaffMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.isOp() && player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (AxisAPI.getProfile(event.getPlayer()).isStaffMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.isOp() && player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (AxisAPI.getProfile(event.getPlayer()).isStaffMode()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (AxisAPI.getProfile((Player) event.getEntity()).isStaffMode()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (AxisAPI.getProfile((Player) event.getDamager()).isStaffMode()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player player = event.getPlayer();
            Profile profile = AxisAPI.getProfile(player);
            if (profile.isStaffMode()) {
                if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
                    return;
                }
                if (player.getItemInHand().getType() == Material.BLAZE_ROD) {
                    player.performCommand("ss " + event.getRightClicked().getName());
                } else if (player.getItemInHand().getType() == Material.BOOK) {
                    player.performCommand("invsee " + event.getRightClicked().getName());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = AxisAPI.getProfile(player);

        if (profile.isVanished()) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();

                if (block.getState() instanceof ContainerBlock) {
                    event.setCancelled(true);
                    ContainerBlock containerBlock = (ContainerBlock) block.getState();
                    Inventory inventory = Bukkit.createInventory(null, containerBlock.getInventory().getSize(), "[F] Chest");

                    for (int i = 0; i < containerBlock.getInventory().getSize(); i++) {
                        inventory.setItem(i, containerBlock.getInventory().getItem(i));
                    }

                    player.openInventory(inventory);
                    player.sendMessage(ColorText.translate("&6Displaying duplicate of &f" + new MessageUtil().setVariable("_", "").format(block.getType().name().toLowerCase()) + " &6inventory."));
                }
            }
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;
        if (!profile.isStaffMode()) return;

        if (event.getAction().name().startsWith("RIGHT")) {
            if (item.getType() == Material.INK_SACK) {
                player.performCommand("vanish");
            } else if (item.getType() == Material.SKULL_ITEM) {
                InventoryMaker inventoryMaker = new InventoryMaker("&d&lOnline Staff &7(" + AxisUtils.getOnlineStaff().size() + ')', 1);

                AxisUtils.getOnlineStaff().forEach(staff -> inventoryMaker.addItem(new InventoryMaker.ClickableItem() {
                    @Override
                    public void onClick(InventoryClickEvent event) {
                        player.performCommand("tp " + staff.getName());
                    }

                    @Override
                    public ItemStack getItemStack() {
                        return new ItemMaker(Material.SKULL_ITEM).setDurability(3).setDisplayname(staff.getDisplayName()).addLore("&7&m--------------", "&6Operator: " + (staff.isOp() ? "&aYes" : "&cNo"), "&6Current Rank: " + AxisAPI.getRank(staff).getColor() + AxisAPI.getRank(staff).getName(), "", "&7&oClick to teleport", "&7&m--------------").create();
                    }
                }));

                player.openInventory(inventoryMaker.getCurrentPage());
            } else if (item.getType() == Material.DIAMOND_PICKAXE) {
                InventoryMaker inventoryMaker = new InventoryMaker("&b&lX-Ray Finder", 1);

                Bukkit.getOnlinePlayers().forEach(o -> {
                    if (30 >= o.getLocation().getBlockY()) {
                        inventoryMaker.addItem(new InventoryMaker.ClickableItem() {
                            @Override
                            public void onClick(InventoryClickEvent event) {
                                player.performCommand("tp " + o.getName());
                            }

                            @Override
                            public ItemStack getItemStack() {
                                return new ItemMaker(Material.SKULL_ITEM).setDisplayname(o.getDisplayName()).setDurability(3).addLore("&7&m--------------", "&6Current Rank: " + AxisAPI.getRank(o).getColor() + AxisAPI.getRank(o).getName(), "&6Location: &f" + o.getLocation().getBlockX() + ", " + o.getLocation().getBlockZ(), "", "&7&oClick to teleport", "&7&m--------------").create();
                            }
                        });
                    }
                });

                player.openInventory(inventoryMaker.getCurrentPage());
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (AxisAPI.getProfile((Player) event.getEntity()).isStaffMode()) {
            event.setCancelled(true);
            ((Player) event.getEntity()).setFoodLevel(20);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("[F] Chest")) {
            event.setCancelled(true);
        }
    }

}