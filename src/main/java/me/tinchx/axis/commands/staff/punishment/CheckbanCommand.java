package me.tinchx.axis.commands.staff.punishment;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.punishment.Punishment;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CheckbanCommand extends AxisCommand {

    public CheckbanCommand() {
        super("checkban");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                sender.sendMessage(ColorText.translate("&c" + args[0] + " has never played before."));
                return false;
            }
            Profile profile = AxisAPI.getProfile(target.getUniqueId());
            Punishment punishment = profile.getBannedPunishment();

            if (punishment == null) {
                sender.sendMessage(ColorText.translate("&c" + target.getName() + " is not banned."));
            } else {
                sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 50)));
                sender.sendMessage(ColorText.translate("&eReason: &7" + punishment.getAddedReason()));
                sender.sendMessage(ColorText.translate("&eExpires in: &7" + punishment.getRemaining()));
                sender.sendMessage(ColorText.translate("&eBanned by: &7" + (punishment.getAddedBy() == null ? "Console" : Bukkit.getOfflinePlayer(punishment.getAddedBy()).getName())));
                sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 50)));
            }

        }
        return true;
    }
}