package me.tinchx.axis.commands.staff.punishment;

import me.tinchx.axis.punishment.Punishment;
import me.tinchx.axis.punishment.PunishmentHelper;
import me.tinchx.axis.punishment.PunishmentType;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.axis.utilities.time.TimeUtils;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WarnCommand extends AxisCommand {

    public WarnCommand() {
        super("warn");
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
            Punishment punishment = new Punishment();
            StringBuilder reason = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]).append(' ');
            }

            boolean isPublic = reason.toString().endsWith(" -p");
            if (isPublic) {
                punishment.setSilent(false);
            }

            punishment.setUuid(UUID.randomUUID());
            punishment.setType(PunishmentType.WARN);
            punishment.setTargetID(target.getUniqueId());
            punishment.setAddedBy((sender instanceof Player ? ((Player) sender).getUniqueId() : null));
            punishment.setAddedReason(reason.toString().replace(" -p", ""));
            punishment.setAddedAt(System.currentTimeMillis());
            punishment.setExpiration(TimeUtils.parse("7d"));
            PunishmentHelper.save(punishment);

            if (AxisUtils.isOnline(target)) {
                target.getPlayer().sendMessage(new MessageUtil().setVariable("{sender}", sender.getName()).setVariable("{reason}", reason.toString()).format(PunishmentType.WARN.getMessage()));
                target.getPlayer().sendMessage(ColorText.translate("&cIt expires in &e7 days&c!"));
            }

            SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "punishment;" + punishment.getUuid().toString() + ';' + sender.getName() + ';' + target.getName());

        }
        return true;
    }
}