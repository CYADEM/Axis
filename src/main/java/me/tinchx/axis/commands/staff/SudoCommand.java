package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand extends AxisCommand {

    public SudoCommand() {
        super("sudo");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName> <message...>"));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(target)) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }

            StringBuilder message = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(' ');
            }

            boolean isCommand = message.toString().startsWith("/"), isOperator = target.isOp();

            target.setOp(true);
            target.chat(message.toString());

            target.setOp(isOperator);
            Command.broadcastCommandMessage(sender, ColorText.translate(target.getDisplayName() + " &6has been forced to " + (isCommand ? "execute" : "say") + ": &f" + message.toString()));
        }
        return true;
    }
}