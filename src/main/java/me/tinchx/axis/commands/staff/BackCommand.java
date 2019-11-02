package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BackCommand extends AxisCommand {

    public BackCommand() {
        super("back");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.performCommand("back " + player.getName());
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                sender.sendMessage(ColorText.translate("&c" + args[0] + " has never played before."));
                return false;
            }

            Profile profile = AxisAPI.getProfile(target.getUniqueId());
            if (profile.getBackLocation() == null) {
                player.sendMessage(ColorText.translate("&cLast location could not be found!"));
            } else {
                if (player.teleport(profile.getBackLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN)) {
                    player.sendMessage(ColorText.translate("&6You have been teleported to the last location &6of " + profile.getRank().getColor() + target.getName() + "&6!"));
                } else {
                    player.sendMessage(ColorText.translate("&cFailed teleport!"));
                }
            }
        }
        return true;
    }
}