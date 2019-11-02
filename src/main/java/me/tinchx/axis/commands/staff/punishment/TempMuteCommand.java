package me.tinchx.axis.commands.staff.punishment;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.punishment.Punishment;
import me.tinchx.axis.punishment.PunishmentHelper;
import me.tinchx.axis.punishment.PunishmentType;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.axis.utilities.time.TimeUtils;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TempMuteCommand extends AxisCommand {

    public TempMuteCommand() {
        super("tempmute", null, "tmute");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName> <duration> <reason> [-p]"));
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                sender.sendMessage(ColorText.translate("&c" + args[0] + " has never played before."));
                return false;
            }
            long duration = TimeUtils.parse(args[1]);

            Profile profile = AxisAPI.getProfile(target.getUniqueId());

            if (profile.getMutedPunishment() != null) {
                sender.sendMessage(ColorText.translate("&c" + target.getName() + " is already muted."));
                return false;
            }

            if (duration <= 0L) {
                sender.sendMessage(ColorText.translate("&cIf you want to permanently mute a player, use /mute instead."));
            } else {
                StringBuilder reason = new StringBuilder();

                for (int i = 2; i < args.length; i++) {
                    reason.append(args[i]).append(' ');
                }

                Punishment punishment = new Punishment();

                boolean isPublic = reason.toString().contains(" -p");
                if (isPublic) {
                    punishment.setSilent(false);
                }

                punishment.setUuid(UUID.randomUUID());
                punishment.setType(PunishmentType.MUTE);
                punishment.setTargetID(target.getUniqueId());
                punishment.setAddedBy((sender instanceof Player ? ((Player) sender).getUniqueId() : null));
                punishment.setAddedReason(reason.toString().replace(" -p", ""));
                punishment.setAddedAt(System.currentTimeMillis());
                punishment.setExpiration(duration);
                PunishmentHelper.save(punishment);

                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "punishment;" + punishment.getUuid().toString() + ';' + sender.getName() + ';' + target.getName());
            }
        }
        return true;
    }
}