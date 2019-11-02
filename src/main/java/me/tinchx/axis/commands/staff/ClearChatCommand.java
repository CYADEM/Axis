package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand extends AxisCommand {

    public ClearChatCommand() {
        super("clearchat", null, "cc");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for(Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.hasPermission(AxisUtils.STAFF_PERMISSION)) {
                continue;
            }
            online.sendMessage(new String[120]);
        }
        Bukkit.broadcastMessage(new MessageUtil().setVariable("{0}", sender.getName()).format(AxisConfiguration.getString("clear_chat")));
        return true;
    }
}