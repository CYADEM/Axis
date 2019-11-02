package me.tinchx.axis.rank.arguments;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.command.CommandSender;

public class RankCreateArgument extends AxisArgument {

    public RankCreateArgument() {
        super("create", null, AxisUtils.PERMISSION + "ranks.delete");
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
            if (rank != null) {
                sender.sendMessage(ColorText.translate("&cA rank with that name already exists."));
            } else {
                rank = new Rank(args[1]);
                rank.save();
                sender.sendMessage(ColorText.translate("&aRank '&7" + rank.getName() + "&a' has been created."));
                SponsorPlugin.getInstance().getRedisPublisher().write("AXIS","rank;reload");
            }
        }
    }
}