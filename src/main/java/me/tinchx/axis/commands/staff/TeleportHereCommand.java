package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportHereCommand extends AxisCommand {

    public TeleportHereCommand() {
        super("teleporthere", null, "tphere", "s");
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
            if (player.isOp() && (args[0].equalsIgnoreCase("*") || args[0].equalsIgnoreCase("all"))) {
                for(Player online : Bukkit.getOnlinePlayers()) {
                    if (online == player) {
                        continue;
                    }

                    online.teleport(player);
                    online.sendMessage(ColorText.translate("&6You have been teleported to " + player.getDisplayName() + "&6!"));
                }
                Command.broadcastCommandMessage(sender, ColorText.translate("&6All players have been &fteleported &6to your &flocation&6!"));
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(target)) {
                player.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }
            if (target.teleport(player, PlayerTeleportEvent.TeleportCause.COMMAND)) {
                Command.broadcastCommandMessage(player, ColorText.translate("&eTeleported " + target.getDisplayName() + " &eto " + player.getDisplayName() + "&e."));
            } else {
                player.sendMessage(ColorText.translate("&cFailed teleport!"));
            }
        }
        return true;
    }
}