package me.tinchx.axis.commands.staff.chat.arguments;

import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.chat.ChatManager;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ChatToggleArgument extends AxisArgument {

    public ChatToggleArgument() {
        super("toggle", "Toggle chat");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name;
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        ChatManager.setPublicChatAllowed(!ChatManager.isPublicChatAllowed());
        Bukkit.broadcastMessage(new MessageUtil().setVariable("{sender}", sender.getName()).format((ChatManager.isPublicChatAllowed() ? AxisConfiguration.getString("unmute_chat") : AxisConfiguration.getString("mute_chat"))));
    }
}