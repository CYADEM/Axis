package me.tinchx.axis.listeners;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.chat.ChatManager;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.punishment.Punishment;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!ChatManager.isPublicChatAllowed() && !player.hasPermission(AxisUtils.STAFF_PERMISSION)) {
            event.setCancelled(true);
            player.sendMessage(ColorText.translate("&cPublic chat is currently muted."));
            return;
        }

        Profile profile = AxisAPI.getProfile(player);
        Punishment punishment = profile.getMutedPunishment();

        if (punishment != null) {
            event.setCancelled(true);
            if (punishment.isPermanent()) {
                player.sendMessage(ColorText.translate("&cYou are permanently muted."));
            } else {
                player.sendMessage(ColorText.translate("&cYou are currently muted for another &l" + punishment.getRemaining() + "&c."));
            }
            AxisUtils.getOnlineStaff().forEach(staff -> staff.sendMessage(new MessageUtil().setVariable("{0}", player.getName()).setVariable("{1}", punishment.getRemaining()).format(AxisConfiguration.getString("player_muted_alert"))));
            return;
        }

        if (!player.hasPermission(AxisUtils.STAFF_PERMISSION)) {
            if (ChatManager.canBeFiltered(event.getMessage())) {
                event.setCancelled(true);
                player.sendMessage(ColorText.translate("&cYour message was filtered."));

                AxisUtils.getOnlineStaff().forEach(online -> online.sendMessage(ColorText.translate("&c[Filtered] " + player.getDisplayName() + "&7: &f" + event.getMessage())));
                return;
            }
        }

        if (!player.hasPermission(AxisUtils.DONATOR_PERMISSION)) {
            if (profile.getChatDelay() > 0L) {
                event.setCancelled(true);
                player.sendMessage(ColorText.translate("&cYou can chat again in &l" + DurationFormatUtils.formatDurationWords(profile.getChatDelay(), true, true) + "&c."));
            } else {
                profile.setChatDelay(System.currentTimeMillis() + ChatManager.getChatDelay());
            }
        }

        event.setFormat(ColorText.translate(player.getDisplayName() + "&7: &f") + "%2$s");
    }
}