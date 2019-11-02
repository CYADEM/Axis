package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleSoundsCommand extends AxisCommand {

    public ToggleSoundsCommand() {
        super("togglesounds", null, "sounds");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        Profile profile = AxisAPI.getProfile(player);
        profile.setSoundsEnabled(!profile.isSoundsEnabled());
        player.sendMessage(AxisConfiguration.getString("sounds_" + (profile.isSoundsEnabled() ? "enabled" : "disabled")));
        return true;
    }
}