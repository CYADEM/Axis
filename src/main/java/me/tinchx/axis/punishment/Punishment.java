package me.tinchx.axis.punishment;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Punishment {

    private UUID uuid, targetID, addedBy, removedBy;
    private String addedReason, removedReason;
    private PunishmentType type;
    private long addedAt, removedAt, expiration = -1;
    private boolean silent = true;

    public boolean isBan() {
        return type == PunishmentType.TEMPBAN || type == PunishmentType.BAN || type == PunishmentType.BLACKLIST;
    }

    public boolean isMute() {
        return type == PunishmentType.MUTE;
    }

    public boolean isRemoved() {
        return this.removedReason != null;
    }

    public boolean isPermanent() {
        return expiration == -1;
    }

    public boolean isActive() {
        if (isRemoved()) {
            return false;
        }
        if (isPermanent()) {
            return true;
        }
        return (addedAt + expiration) > System.currentTimeMillis();
    }

    public String getRemaining() {
        String toReturn;
        if (isRemoved()) {
            toReturn = "Removed";
        } else if (isPermanent()) {
            toReturn = "Permanent";
        } else if (!isActive()) {
            toReturn = "Expired";
        } else {
            toReturn = DurationFormatUtils.formatDurationWords((addedAt + expiration) - System.currentTimeMillis(), true, true);
        }

        return toReturn;
    }

    public void send(String target, String sender) {
        String message = new MessageUtil().setVariable("{target}", target).setVariable("{sender}", sender).setVariable("{context}", (this.removedReason == null ? type.getContext() : type.getUndoContext())).format(AxisConfiguration.getString("punishment_format"));

        if (isSilent()) {
            AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(ColorText.translate("&7[STAFF ONLY] " + message)));
            AxisAPI.sendToConsole(ColorText.translate("&7[STAFF ONLY] " + message));
        } else {
            Bukkit.broadcastMessage(message);
        }
    }
}