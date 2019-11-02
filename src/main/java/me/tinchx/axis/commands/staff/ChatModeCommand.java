package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.profile.ChatType;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatModeCommand extends AxisCommand {

    public ChatModeCommand() {
        super("ownerchat", null, "adminchat", "ac", "oc", "staffchat", "sc", "frozenchat", "fc", "publicchat", "pc");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        Profile profile = AxisAPI.getProfile(player);

        if (label.equalsIgnoreCase("ownerchat") || label.equalsIgnoreCase("oc")) {
            if (player.isOp()) {
                profile.setChatType((profile.getChatType() == ChatType.OWNER ? ChatType.PUBLIC : ChatType.OWNER));
                player.sendMessage(new MessageUtil().setVariable("{0}", "ownerchat").format(AxisConfiguration.getString("chat_mode_" + (profile.getChatType() == ChatType.OWNER ? "en" : "dis") + "abled")));
            } else {
                player.sendMessage(AxisConfiguration.getString("no_permissions"));
            }
        } else if (label.equalsIgnoreCase("adminchat") || label.equalsIgnoreCase("ac")) {
            if (player.hasPermission(AxisUtils.STAFF_PERMISSION + ".admin")) {
                profile.setChatType((profile.getChatType() == ChatType.ADMIN ? ChatType.PUBLIC : ChatType.ADMIN));
                player.sendMessage(new MessageUtil().setVariable("{0}", "adminchat").format(AxisConfiguration.getString("chat_mode_" + (profile.getChatType() == ChatType.ADMIN ? "en" : "dis") + "abled")));
            } else {
                player.sendMessage(AxisConfiguration.getString("no_permissions"));
            }
        } else if (label.equalsIgnoreCase("staffchat") || label.equalsIgnoreCase("sc")) {
            profile.setChatType((profile.getChatType() == ChatType.STAFF ? ChatType.PUBLIC : ChatType.STAFF));
            player.sendMessage(new MessageUtil().setVariable("{0}", "staffchat").format(AxisConfiguration.getString("chat_mode_" + (profile.getChatType() == ChatType.STAFF ? "en" : "dis") + "abled")));
        } else if (label.equalsIgnoreCase("frozenchat") || label.equalsIgnoreCase("fc")) {
            profile.setChatType((profile.getChatType() == ChatType.FROZEN ? ChatType.PUBLIC : ChatType.FROZEN));
            player.sendMessage(new MessageUtil().setVariable("{0}", "frozenchat").format(AxisConfiguration.getString("chat_mode_" + (profile.getChatType() == ChatType.FROZEN ? "en" : "dis") + "abled")));
        } else if (label.equalsIgnoreCase("publicchat") || label.equalsIgnoreCase("pc")) {
            profile.setChatType(ChatType.PUBLIC);
            player.sendMessage(new MessageUtil().setVariable("{0}", "publicchat").format(AxisConfiguration.getString("chat_mode_enabled")));
        }
        return true;
    }
}