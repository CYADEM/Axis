package me.tinchx.axis.commands.general.message;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.chat.ChatManager;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand extends AxisCommand {

    public MessageCommand() {
        super("message", null, "msg", "tell", "whisper", "m");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName> <message...>"));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(target) || (sender instanceof Player && !((Player) sender).canSee(target))) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }
            Profile profile = AxisAPI.getProfile(target);

            if ((!profile.isMessagesEnabled() || (sender instanceof Player && profile.isIgnored(((Player) sender).getUniqueId()))) && !sender.isOp()) {
                sender.sendMessage(ColorText.translate("&cThat player has disabled private messages."));
                return false;
            }

            profile.setLastConversation((sender instanceof Player ? ((Player) sender).getUniqueId() : null));
            if (sender instanceof Player) {
                AxisAPI.getProfile((Player) sender).setLastConversation(target.getUniqueId());
            }

            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(' ');
            }

            String senderName = (AxisConfiguration.getBoolean("ranks") ? (sender instanceof Player ? AxisAPI.getRank((Player) sender).getColor() + sender.getName() : "&fConsole") : (sender instanceof Player ? ((Player) sender).getDisplayName() : "&fConsole"));
            String targetName = (AxisConfiguration.getBoolean("ranks") ? profile.getRank().getColor() + target.getName() : target.getDisplayName());
            target.sendMessage(new MessageUtil().setVariable("{0}", senderName).setVariable("{1}", message.toString()).format(AxisConfiguration.getString("message_from")));
            sender.sendMessage(new MessageUtil().setVariable("{0}", targetName).setVariable("{1}", message.toString()).format(AxisConfiguration.getString("message_to")));

            if (profile.isSoundsEnabled()) target.playSound(target.getLocation(), Sound.LEVEL_UP, 1, 1);

            if (sender instanceof Player && AxisAPI.getProfile((Player) sender).isFrozen()) {
                AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(ColorText.translate("&c[Frozen] " + senderName + " &7-> " + targetName + "&7: &f" + message.toString())));
            } else if (ChatManager.canBeFiltered(message.toString())) {
                AxisUtils.getOnlineStaff().forEach(player -> player.sendMessage(ColorText.translate("&c[Filtered] " + senderName + " &7-> " + targetName + "&7: &f" + message.toString())));
            } else {
                AxisUtils.getOnlineStaff().forEach(player -> {
                    if (AxisAPI.getProfile(player).isSocialSpy()) {
                        player.sendMessage(ColorText.translate("&c[Spy] " + senderName + " &7-> " + targetName + "&7: &f" + message.toString()));
                    }
                });
            }
        }
        return true;
    }
}