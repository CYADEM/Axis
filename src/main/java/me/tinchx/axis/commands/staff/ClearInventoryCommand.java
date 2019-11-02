package me.tinchx.axis.commands.staff;

import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class ClearInventoryCommand extends AxisCommand {

    public ClearInventoryCommand() {
        super("clear", null, "clearinventory", "ci");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        PlayerInventory playerInventory;
        if (args.length < 1) {
            if (sender instanceof Player) {
                target = (Player) sender;
                playerInventory = target.getInventory();
                playerInventory.clear();
                playerInventory.setArmorContents(null);
                target.sendMessage(ColorText.translate("&eYour inventory has been cleared."));
            } else {
                sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
            }
        } else {
            target = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(target)) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }
            playerInventory = target.getInventory();
            playerInventory.clear();
            playerInventory.setArmorContents(null);
            Command.broadcastCommandMessage(sender, ColorText.translate(target.getDisplayName() + "&e's inventory has been cleared."));
        }
        return true;
    }
}