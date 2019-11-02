package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RealNameCommand extends AxisCommand {

    public RealNameCommand() {
        super("realname");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <nickName>"));
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AxisAPI.getProfile(player).getNickName() == null || !AxisAPI.getProfile(player).getNickName().equalsIgnoreCase(args[0])) {
                    continue;
                }
                sender.sendMessage(ColorText.translate("&e" + args[0] + " is known as " + AxisAPI.getProfile(player).getRank().getColor() + player.getName() + "&e!"));
            }
        }
        return true;
    }
}