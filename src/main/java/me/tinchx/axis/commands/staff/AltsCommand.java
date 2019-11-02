package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class AltsCommand extends AxisCommand {

    public AltsCommand() {
        super("alts");
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

            profile.findAlts();

            if (profile.getAlts().size() < 1) {
                sender.sendMessage(ColorText.translate("&c" + target.getName() + " doesn't have any alts."));
            } else {
                sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 45)));
                sender.sendMessage(ColorText.translate(profile.getAlts().size() + " &6" + (profile.getAlts().size() == 1 ? "alt was" : "alts were") + " successfully found!"));
                sender.sendMessage("");
                for(UUID uuid : profile.getAlts()) {
                    Profile altProfile = AxisAPI.getProfile(uuid);
                    sender.sendMessage(ColorText.translate(" &7- &e" + altProfile.getPlayerName() + " &7(" + (altProfile.getBannedPunishment() == null ? "&eUnbanned" : "&cBanned") + "&7)"));
                }
                sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 45)));
            }
        }
        return true;
    }
}