package me.tinchx.axis.profile.commands;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.chat.ChatUtil;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand extends AxisCommand {

    public ProfileCommand() {
        super("profile");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                sender.sendMessage(ColorText.translate("&c" + args[0] + " has never played before."));
                return false;
            }
            Profile profile = AxisAPI.getProfile(target.getUniqueId());
            sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 20) + "&r &a&l" + target.getName() + " &7&m" + StringUtils.repeat("-", 20)));
            sender.sendMessage(ColorText.translate("&eAddress: &f" + (profile.getAddress() == null ? "&c127.0.0.1" : profile.getAddress())));
            sender.sendMessage(ColorText.translate("&eCurrent Rank: " + (AxisConfiguration.getBoolean("ranks") ? profile.getRank().getColor() + profile.getRank().getName() : axis.getChat().getPrimaryGroup(null, target))));
            if (AxisConfiguration.getBoolean("cosmetics") && profile.getTag() != null && profile.getTag().getPrefix() != null) {
                sender.sendMessage(ColorText.translate("&eCurrent Tag: " + profile.getTag().getPrefix()));
            }
            sender.sendMessage(ColorText.translate("&eLast Name: &f" + profile.getPlayerName()));
            sender.sendMessage(ColorText.translate("&eLast Server: &f" + profile.getLastServer()));
            if (profile.getBackLocation() != null && sender instanceof Player) {
                new ChatUtil("&eLast Location: &f" + profile.getBackLocation().getBlockX() + ", " + profile.getBackLocation().getBlockY() + ", " + profile.getBackLocation().getBlockZ() + " &a&l&nCLICK HERE TO BE TELEPORTED", "&7Click here", "/teleport " + target.getName()).send((Player) sender);
            }
            if (!profile.getPermissions().isEmpty()) {
                sender.sendMessage(ColorText.translate("&ePermissions: &f(" + profile.getPermissions().size() + ')'));
                profile.getPermissions().forEach(s -> sender.sendMessage(ColorText.translate(" &7- &f" + s)));
            }
            if (profile.getEmail() != null) {
                sender.sendMessage(ColorText.translate("&eE-Mail: &f" + profile.getEmail()));
            }
            sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 20) + "&r &a&l" + target.getName() + " &7&m" + StringUtils.repeat("-", 20)));
        }
        return true;
    }
}