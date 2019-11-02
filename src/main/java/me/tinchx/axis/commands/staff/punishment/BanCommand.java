package me.tinchx.axis.commands.staff.punishment;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.punishment.Punishment;
import me.tinchx.axis.punishment.PunishmentHelper;
import me.tinchx.axis.punishment.PunishmentType;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BanCommand extends AxisCommand {

    public BanCommand() {
        super("ban");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName> <reason> [-p]"));
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                sender.sendMessage(ColorText.translate("&c" + args[0] + " has never played before."));
                return false;
            }
            Profile profile = AxisAPI.getProfile(target.getUniqueId());
            Punishment punishment = profile.getBannedPunishment();
            if (punishment != null) {
                sender.sendMessage(ColorText.translate("&c" + target.getName() + " is already banned."));
            } else {
                punishment = new Punishment();
                StringBuilder reason = new StringBuilder();

                for (int i = 1; i < args.length; i++) {
                    reason.append(args[i]).append(' ');
                }

                boolean isPublic = reason.toString().contains(" -p");
                if (isPublic) {
                    punishment.setSilent(false);
                }

                punishment.setUuid(UUID.randomUUID());
                punishment.setType(PunishmentType.BAN);
                punishment.setTargetID(target.getUniqueId());
                punishment.setAddedBy((sender instanceof Player ? ((Player) sender).getUniqueId() : null));
                punishment.setAddedReason(reason.toString().replace(" -p", ""));
                punishment.setAddedAt(System.currentTimeMillis());
                PunishmentHelper.save(punishment);

                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "punishment;" + punishment.getUuid().toString() + ';' + sender.getName() + ';' + target.getName());
            }

        }
        return true;
    }
}