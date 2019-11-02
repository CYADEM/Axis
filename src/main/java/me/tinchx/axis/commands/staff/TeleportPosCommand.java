package me.tinchx.axis.commands.staff;

import com.google.common.primitives.Ints;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportPosCommand extends AxisCommand {

    public TeleportPosCommand() {
        super("teleportposition", null, "teleportpos", "teleportcoords", "tpcoords", "tppos");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 3) {
            player.sendMessage(ColorText.translate("&cUsage: /" + label + " <x> <y> <z>"));
        } else {
            try {
                Integer x = Ints.tryParse(args[0]);
                Integer y = Ints.tryParse(args[1]);
                Integer z = Ints.tryParse(args[2]);

                if (player.teleport(new Location(player.getWorld(), x, y, z))) {
                    Command.broadcastCommandMessage(player, ColorText.translate("&eTeleported to " + x + ", " + y + ", " + z +'.'));
                } else {
                    player.sendMessage(ColorText.translate("&cFailed teleport!"));
                }
            } catch (NullPointerException ex) {
                player.sendMessage(ColorText.translate("&cUsage: /" + label + " <x> <y> <z>"));
            }
        }
        return true;
    }
}