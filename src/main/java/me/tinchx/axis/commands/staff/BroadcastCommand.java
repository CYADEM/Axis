package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BroadcastCommand extends AxisCommand {

    public BroadcastCommand() {
        super("broadcast", null, "bc");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <message...>"));
        } else {
            Bukkit.broadcastMessage(ColorText.translate(StringUtils.join(args, " ")));
        }
        return true;
    }
}