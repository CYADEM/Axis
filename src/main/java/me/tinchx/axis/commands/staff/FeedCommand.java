package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand extends AxisCommand {

    public FeedCommand() {
        super("feed", null, "eat");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length < 1) {
            if (sender instanceof Player) {
                if ((target = (Player) sender).getFoodLevel() >= 20) {
                    sender.sendMessage(ColorText.translate("&c" + target.getName() + " has already full hunger. (" + target.getMaxHealth() + ')'));
                    return false;
                }
                target.setFoodLevel(20);
                Command.broadcastCommandMessage(sender, ColorText.translate("&eFed!"));
            } else {
                sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
            }
        } else {
            if (args[0].equalsIgnoreCase("all")) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    online.setFoodLevel(20);
                }
                Command.broadcastCommandMessage(sender, ColorText.translate("&eAll players have been fed!"));
            } else if (!AxisUtils.isOnline((target = Bukkit.getPlayer(args[0])))) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
            } else {
                if (target.getFoodLevel() >= 20) {
                    sender.sendMessage(ColorText.translate("&c" + target.getName() + " has already full hunger. (" + target.getMaxHealth() + ')'));
                    return false;
                }
                target.setFoodLevel(20);
                Command.broadcastCommandMessage(sender, ColorText.translate("&e" + target.getName() + " Fed!"));
            }
        }
        return true;
    }
}