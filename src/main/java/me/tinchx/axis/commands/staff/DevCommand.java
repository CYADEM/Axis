package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DevCommand extends AxisCommand {

    public DevCommand() {
        super("dev");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
            return false;
        }
        for (String arg : args) {
            for (String message : arg.split("\n")) {
                message = message.replace("\n", "");
                sender.sendMessage(ColorText.translate(message));
            }
        }
        return true;
    }
}