package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpyCommand extends AxisCommand {

    public SpyCommand() {
        super("socialspy", null, "spy");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        Profile profile = AxisAPI.getProfile(player);
        profile.setSocialSpy(!profile.isSocialSpy());

        player.sendMessage(AxisConfiguration.getString("social_spy_" + (profile.isSocialSpy() ? "en" : "dis") + "abled"));
        return true;
    }
}