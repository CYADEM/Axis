package me.tinchx.axis.commands.staff;

import com.google.common.primitives.Ints;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;

public class SlotsCommand extends AxisCommand {

    public SlotsCommand() {
        super("slots", null, "setslots", "setmaxplayers");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <amount>"));
        } else {
            Integer integer = Ints.tryParse(args[0]);
            if (integer == null || integer < 0) {
                sender.sendMessage(ColorText.translate("&cWrong syntax!"));
            } else {
                try {
                    setMaxPlayers(integer);
                    Command.broadcastCommandMessage(sender, ColorText.translate("&6Set the maximum players to &f" + integer + "&6!"));
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void setMaxPlayers(int amount) throws ReflectiveOperationException {
        String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        Object playerList = Class.forName("org.bukkit.craftbukkit." + bukkitVersion + ".CraftServer")
                .getDeclaredMethod("getHandle", null).invoke(Bukkit.getServer(), null);
        Field maxPlayers = playerList.getClass().getSuperclass().getDeclaredField("maxPlayers");
        maxPlayers.setAccessible(true);
        maxPlayers.set(playerList, amount);
    }
}