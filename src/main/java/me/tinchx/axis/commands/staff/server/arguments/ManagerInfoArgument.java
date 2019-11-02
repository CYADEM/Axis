package me.tinchx.axis.commands.staff.server.arguments;

import me.tinchx.axis.server.Server;
import me.tinchx.axis.server.ServerProfile;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class ManagerInfoArgument extends AxisArgument {

    public ManagerInfoArgument() {
        super("info", "See info about server");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <server>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            ServerProfile serverProfile = Server.getServerByName(args[1]);
            if (serverProfile == null) {
                sender.sendMessage(ColorText.translate("&cThat server could not be found."));
            } else {
                sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 15) + "&r &6&l" + serverProfile.getName().toUpperCase() + " &7&m" + StringUtils.repeat("-", 15)));
                sender.sendMessage(ColorText.translate("&c&lName: &f" + serverProfile.getName()));
                sender.sendMessage(ColorText.translate("&c&lOnline Players: &f" + serverProfile.getOnlinePlayers()));
                sender.sendMessage(ColorText.translate("&c&lOnline Staffs: &f" + serverProfile.getOnlineStaffs()));
                sender.sendMessage(ColorText.translate("&c&lOnline OPs: &f" + serverProfile.getOnlineOperators()));
                sender.sendMessage(ColorText.translate("&c&lMax Players: &f" + serverProfile.getMaxPlayers()));
                sender.sendMessage(ColorText.translate("&c&lWhitelisted &f" + serverProfile.isWhitelisted()));
                sender.sendMessage(" ");
                sender.sendMessage(ColorText.translate("&7This server is updated every 1 minute."));
            }
        }
    }
}