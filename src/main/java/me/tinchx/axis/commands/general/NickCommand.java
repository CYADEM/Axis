package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCommand extends AxisCommand {

    public NickCommand() {
        super("nick");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        Profile profile = AxisAPI.getProfile(player);
        if (args.length < 1) {
            player.sendMessage(ColorText.translate("&cUsage: /" + label + " <reset/nick>"));
            if (profile.getNickName() != null) {
                player.sendMessage(ColorText.translate("&eYour current nick name is: &d" + profile.getNickName() + "&e!"));
            }
        } else {
            String nickName = args[0];
            if (args[0].equalsIgnoreCase("reset") && profile.getNickName() != null) {
                profile.setNickName(null);
                nickName = player.getName();
            } else {
                nickName = nickName.replace("&l", "");
                nickName = nickName.replace("&n", "");
                nickName = nickName.replace("&o", "");
                nickName = nickName.replace("&k", "");
                nickName = nickName.replace("&m", "");

                String finalName = nickName;
                for (int i = 0; i < 9; i++) {
                    finalName = finalName.replace("&" + i, "");
                }

                finalName = finalName.replace("&a", "");
                finalName = finalName.replace("&b", "");
                finalName = finalName.replace("&c", "");
                finalName = finalName.replace("&d", "");
                finalName = finalName.replace("&e", "");
                finalName = finalName.replace("&f", "");

                if (finalName.length() < 3) {
                    player.sendMessage(ColorText.translate("&cMinimum nick name size is 3 characters."));
                    return false;
                }
                if (finalName.length() > 10) {
                    player.sendMessage(ColorText.translate("&cMaximum nick name size is 10 characters."));
                    return false;
                }
                profile.setNickName(nickName);
            }
            profile.updateProfile();
            player.sendMessage(new MessageUtil().setVariable("{0}", nickName).format(AxisConfiguration.getString("nick_name_changed")));
        }
        return true;
    }
}