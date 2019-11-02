package me.tinchx.axis.rank.arguments;

import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.configuration.Config;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.axis.utilities.task.TaskUtil;
import me.tinchx.sponsor.SponsorPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public class RankImportArgument extends AxisArgument {

    public RankImportArgument() {
        super("import", null, AxisUtils.PERMISSION + "ranks.import");
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + name;
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        Rank.getRanks().forEach(Rank::delete);

        Config config = new Config(axis, "ranks.yml");
        TaskUtil.runTaskLater(() -> {
            int i = 0;
            for (String string : config.getKeys(false)) {
                String name = config.getString(string + ".name");
                String prefix = config.getString(string + ".prefix");
                String suffix = config.getString(string + ".suffix");
                String color = config.getString(string + ".color");
                boolean defaultRank = config.getBoolean(string + ".default");
                List<String> perms = config.getStringList(string + ".permissions");
                List<String> inheritances = config.getStringList(string + ".inheritances");
                int priority = config.getInt(string + ".priority");

                Rank rank = new
                        Rank(UUID.randomUUID(),
                        name,
                        prefix,
                        suffix,
                        color,
                        priority,
                        defaultRank);
                for (String permission : perms) {
                    rank.getPermissions().add(permission);
                }
                for (String inheritance : inheritances) {
                    rank.getInheritances().add(inheritance);
                }
                rank.save();
                i++;
            }
            sender.sendMessage(new MessageUtil().setVariable("{0}", String.valueOf(i)).format(AxisConfiguration.getString("ranks_successfully_imported")));
            SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "rank;reload");
            SponsorPlugin.getInstance().getRedisPublisher().write("AXIS", "profile;reload;all");
        }, 40L);
    }
}