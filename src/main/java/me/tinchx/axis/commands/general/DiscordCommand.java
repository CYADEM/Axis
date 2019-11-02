package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DiscordCommand extends AxisCommand {

    public DiscordCommand() {
        super("discord", null, "dc");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(new MessageUtil().setVariable("{discord}", AxisConfiguration.getString("discord")).format(AxisConfiguration.getString("lang_discord")));
        return true;
    }
}