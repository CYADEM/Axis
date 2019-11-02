package me.tinchx.axis.listeners;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.AxisPlugin;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class CoreListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        Profile profile = AxisAPI.getProfile(player);
        profile.setPlayerName(player.getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(PlayerPreLoginEvent event) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getUniqueId());
        if (player.isWhitelisted() || player.isOp()) {
            return;
        }
        if (Bukkit.hasWhitelist()) {
            event.disallow(PlayerPreLoginEvent.Result.KICK_WHITELIST, new MessageUtil().setVariable("{0}", "\n").format(AxisConfiguration.getString("whitelist_kick")));
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!AxisConfiguration.getBoolean("do_mob_spawn")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (!AxisConfiguration.getBoolean("do_leaves_decay")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!AxisConfiguration.getBoolean("do_weather_change")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        if (!AxisConfiguration.getBoolean("do_thunder_change")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if (!AxisConfiguration.getBoolean("do_hanging_break")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        if (!AxisConfiguration.getBoolean("do_hanging_break")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (!AxisConfiguration.getBoolean("do_block_burn")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if (!AxisConfiguration.getBoolean("do_block_spread")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!AxisConfiguration.getBoolean("do_entity_explode")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (!AxisConfiguration.getBoolean("do_world_load_clean")) {
            return;
        }

        event.getWorld().setGameRuleValue("doDaylightCycle", "false");
        event.getWorld().setTime(12000L);
        event.getWorld().setThundering(false);

        if (!AxisConfiguration.getBoolean("do_world_load_remove_entities")) {
            return;
        }

        int i = 0;

        for (Entity entity : event.getWorld().getEntities()) {
            entity.remove();
            i++;
        }

        AxisPlugin.getPlugin().getLogger().info("Cleared " + i + " entities from " + event.getWorld().getName() + " on world load.");
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().isOp()) {
            return;
        }
        for (String message : AxisConfiguration.getArray("disallowed_commands")) {
            if (event.getMessage().startsWith(message)) {
                event.setCancelled(true);
                AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(ColorText.translate("&6[ALERT] " + event.getPlayer().getDisplayName() + " &eissued command: &f" + event.getMessage())));
            }
        }
    }
}