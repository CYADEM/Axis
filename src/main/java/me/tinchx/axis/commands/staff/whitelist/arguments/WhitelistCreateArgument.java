package me.tinchx.axis.commands.staff.whitelist.arguments;

import me.tinchx.axis.commands.staff.whitelist.Whitelist;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.axis.utilities.time.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class WhitelistCreateArgument extends AxisArgument {

    public WhitelistCreateArgument() {
        super("create");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <duration>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Whitelist whitelist = Whitelist.getByServer(Bukkit.getServerName());
            if (whitelist != null) {
                sender.sendMessage(ColorText.translate("&cWhitelist countdown already created!"));
            } else {
                whitelist = new Whitelist(Bukkit.getServerName(), TimeUtils.parse(args[1]));
                sender.sendMessage(ColorText.translate("&6Whitelist named '&f" + whitelist.getServerName() + "&6' has been created!"));
                sender.sendMessage(ColorText.translate("&6Use /whitelist toggle"));
            }
        }
    }
}