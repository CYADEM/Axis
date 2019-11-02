package me.tinchx.axis.commands.general.message;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand extends AxisCommand {

    public ReplyCommand() {
        super("reply", null, "r");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        Profile profile = AxisAPI.getProfile(player);
        Player target;
        if (profile.getLastConversation() == null || (target = Bukkit.getPlayer(profile.getLastConversation())) == null) {
            player.sendMessage(ColorText.translate("&cYou are not messaging anyone currently."));
        } else {
            if (args.length < 1) {
                player.sendMessage(ColorText.translate("&7You are currently messaging " + (AxisConfiguration.getBoolean("ranks") ? AxisAPI.getRank(target).getColor() + target.getName() : target.getDisplayName()) + "&7."));
                player.sendMessage(ColorText.translate("&cUsage: /" + label + " <message...>"));
                return false;
            }

            StringBuilder message = new StringBuilder();

            for (String arg : args) {
                message.append(arg).append(' ');
            }
            player.performCommand("msg " + Bukkit.getPlayer(profile.getLastConversation()).getName() + ' ' + message.toString());
        }
        return true;
    }
}