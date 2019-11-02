package me.tinchx.axis.commands.staff.permission.arguments;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class PermissionRemoveArgument extends AxisArgument {

    public PermissionRemoveArgument() {
        super("remove");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <playerName> <permission>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                sender.sendMessage(ColorText.translate("&c" + args[1] + " has never played before."));
                return;
            }
            Profile profile = AxisAPI.getProfile(target.getUniqueId());
            profile.getPermissions().remove(args[2]);
            profile.updateProfile();
            profile.save();
            SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "profile;update;uuid;" + target.getUniqueId().toString());
            sender.sendMessage(ColorText.translate("&6" + target.getName() + "'s perms successfully &fupdated&6!"));
        }
    }
}