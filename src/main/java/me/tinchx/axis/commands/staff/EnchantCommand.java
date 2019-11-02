package me.tinchx.axis.commands.staff;

import com.google.common.primitives.Ints;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantCommand extends AxisCommand {

    public EnchantCommand() {
        super("enchant");
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
            return false;
        }

        if (args.length < 2) {
            player.sendMessage(ColorText.translate("&cUsage: /" + label + " <enchantment> <level>"));
        } else {
            Enchantment enchantment = Enchantment.getByName(args[0].toUpperCase());
            Integer integer = Ints.tryParse(args[1]);
            if (enchantment == null) {
                player.sendMessage(ColorText.translate("&cAn enchantment with that name doesn't exists."));
            } else if(integer == null || integer < 0) {
                player.sendMessage(ColorText.translate("&cInvalid level."));
            } else {
                itemStack.addUnsafeEnchantment(enchantment, integer);
                String itemName = (itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : itemStack.getType().name());
                Command.broadcastCommandMessage(player, ColorText.translate("&6Applied &f" + enchantment.getName() + " &6at level &f" + integer + " &6onto " + itemName + "&6!"));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> toReturn = new ArrayList<>();
        if (args.length == 1) {
            for(Enchantment enchantment : Enchantment.values()) {
                toReturn.add(enchantment.getName());
            }
        }
        return AxisUtils.getCompletions(args, toReturn);
    }
}