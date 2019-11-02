package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand extends AxisCommand {

    public HubCommand() {
        super("hub", null, "lobby");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        AxisAPI.sendToServer(player, AxisConfiguration.getString("server_lobby"));
        player.sendMessage(new MessageUtil().setVariable("{0}", "lobby").format(AxisConfiguration.getString("connecting_to")));
        return true;
    }
}