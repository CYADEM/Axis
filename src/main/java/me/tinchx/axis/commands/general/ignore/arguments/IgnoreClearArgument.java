package me.tinchx.axis.commands.general.ignore.arguments;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreClearArgument extends AxisArgument {

    public IgnoreClearArgument() {
        super("clear", "Clears all ignored players", null, "reset");
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + name;
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorText.translate("&cYou must be player to execute this command."));
            return;
        }
        Player player = (Player) sender;

        Profile profile = AxisAPI.getProfile(player);

        if (profile.getIgnoreList().isEmpty()) {
            player.sendMessage(ColorText.translate("&cYour ignore list is already empty!"));
        } else {
            profile.getIgnoreList().clear();
            player.sendMessage(ColorText.translate("&aYour ignore list has been cleared."));
        }
    }
}
