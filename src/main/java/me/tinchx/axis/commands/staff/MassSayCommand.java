package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MassSayCommand extends AxisCommand {

    public MassSayCommand() {
        super("masssay");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <message...>"));
        } else {
            StringBuilder message = new StringBuilder();
            for (String arg : args) {
                message.append(arg).append(' ');
            }

            boolean isCommand = message.toString().startsWith("/");


            for (Player online : Bukkit.getServer().getOnlinePlayers()) {

                boolean wasOp = online.isOp();

                online.setOp(true);

                online.chat(message.toString());

                online.setOp(wasOp);
            }

            Command.broadcastCommandMessage(sender, ColorText.translate("&6All players have been forced to " + (isCommand ? "execute" : "say") + ": &f" + message.toString()));
        }
        return true;
    }
}