package me.tinchx.axis.rank.arguments;

import com.google.common.primitives.Ints;
import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankPriorityArgument extends AxisArgument {

    public RankPriorityArgument() {
        super("priority", null, AxisUtils.PERMISSION + "ranks.priority");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <rankName> <priority>";
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Rank rank = AxisAPI.getRankByName(args[1]);
            if (rank == null) {
                sender.sendMessage(ColorText.translate("&cA rank with that name doesn't exists."));
            } else {
                Integer integer = Ints.tryParse(args[2]);
                if (integer == null || integer < 0) {
                    sender.sendMessage(ColorText.translate("&cWrong syntax."));
                } else {
                    if (sender instanceof Player) {
                        int priority = AxisAPI.getRank((Player) sender).getPriority();
                        if (rank.getPriority() < priority) {
                            sender.sendMessage(ColorText.translate("&cYou may not set modify higher than your current rank."));
                            return;
                        }
                    }
                    rank.setPriority(integer);
                    rank.save();
                    SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "rank;reload");
                    sender.sendMessage(new MessageUtil().setVariable("{0}", rank.getColor() + rank.getName()).format(AxisConfiguration.getString("rank_data_updated")));
                }
            }
        }
    }
}