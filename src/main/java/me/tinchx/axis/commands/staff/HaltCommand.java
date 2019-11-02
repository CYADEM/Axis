package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.listeners.events.PlayerFreezeEvent;
import me.tinchx.axis.profile.ChatType;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HaltCommand extends AxisCommand {

    public HaltCommand() {
        super("halt", null, "freeze", "screenshare", "ss");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(target)) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }
            Profile profile = AxisAPI.getProfile(target);
            if (target.hasPermission(AxisUtils.STAFF_PERMISSION)) {
                sender.sendMessage(AxisConfiguration.getString("cant_freeze_staff"));
                return false;
            }

            boolean haltBoolean;
            String senderName = (AxisConfiguration.getBoolean("ranks") ? (sender instanceof Player ? AxisAPI.getRank((Player) sender).getColor() + sender.getName() : "&fConsole") : (sender instanceof Player ? sender.getDisplayName() : "&fConsole"));
            String targetName = (AxisConfiguration.getBoolean("ranks") ? profile.getRank().getColor() + target.getName() : target.getDisplayName());
            profile.setFrozen(haltBoolean = !profile.isFrozen());
            if (sender instanceof Player) {
                AxisAPI.getProfile((Player) sender).setChatType(((haltBoolean ? ChatType.FROZEN : ChatType.PUBLIC)));
            }
            new PlayerFreezeEvent(target, profile.isFrozen()).call();
            target.sendMessage(new MessageUtil().setVariable("{0}", senderName).format(AxisConfiguration.getString("target_" + (profile.isFrozen() ? "" : "un") + "frozen")));
            sender.sendMessage(new MessageUtil().setVariable("{0}", targetName).format(AxisConfiguration.getString("sender_target_" + (profile.isFrozen() ? "" : "un") + "frozen")));
            SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "freeze;" + (profile.isFrozen() ? "add" : "remove") + ';' + Bukkit.getServerName() + ';' + senderName + ';' + targetName);
        }
        return true;
    }
}