package me.tinchx.axis.rank.arguments;

import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.sponsor.SponsorPlugin;
import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.rank.Rank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankInheritanceArgument extends AxisArgument {

    public RankInheritanceArgument() {
        super("inheritance");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <rankName> <add|remove> <rankInheritance>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Rank rank = AxisAPI.getRankByName(args[1]);
            if (rank == null) {
                sender.sendMessage(ColorText.translate("&cA rank with that name doesn't exists."));
            } else {
                if (sender instanceof Player) {
                    int priority = AxisAPI.getRank((Player) sender).getPriority();
                    if (rank.getPriority() < priority) {
                        sender.sendMessage(ColorText.translate("&cYou may not modify higher than your current rank."));
                        return;
                    }
                }
                Rank rankInheritance = AxisAPI.getRankByName(args[3]);
                if (rankInheritance == null) {
                    sender.sendMessage(ColorText.translate("&cA rank with that name doesn't exists."));
                } else {
                    if (args[2].equalsIgnoreCase("add")) {
                        rank.getInheritances().add(rankInheritance.getName());
                    } else {
                        rank.getInheritances().remove(rankInheritance.getName());
                    }
                    sender.sendMessage(ColorText.translate(rank.getColor() + rank.getName() + "&e's rank inheritance has been updated."));
                    rank.save();
                    SponsorPlugin.getInstance().getRedisPublisher().write("CORE", "rank;reload");
                    SponsorPlugin.getInstance().getRedisPublisher().write("CORE", "profile;update;all");
                }
            }
        }
    }
}