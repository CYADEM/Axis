package me.tinchx.axis.rank.arguments;

import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankListArgument extends AxisArgument {

    public RankListArgument() {
        super("list");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name;
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 50)));
        sender.sendMessage(ColorText.translate("&6Ranks:"));
        List<Rank> rankList = new ArrayList<>(Rank.getRanks());
        rankList.sort(Comparator.comparingInt(Rank::getPriority));
        rankList.forEach(rank -> sender.sendMessage(ColorText.translate(" &7- " + rank.getColor() + rank.getName() + " &f(Priority: " + rank.getPriority() + ')')));
        sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 50)));
    }
}