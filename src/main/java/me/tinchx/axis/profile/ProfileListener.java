package me.tinchx.axis.profile;

import lombok.Getter;
import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.AxisPlugin;
import me.tinchx.axis.listeners.events.PlayerFirstJoinEvent;
import me.tinchx.axis.listeners.events.PlayerRankChangedEvent;
import me.tinchx.axis.punishment.Punishment;
import me.tinchx.axis.punishment.PunishmentType;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.task.TaskUtil;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.UUID;


@Getter
public class ProfileListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Profile profile = new Profile(event.getUniqueId());
        Punishment punishment = profile.getBannedPunishment();
        profile.setAddress(event.getAddress().getHostAddress());
        profile.setPlayerName(event.getName());
        profile.findAlts();
        profile.save();

        if (!AxisPlugin.getPlugin().isLoaded()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, AxisConfiguration.getString("loading_data"));
        }

        for (UUID uuid : profile.getAlts()) {
            if (!uuid.equals(event.getUniqueId())) {
                Profile altProfile = AxisAPI.getProfile(uuid);
                Punishment bannedPunishment = altProfile.getBannedPunishment();

                if (bannedPunishment != null && bannedPunishment.getType() == PunishmentType.BLACKLIST) {
                    event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
                    event.setKickMessage(ColorText.translate(bannedPunishment.getType().getMessage()));
                }
            }
        }

        Player player = Bukkit.getPlayer(event.getUniqueId());

        if (AxisUtils.isOnline(player)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ColorText.translate("&cYou tried to login too quickly after disconnecting.\nTry again in a few seconds."));
            TaskUtil.runTask(() -> player.kickPlayer(ColorText.translate("&cYour account is trying to login with different ip")));
        }

        if (punishment != null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, new MessageUtil().setVariable("{expires}", punishment.getRemaining()).format(punishment.getType().getMessage()));
        }

        if (event.getResult() == PlayerPreLoginEvent.Result.KICK_OTHER) {
            profile.removeFromMap();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Profile profile = AxisAPI.getProfile(player);
        profile.setLastServer(Bukkit.getServerName());
        profile.updateProfile();

        if (!player.hasPermission(AxisUtils.STAFF_PERMISSION)) {
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (AxisAPI.getProfile(online).isVanished()) {
                    player.hidePlayer(online);
                }
            }
        }

        if (!player.hasPlayedBefore()) {
            new PlayerFirstJoinEvent(player).call();
        }
    }

    @EventHandler
    public void onPlayerFirstJoin(PlayerFirstJoinEvent event) {
        AxisAPI.getProfile(event.getPlayer()).setFirstJoin(System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Profile profile = AxisAPI.getProfile(event.getPlayer());

        if (profile.isFrozen()) {
            SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "freeze;alert;" + Bukkit.getServerName() + ';' + profile.getRank().getColor() + profile.getPlayerName());
        }

        if (AxisConfiguration.getBoolean("staff_mode") && profile.isStaffMode()) {
            profile.setStaffMode(false);
            profile.rollbackInventory();
        }

        profile.setLastJoin(System.currentTimeMillis());

        profile.save();
        profile.removeFromMap();
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().equalsIgnoreCase("/gmc")) {
            event.setCancelled(true);
            player.performCommand("gm c");
        } else if (event.getMessage().equalsIgnoreCase("/gms")) {
            event.setCancelled(true);
            player.performCommand("gm s");
        } else if (event.getMessage().equalsIgnoreCase("/gma")) {
            event.setCancelled(true);
            player.performCommand("gm a");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        Player player = event.getPlayer();
        Profile profile = AxisAPI.getProfile(player);
        if (profile.isFrozen()) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (AxisAPI.getProfile((Player) event.getDamager()).isFrozen()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            if (AxisAPI.getProfile((Player) event.getEntity()).isFrozen()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (AxisAPI.getProfile((Player) event.getEntity()).isFrozen()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (AxisAPI.getProfile(event.getPlayer()).isFrozen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (AxisAPI.getProfile(event.getPlayer()).isFrozen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        if (AxisAPI.getProfile(event.getPlayer()).isFrozen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (AxisAPI.getProfile(event.getPlayer()).isFrozen() && event.getCause() != PlayerTeleportEvent.TeleportCause.PLUGIN) {
            event.setCancelled(true);
            return;
        }
        Profile profile = AxisAPI.getProfile(event.getPlayer());
        if (event.getTo() != null) {
            profile.setBackLocation(event.getTo());
        }
    }

    @EventHandler
    public void onPlayerItemPickup(PlayerPickupItemEvent event) {
        if (AxisAPI.getProfile(event.getPlayer()).isFrozen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Profile profile = AxisAPI.getProfile(event.getEntity());
        profile.setBackLocation(event.getEntity().getLocation());
    }

    @EventHandler
    public void onPlayerRankChanged(PlayerRankChangedEvent event) {
        Player player = event.getPlayer();
        Rank rank = event.getRank();

        if (!event.isCancelled()) {
            player.sendMessage(new MessageUtil().setVariable("{0}", rank.getColor() + rank.getName()).format(AxisConfiguration.getString("rank_updated_message")));
        }
    }

}