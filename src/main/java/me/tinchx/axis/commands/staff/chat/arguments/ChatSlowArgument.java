package me.tinchx.axis.commands.staff.chat.arguments;

import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.chat.ChatManager;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.axis.utilities.time.TimeUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ChatSlowArgument extends AxisArgument {

    public ChatSlowArgument() {
        super("slow", "Slow down the chat");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <time>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            if (ChatManager.getChatDelay() > 0L) {
                ChatManager.setChatDelay(0L);
                Bukkit.broadcastMessage(AxisConfiguration.getString("slow_chat_removed"));
            } else {
                ChatManager.setChatDelay(TimeUtils.parse("5s"));
                Bukkit.broadcastMessage(new MessageUtil().setVariable("{0}", "5 seconds").format(AxisConfiguration.getString("slow_chat_set")));
            }
        } else {
            long delay = TimeUtils.parse(args[1]);
            if (delay > 0L) {
                ChatManager.setChatDelay(delay);
                Bukkit.broadcastMessage(new MessageUtil().setVariable("{0}", DurationFormatUtils.formatDurationWords(delay, true, true)).format(AxisConfiguration.getString("slow_chat_set")));
            } else {
                ChatManager.setChatDelay(0L);
                Bukkit.broadcastMessage(AxisConfiguration.getString("slow_chat_removed"));
            }
        }
    }
}