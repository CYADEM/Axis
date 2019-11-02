package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends AxisCommand {

    public JoinCommand() {
        super("join", null, "joinserver");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage(ColorText.translate("&cUsage: /" + label + " <serverName>"));
        } else {
            AxisAPI.sendToServer(player, args[0]);
            player.sendMessage(new MessageUtil().setVariable("{0}", args[0]).format(AxisConfiguration.getString("connecting_to")));
        }
        return true;
    }
}