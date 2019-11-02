package me.tinchx.axis.commands.general;

import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand extends AxisCommand {

    public RenameCommand() {
        super("rename");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            player.sendMessage(ColorText.translate("&cYou must have anything in your hand."));
        } else {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (args.length < 1) {
                player.sendMessage(ColorText.translate("&cUsage: /" + label + " <newName>"));
            } else {
                itemMeta.setDisplayName((sender.isOp() ? ColorText.translate(StringUtils.join(args, " ")) : StringUtils.join(args, " ")));
                itemStack.setItemMeta(itemMeta);
                player.updateInventory();
                player.sendMessage(ColorText.translate("&6You renamed the item in your hand."));

            }
        }
        return true;
    }
}