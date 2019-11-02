package me.tinchx.axis.rank.arguments;

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

public class RankSuffixArgument extends AxisArgument {

    public RankSuffixArgument() {
        super("suffix", null, AxisUtils.PERMISSION + "ranks.suffix");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <rankName> <suffix>";
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
                if (sender instanceof Player) {
                    int priority = AxisAPI.getRank((Player) sender).getPriority();
                    if (rank.getPriority() < priority) {
                        sender.sendMessage(ColorText.translate("&cYou may not modify higher than your current rank."));
                        return;
                    }
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }

                String suffix = sb.toString().trim().replace("\"", "");
                if (suffix.equalsIgnoreCase("null")) {
                    rank.setSuffix(null);
                } else {
                    rank.setSuffix(ColorText.translate(suffix));
                }
                rank.save();
                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "rank;reload");
                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "profile;reload;all");
                sender.sendMessage(new MessageUtil().setVariable("{0}", rank.getColor() + rank.getName()).format(AxisConfiguration.getString("rank_data_updated")));
            }
        }
    }
}