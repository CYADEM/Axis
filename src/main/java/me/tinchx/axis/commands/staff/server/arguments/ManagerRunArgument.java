package me.tinchx.axis.commands.staff.server.arguments;

import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.command.CommandSender;

public class ManagerRunArgument extends AxisArgument {

    public ManagerRunArgument() {
        super("runcmd","Run console command on all or specific server");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label+ ' ' + name + " <server|all> <cmd>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            String server = args[1];

            StringBuilder builder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                builder.append(args[i]).append(' ');
            }

            sender.sendMessage(ColorText.translate("&aPerforming &9" + builder.toString().replaceFirst("/", "") + " &aby " + sender.getName() + " on " + (server.equalsIgnoreCase("all") ? "network" : server) + '.'));
            SponsorPlugin.getInstance().getRedisPublisher().write("CORE", "servermanager;runcmd;" + server + ';' + builder.toString().replaceFirst("/", ""));
        }
    }
}