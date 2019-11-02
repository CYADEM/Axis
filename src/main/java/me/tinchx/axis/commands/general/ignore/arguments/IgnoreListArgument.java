package me.tinchx.axis.commands.general.ignore.arguments;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisArgument;
import me.tinchx.axis.utilities.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IgnoreListArgument extends AxisArgument {

    public IgnoreListArgument() {
        super("list", "List all ignore players.");
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + name;
    }

    @Override
    public void onExecute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorText.translate("&cYou must be player to execute this command."));
            return;
        }
        Player player = (Player) sender;

        Profile profile = AxisAPI.getProfile(player.getUniqueId());

        if (profile.getIgnoreList().isEmpty()) {
            player.sendMessage(ColorText.translate("&cYou are not ignoring anyone."));
        } else {
            player.sendMessage(ColorText.translate("&aLoading your ignore list..."));
            TaskUtil.runTaskLater(() -> {
                player.sendMessage("");
                player.sendMessage(ColorText.translate("&aYou are now ignoring (" + profile.getIgnoreList().size() + ") &7-"));
                player.sendMessage("");
                for (UUID uuid : profile.getIgnoreList()) {
                    OfflinePlayer ignored = Bukkit.getOfflinePlayer(uuid);
                    player.sendMessage(ColorText.translate("&7- " + (ignored.isOnline() ? "&a" : "&c") + ignored.getName()));
                }
                player.sendMessage("");
            }, 10L);
        }
    }
}