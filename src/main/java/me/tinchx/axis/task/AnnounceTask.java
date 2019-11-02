package me.tinchx.axis.task;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class AnnounceTask {

    private List<String> MESSAGES = AxisConfiguration.getArray("announce_messages");

    public AnnounceTask() {
        TaskUtil.runTaskTimer(() -> {
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (!AxisAPI.getProfile(online).isTipsEnabled()) {
                    continue;
                }

                online.sendMessage(new MessageUtil().setVariable("{0}", "\n").setVariable("%playername%", online.getName()).format(MESSAGES.get(AxisUtils.getRandomNumber(MESSAGES.size()))));
            }
        }, 20L, AxisConfiguration.getInteger("announce_delay") * 20L);
    }
}