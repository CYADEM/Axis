package me.tinchx.axis.commands.staff.punishment;

import me.tinchx.axis.punishment.Punishment;
import me.tinchx.axis.punishment.PunishmentHelper;
import me.tinchx.axis.punishment.PunishmentType;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KickCommand extends AxisCommand {

    public KickCommand() {
        super("kick");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName> <reason> [-p]"));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(target)) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }
            StringBuilder reason = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]).append(' ');
            }
            Punishment punishment = new Punishment();
            boolean isPublic = reason.toString().contains(" -p");
            if (isPublic) {
                punishment.setSilent(false);
            }

            punishment.setUuid(UUID.randomUUID());
            punishment.setType(PunishmentType.KICK);
            punishment.setTargetID(target.getUniqueId());
            punishment.setAddedBy((sender instanceof Player ? ((Player) sender).getUniqueId() : null));
            punishment.setAddedReason(reason.toString().replace(" -p", ""));
            punishment.setAddedAt(System.currentTimeMillis());
            PunishmentHelper.save(punishment);

            target.kickPlayer(new MessageUtil().setVariable("{reason}", reason.toString()).format(PunishmentType.KICK.getMessage()));

            SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "punishment;" + punishment.getUuid().toString() + ';' + sender.getName() + ';' + target.getName());
        }
        return true;
    }
}