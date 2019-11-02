package me.tinchx.axis.commands.staff.gamemode.arguments;

import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeAdventureArgument extends AxisArgument {

    public GamemodeAdventureArgument() {
        super("adventure", null, "", "a", "2");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <playerName>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        Player target;
        if (args.length < 2) {
            if (sender instanceof Player) {
                target = (Player) sender;
                target.setGameMode(GameMode.ADVENTURE);
                sender.sendMessage(ColorText.translate("&6Your gamemode has been changed to &fADVENTURE&e."));
            }
        } else {
            target = Bukkit.getPlayer(args[1]);
            if (!AxisUtils.isOnline(target)) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[1]));
                return;
            }
            target.setGameMode(GameMode.ADVENTURE);
            Command.broadcastCommandMessage(sender, ColorText.translate("&6Gamemode of " + target.getDisplayName() + " &6has been changed to &fADVENTURE&6."));
        }
    }
}