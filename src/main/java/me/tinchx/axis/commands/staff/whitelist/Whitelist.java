package me.tinchx.axis.commands.staff.whitelist;

import lombok.Getter;
import lombok.Setter;
import me.tinchx.axis.AxisPlugin;
import me.tinchx.axis.utilities.task.TaskUtil;
import me.tinchx.axis.utilities.time.TimeUtils;
import me.tinchx.sponsor.SponsorPlugin;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Whitelist {

    private static List<Long> delays = Arrays.asList(TimeUtils.parse("1h"), TimeUtils.parse("45m"), TimeUtils.parse("30m"), TimeUtils.parse("25m"), TimeUtils.parse("20m"), TimeUtils.parse("15m"), TimeUtils.parse("12m"), TimeUtils.parse("10m"), TimeUtils.parse("9m"), TimeUtils.parse("8m"), TimeUtils.parse("7m"), TimeUtils.parse("6m"), TimeUtils.parse("5m30s"), TimeUtils.parse("5m"), TimeUtils.parse("4m30s"), TimeUtils.parse("4m"), TimeUtils.parse("3m30s"), TimeUtils.parse("3m"), TimeUtils.parse("2m30s"), TimeUtils.parse("2m"), TimeUtils.parse("1m30s"), TimeUtils.parse("1m"), TimeUtils.parse("45s"), TimeUtils.parse("30s"), TimeUtils.parse("25s"), TimeUtils.parse("20s"), TimeUtils.parse("15s"), TimeUtils.parse("10s"), TimeUtils.parse("5s"), TimeUtils.parse("4s"), TimeUtils.parse("3s"), TimeUtils.parse("2s"), TimeUtils.parse("1s"));
    private static Map<String, Whitelist> whitelists = new HashMap<>();
    private String serverName;
    @Setter
    private long createdAt, duration;

    public Whitelist(String serverName, long duration) {
        this.serverName = serverName;
        this.createdAt = System.currentTimeMillis();
        this.duration = duration;

        whitelists.put(serverName, this);
    }

    private long getDurationLeft() {
        return (createdAt + duration) - System.currentTimeMillis();
    }


    public static void run(AxisPlugin axis) {
        TaskUtil.runTaskTimer(() -> {
            for (Whitelist list : whitelists.values()) {
                long remaining = list.getDurationLeft();
                remaining -= remaining % 1000;

                if (remaining <= 0L) {
                    Bukkit.setWhitelist(false);
                    SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "whitelist;unwhitelisted;" + list.getServerName());
                    list.remove();
                    /*if (AxisConfiguration.getBoolean("teamspeak_api") && AxisPlugin.getPlugin().getTs3Api() != null) {
                        AxisPlugin.getPlugin().getTs3Api().broadcast(ChatColor.stripColor("Hey! " + list.getServerName() + " has been un-whitelisted. Come and enjoy together us!\nServer IP: " + AxisConfiguration.getString("address") + " (1.7x & 1.8x)"));
                    }*/
                    continue;
                }

                if (delays.contains(remaining)) {
                    SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "whitelist;running;" + list.getServerName() + ';' + DurationFormatUtils.formatDurationWords(remaining, true, true));
                }
            }
        }, 0L, 20L);
    }

    public void remove() {
        whitelists.remove(serverName);
    }

    public static Whitelist getByServer(String serverName) {
        for (Whitelist whitelist : whitelists.values()) {
            if (whitelist.serverName.equalsIgnoreCase(serverName.replace(" ", ""))) {
                return whitelist;
            }
        }
        return null;
    }
}