package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlightCommand extends AxisCommand {

    public FlightCommand() {
        super("flight", null, "fly");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length < 1) {
            if (sender instanceof Player) {
                target = (Player) sender;
                boolean toggle = !target.getAllowFlight();

                target.setAllowFlight(toggle);
                target.setFlying(toggle);
                target.sendMessage(ColorText.translate("&eYour flight mode has been " + (toggle ? "&aenabled" : "&cdisabled") + "&e."));
            } else {
                sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
            }
        } else {
            target = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(target)) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }
            boolean toggle = !target.getAllowFlight();
            target.setAllowFlight(toggle);
            target.setFlying(toggle);
            Command.broadcastCommandMessage(sender, ColorText.translate("&eFlight mode of " + target.getDisplayName() + " &ehas been " + (toggle ? "&aenabled" : "&cdisabled") + "&e."));
        }
        return true;
    }
}