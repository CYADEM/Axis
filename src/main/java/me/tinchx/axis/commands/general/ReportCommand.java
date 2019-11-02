package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.cooldown.Cooldown;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.sponsor.SponsorPlugin;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand extends AxisCommand {

    public ReportCommand() {
        super("report", null, "hackertp");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName> <reason...>"));
        } else {
            Player player = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(player) || (sender instanceof Player && !((Player) sender).canSee(player))) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }
            StringBuilder reason = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]).append(' ');
            }

            if (player.equals(sender)) {
                sender.sendMessage(ColorText.translate("&cYou may not report yourself!"));
                return false;
            }

            if (sender instanceof Player) {
                Cooldown cooldown = AxisAPI.getCooldown("report");

                if (cooldown != null) {
                    if (cooldown.isOnCooldown((Player) sender)) {
                        sender.sendMessage(ColorText.translate("&cYou are on cooldown for another &l" + DurationFormatUtils.formatDurationWords(cooldown.getDuration((Player) sender), true, true) + "&c."));
                        return false;
                    }

                    cooldown.setCooldown(((Player) sender));
                }
            }

            String senderName = (sender instanceof Player ? AxisAPI.getRank((Player) sender).getColor() + sender.getName() : "&fConsole");
            SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "report;" + senderName + ';' + Bukkit.getServerName() + ';' + AxisAPI.getRank(player).getName() + player.getName() + ';' + reason.toString());
            sender.sendMessage(AxisConfiguration.getString("report_sent"));
        }
        return true;
    }
}