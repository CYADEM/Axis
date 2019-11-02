package me.tinchx.axis.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PunishmentType {

    BLACKLIST("blacklisted", "&cYour account has been blacklisted.\nThis type of punishment cannot be appealed.", "unblacklisted"), BAN("banned", "&cYour account has been suspended.", "unbanned"), TEMPBAN("temporarily banned", "&cYour account has been temporarily suspended.\nExpires in {expires}.", "unbanned"), KICK("kicked", "&cYour account has been kicked.\nReason {reason}", null), MUTE("muted", "&cYou are currently muted for {duration}", "unmuted"), WARN("warned", "&cYou have been warned by {sender} for {reason}.", null);

    private String context, message, undoContext;
}