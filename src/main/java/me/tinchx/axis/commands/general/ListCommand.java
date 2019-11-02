package me.tinchx.axis.commands.general;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisCommand;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListCommand extends AxisCommand {

    public ListCommand() {
        super("list", null, "who", "online");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (AxisConfiguration.getBoolean("ranks")) {
            List<Player> players = new ArrayList<>();
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (!player.canSee(online) && !player.hasPermission(AxisUtils.STAFF_PERMISSION)) {
                        continue;
                    }
                }
                players.add(online);
            }
            List<Rank> ranks = new ArrayList<>(Rank.getRanks());
            List<String> names = new ArrayList<>();
            List<String> rankNames = new ArrayList<>();

            ranks.sort(Comparator.comparingInt(Rank::getPriority));
            players.sort(Comparator.comparingInt(o -> AxisAPI.getRank(o).getPriority()));
            players.forEach(player -> names.add((AxisAPI.getProfile(player).isVanished() ? "&7(HIDDEN) " : "") + AxisAPI.getRank(player).getColor() + player.getName()));

            ranks.forEach(rank -> rankNames.add(rank.getColor() + rank.getName()));
            sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 45)));
            sender.sendMessage(ColorText.translate(StringUtils.join(rankNames, "&7, ") + "&7."));
            sender.sendMessage("");
            sender.sendMessage(ColorText.translate("&7(&f" + Bukkit.getOnlinePlayers().size() + "&7/&f" + Bukkit.getMaxPlayers() + "&7): " + StringUtils.join(names, "&7, ") + "&7."));
            sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 45)));
        } else {
            sender.sendMessage(new MessageUtil().setVariable("{0}", String.valueOf(Bukkit.getServer().getOnlinePlayers().size())).setVariable("{1}", String.valueOf(Bukkit.getMaxPlayers())).format(AxisConfiguration.getString("player_list")));
        }
        return true;
    }
}