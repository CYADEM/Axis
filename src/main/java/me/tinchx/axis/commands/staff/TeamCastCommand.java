package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TeamCastCommand extends AxisCommand {

    public TeamCastCommand() {
        super("teamcast", null, "tc");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /*try {
            AxisPlugin.getPlugin().getTs3Api().broadcast(StringUtils.join(args, " "));
        } catch (Exception ex) {
            sender.sendMessage(ColorText.translate("&cYour message could not be sent."));
        }*/
        return true;
    }
}