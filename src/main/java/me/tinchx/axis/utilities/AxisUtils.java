package me.tinchx.axis.utilities;

import com.google.common.base.Preconditions;
import net.minecraft.server.v1_8_R3.MathHelper;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.utilities.chat.ColorText;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AxisUtils {

    public static String STAFF_PERMISSION = AxisConfiguration.getString("staff_permission");
    public static String DONATOR_PERMISSION = AxisConfiguration.getString("donator_permission");
    public static String DONATOR_TOP_PERMISSION = AxisConfiguration.getString("donator_top_permission");
    public static String PERMISSION = "axis.command.";
    public static String ONLY_PLAYERS = AxisConfiguration.getString("only_players");

    public static List<Player> getOnlineStaff() {
        List<Player> players = new ArrayList<>();
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.hasPermission(STAFF_PERMISSION)) {
                players.add(online);
            }
        }
        return players;
    }

    public static List<Player> getOnlineOperators() {
        List<Player> players = new ArrayList<>();
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.isOp()) {
                players.add(online);
            }
        }
        return players;
    }

    public static String getPlayerNotFoundMessage(String name) {
        return ColorText.translate("&6Player with name '&f" + name + "&6' not found.");
    }

    public static boolean isOnline(OfflinePlayer target) {
        return target != null && target.isOnline();
    }

    public static int getRandomNumber(int random) {
        return new Random().nextInt(random);
    }

    public static List<String> getCompletions(String[] args, List<String> input) {
        return getCompletions(args, input, 80);
    }

    private static List<String> getCompletions(String[] args, List<String> input, int limit) {
        Preconditions.checkNotNull((Object) args);
        Preconditions.checkArgument(args.length != 0);
        String argument = args[args.length - 1];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(limit).collect(Collectors.toList());
    }

    public static int bestLocation(double position) {
        return MathHelper.floor(position * 32.0);
    }
}