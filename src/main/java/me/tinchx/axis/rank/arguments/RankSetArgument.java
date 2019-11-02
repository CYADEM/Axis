package me.tinchx.axis.rank.arguments;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankSetArgument extends AxisArgument {

    public RankSetArgument() {
        super("set", null, AxisUtils.PERMISSION + "ranks.set");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name + " <rankName> <playerName>";
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
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
                if ((!target.hasPlayedBefore()) && (!target.isOnline())) {
                    sender.sendMessage(ColorText.translate("&c" + args[2] + " has never played before."));
                } else {
                    if (sender instanceof Player) {
                        int priority = AxisAPI.getRank((Player) sender).getPriority();
                        if (rank.getPriority() < priority) {
                            sender.sendMessage(ColorText.translate("&cYou may not set ranks higher than your current rank."));
                            return;
                        }
                    }
                    /*Profile profile;
                    if (AxisConfiguration.getBoolean("teamspeak_api") && (profile = AxisAPI.getProfile(target.getUniqueId())).getTeamID() != 0) {
                        if (!target.isOp()) {
                            for (ServerGroup serverGroup : AxisPlugin.getPlugin().getTs3Api().getServerGroupsByClientId(profile.getTeamID())) {
                                AxisPlugin.getPlugin().getTs3Api().removeClientFromServerGroup(serverGroup.getId(), profile.getTeamID());
                            }
                        }
                        if (rank.getTeamspeakID() != 0) {
                            AxisPlugin.getPlugin().getTs3Api().addClientToServerGroup(rank.getTeamspeakID(), profile.getTeamID());
                        }
                    }*/
                    SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "profile;setrank;" + target.getUniqueId().toString() + ';' + rank.getName());
                    sender.sendMessage(new MessageUtil().setVariable("{0}", target.getName()).setVariable("{1}", rank.getColor() + rank.getName()).format(AxisConfiguration.getString("target_rank_updated")));
                }
            }
        }
    }
}