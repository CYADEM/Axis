package me.tinchx.axis.commands.general.ignore.arguments;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreAddArgument extends AxisArgument {
    public IgnoreAddArgument() {
        super("add", "Starts ignoring a player");
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <playerName>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorText.translate("&cYou must be player to execute this command."));
            return;
        }
        Player player = (Player) sender;

        Profile profile = AxisAPI.getProfile(player.getUniqueId());

        if (args.length < 2) {
            player.sendMessage(ColorText.translate("&cUsage: /" + getUsage(label)));
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                player.sendMessage(ColorText.translate("&c" + target.getName() + " has never played before."));
                return;
            }

            if (target == player) {
                player.sendMessage(ColorText.translate("&cYou cannot ignore yourself."));
            } else if (profile.isIgnored(target.getUniqueId())) {
                player.sendMessage(ColorText.translate("&cYou're already ignoring to " + target.getName() + '.'));
                player.sendMessage(ColorText.translate("&eUse /" + label + " remove " + target.getName()));
            } else {
                profile.getIgnoreList().add(target.getUniqueId());
                player.sendMessage(ColorText.translate("&cYou are now ignoring " + target.getName() + '.'));
            }
        }
    }
}