package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TeamSpeakCommand extends AxisCommand {

    public TeamSpeakCommand() {
        super("teamspeak", null, "ts", "ts3", "teamspeak3");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(new MessageUtil().setVariable("{ts}", AxisConfiguration.getString("teamspeak")).format(AxisConfiguration.getString("lang_teamspeak")));
        return true;
    }
}