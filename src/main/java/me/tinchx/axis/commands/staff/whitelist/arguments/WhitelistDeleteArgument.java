package me.tinchx.axis.commands.staff.whitelist.arguments;

import me.tinchx.axis.commands.staff.whitelist.Whitelist;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class WhitelistDeleteArgument extends AxisArgument {

    public WhitelistDeleteArgument() {
        super("delete");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name;
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        Whitelist whitelist = Whitelist.getByServer(Bukkit.getServerName());
        if (whitelist == null) {
            sender.sendMessage(ColorText.translate("&cWhitelist with that name doesn't exists."));
        } else {
            whitelist.remove();
            sender.sendMessage(ColorText.translate("&6Whitelist named '&f" + Bukkit.getServerName() + "&6' successfully deleted!"));
        }
    }
}