package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCommand extends AxisCommand {

    public SkullCommand() {
        super("skull", null, "head");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.performCommand("skull " + player.getName());
        } else {
            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(args[0]);
            item.setItemMeta(meta);

            for (ItemStack itemStack : player.getInventory().addItem(item).values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            }

            player.sendMessage(new MessageUtil().setVariable("{0}", args[0]).format(AxisConfiguration.getString("skull_given")));
        }
        return true;
    }
}