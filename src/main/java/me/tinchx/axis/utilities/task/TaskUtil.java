package me.tinchx.axis.utilities.task;

import me.tinchx.axis.AxisPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void runTaskAsync(Runnable runnable) {
        AxisPlugin.getPlugin().getServer().getScheduler().runTaskAsynchronously(AxisPlugin.getPlugin(), runnable);
    }

    public static void runTaskLater(Runnable runnable, long delay) {
        AxisPlugin.getPlugin().getServer().getScheduler().runTaskLater(AxisPlugin.getPlugin(), runnable, delay);
    }

    public static void runTaskTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(AxisPlugin.getPlugin(), delay, timer);
    }

    public static void runTaskTimer(Runnable runnable, long delay, long timer) {
        AxisPlugin.getPlugin().getServer().getScheduler().runTaskTimer(AxisPlugin.getPlugin(), runnable, delay, timer);
    }

    public static void runTask(Runnable runnable) {
        AxisPlugin.getPlugin().getServer().getScheduler().runTask(AxisPlugin.getPlugin(), runnable);
    }
}