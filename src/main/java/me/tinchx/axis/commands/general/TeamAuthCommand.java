package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamAuthCommand extends AxisCommand {

    public TeamAuthCommand() {
        super("teamauth", null, "tr", "teamregister", "teamreg");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AxisUtils.ONLY_PLAYERS);
            return false;
        }
        Player player = (Player) sender;
        Profile profile = AxisAPI.getProfile(player);
        if (profile.isTeamSpeakRegistered()) {
            player.sendMessage(AxisConfiguration.getString("teamspeak_player_already_registered"));
            return false;
        }
        profile.setTeamSpeakRegistered(true);
        player.sendMessage(AxisConfiguration.getString("teamspeak_player_registered"));
        return true;
    }
}