package me.tinchx.axis.scoreboard.listeners;

import me.tinchx.axis.AxisPlugin;
import me.tinchx.axis.scoreboard.Aridi;
import me.tinchx.axis.scoreboard.AridiManager;
import me.tinchx.axis.scoreboard.events.PlayerLoadScoreboardEvent;
import me.tinchx.axis.scoreboard.events.PlayerUnloadScoreboardEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AridiListener implements Listener {

    private AxisPlugin plugin = AxisPlugin.getPlugin();
    private AridiManager aridiManager = plugin.getAridiManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (aridiManager != null) {
            Aridi aridi = new Aridi(player, aridiManager);
            aridiManager.getAridiMap().put(player.getUniqueId(), aridi);
            new PlayerLoadScoreboardEvent(player, aridi).call();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (aridiManager != null && aridiManager.getAridiMap().containsKey(player.getUniqueId())) {
            new PlayerUnloadScoreboardEvent(player, aridiManager.getAridiMap().get(player.getUniqueId())).call();
            aridiManager.getAridiMap().remove(player.getUniqueId());
        }
    }
}