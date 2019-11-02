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

public class RankPermsArgument extends AxisArgument {

    public RankPermsArgument() {
        super("perms", null, AxisUtils.PERMISSION + "ranks.perms");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <rankName> <add/remove> <permission>";
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

                String permission = args[3];
                if (args[2].equalsIgnoreCase("add")) {
                    if (!rank.getPermissions().contains(permission)) {
                        rank.getPermissions().add(permission);
                    }
                    rank.save();
                    sender.sendMessage(new MessageUtil().setVariable("{0}", rank.getColor() + rank.getName()).format(AxisConfiguration.getString("rank_data_updated")));
                } else if (args[2].equalsIgnoreCase("remove")) {
                    rank.getPermissions().remove(permission);
                    rank.save();
                    sender.sendMessage(new MessageUtil().setVariable("{0}", rank.getColor() + rank.getName()).format(AxisConfiguration.getString("rank_data_updated")));
                } else {
                    sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
                    return;
                }
                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "rank;reload");
                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "profile;reload;all");
            }
        }
    }
}