package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.sponsor.SponsorPlugin;
import me.tinchx.axis.cooldown.Cooldown;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RequestCommand extends AxisCommand {

    public RequestCommand() {
        super("request", null, "helpop", "ac");
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

            if (sender instanceof Player) {
                Cooldown cooldown = AxisAPI.getCooldown("request");

                if (cooldown != null) {
                    if (cooldown.isOnCooldown((Player) sender)) {
                        sender.sendMessage(ColorText.translate("&cYou are on cooldown for another &l" + DurationFormatUtils.formatDurationWords(cooldown.getDuration((Player) sender), true, true) + "&c."));
                        return false;
                    }

                    cooldown.setCooldown(((Player) sender));
                }
            }
            String senderName = (sender instanceof Player ? AxisAPI.getRank((Player) sender).getColor() + sender.getName() : "&fConsole");
            //axis.getRedisPublisher().write("request;" + senderName + ';' + Bukkit.getServerName() + ';' + message);
            SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "request;" + senderName + ';' + Bukkit.getServerName() + ';' + message);
            sender.sendMessage(AxisConfiguration.getString("request_sent"));
        }
        return true;
    }
}