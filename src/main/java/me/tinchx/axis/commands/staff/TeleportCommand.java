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

public class TeleportCommand extends AxisCommand {

    public TeleportCommand() {
        super("teleport", null, "tp");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
        } else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                sender.sendMessage(ColorText.translate("&c" + args[0] + " has never played before."));
                return false;
            }
            Profile profile = AxisAPI.getProfile(target.getUniqueId());
            if (profile.getBackLocation() != null && !AxisUtils.isOnline(target)) {
                if (player.teleport(profile.getBackLocation(), PlayerTeleportEvent.TeleportCause.COMMAND)) {
                    player.sendMessage(ColorText.translate("&6Teleporting you to offline player " + profile.getRank().getColor() + target.getName() + "&6."));
                } else {
                    player.sendMessage(ColorText.translate("&cFailed teleport!"));
                }
            } else if (AxisUtils.isOnline(target)) {
                if (player.teleport(target.getPlayer(), PlayerTeleportEvent.TeleportCause.COMMAND)) {
                    Command.broadcastCommandMessage(player, ColorText.translate("&6Teleporting you to " + profile.getRank().getColor() + target.getName() + "&6."));
                } else {
                    player.sendMessage(ColorText.translate("&cFailed teleport!"));
                }
            } else {
                player.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
            }
        }
        return true;
    }
}