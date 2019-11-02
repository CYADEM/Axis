package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand extends AxisCommand {

    public PingCommand() {
        super("ping", null, "latency", "ms");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            if (sender instanceof Player) {
                ((Player) sender).performCommand("ping " + sender.getName());
            } else {
                sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(target)) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }
            sender.sendMessage(new MessageUtil().setVariable("{0}", target.getName()).setVariable("{1}", String.valueOf(AxisAPI.getPing(target))).format(AxisConfiguration.getString("player_ping")));
        }
        return true;
    }
}