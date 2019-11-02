package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand extends AxisCommand {

    public HealCommand() {
        super("heal");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length < 1) {
            if (sender instanceof Player) {
                if((target = (Player) sender).getHealth() >= target.getMaxHealth()) {
                    sender.sendMessage(ColorText.translate("&c" + target.getName() + " has already full heart. (" + target.getMaxHealth() + ')'));
                    return false;
                }
                target.setHealth(target.getMaxHealth());
                target.setFoodLevel(20);
                target.getActivePotionEffects().forEach(potionEffect -> target.removePotionEffect(potionEffect.getType()));
                target.setFireTicks(0);
                Command.broadcastCommandMessage(sender, ColorText.translate("&eHealed!"));
            } else {
                sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
            }
        } else {
            if (args[0].equalsIgnoreCase("all")) {
                for(Player online : Bukkit.getServer().getOnlinePlayers()) {
                    online.setHealth(online.getMaxHealth());
                    online.setFoodLevel(20);
                    online.setFireTicks(0);
                }
                Command.broadcastCommandMessage(sender, ColorText.translate("&eAll players have been healed!"));
            } else if (!AxisUtils.isOnline((target = Bukkit.getPlayer(args[0])))) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
            } else {
                if(target.getHealth() >= target.getMaxHealth()) {
                    sender.sendMessage(ColorText.translate("&c" + target.getName() + " has already full heart. (" + target.getMaxHealth() + ')'));
                    return false;
                }
                target.setHealth(target.getMaxHealth());
                target.setFoodLevel(20);
                target.getActivePotionEffects().forEach(potionEffect -> target.removePotionEffect(potionEffect.getType()));
                target.setFireTicks(0);
                Command.broadcastCommandMessage(sender, ColorText.translate("&e" + target.getName() + " Healed!"));
            }
        }
        return true;
    }
}