package me.tinchx.axis.rank.arguments;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.axis.rank.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class RankInfoArgument extends AxisArgument {

    public RankInfoArgument() {
        super("info", null, "information");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <rankName>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Rank rank = AxisAPI.getRankByName(args[1]);
            if (rank == null) {
                sender.sendMessage(ColorText.translate("&cA rank with that name doesn't exists."));
            } else {
                sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 50)));
                sender.sendMessage(ColorText.translate("&6Rank&7: " + rank.getColor() + rank.getName()));
                sender.sendMessage("");
                sender.sendMessage(ColorText.translate("&6Priority&7: &f" + rank.getPriority()));
                sender.sendMessage(ColorText.translate("&6Default Rank&7: " + (rank.isDefaultRank() ? "&aYes" : "&cNo")));
                sender.sendMessage(ColorText.translate("&6Prefix&7: " + rank.getPrefix() + "Example"));
                if (rank.getSuffix() != null) {
                    sender.sendMessage(ColorText.translate("&6Suffix&7: &fExample" + rank.getSuffix()));
                }
                sender.sendMessage(ColorText.translate("&6Color&7: " + rank.getColor() + "Example"));
                if (!rank.getPermissions().isEmpty()) {
                    sender.sendMessage(ColorText.translate("&6Permissions&7: &f(" + rank.getPermissions().size() + ')'));
                    for (String permission : rank.getPermissions()) {
                        sender.sendMessage(ColorText.translate(" &7- &f" + permission));
                    }
                }
                if (!rank.getInheritances().isEmpty()) {
                    ColorText.translate("&7&m" + StringUtils.repeat("-", 50));
                    for (String inheritance : rank.getInheritances()) {
                        Rank rankInheritance = AxisAPI.getRankByName(inheritance);
                        if (rankInheritance == null) {
                            continue;
                        }
                        sender.sendMessage(ColorText.translate(rankInheritance.getColor() + rankInheritance.getName() + " &7has &f&l" + rankInheritance.getPermissions().size() + " &7permission(s)."));
                    }
                }
                sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 50)));
            }
        }
    }
}