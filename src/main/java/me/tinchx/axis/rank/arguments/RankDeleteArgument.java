package me.tinchx.axis.rank.arguments;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankDeleteArgument extends AxisArgument {

    public RankDeleteArgument() {
        super("delete", null, AxisUtils.PERMISSION + "ranks.delete");
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
                if (sender instanceof Player) {
                    int priority = AxisAPI.getRank((Player) sender).getPriority();
                    if (rank.getPriority() < priority) {
                        sender.sendMessage(ColorText.translate("&cYou may not delete higher than your current rank."));
                        return;
                    }
                }
                rank.delete();
                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS","rank;reload");
                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS","profile;reload;all");
                sender.sendMessage(ColorText.translate("&cRank '&7" + args[1] + "&c' has been deleted."));
            }
        }
    }
}