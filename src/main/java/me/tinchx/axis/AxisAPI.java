package me.tinchx.axis;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.cooldown.Cooldown;
import me.tinchx.axis.cosmetic.Tag;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.UUID;

public class AxisAPI {

    //PROFILE

    public static Profile getProfile(String name) {
        Profile found = null;
        for(Map.Entry<UUID, Profile> entry : Profile.getProfileMap().entrySet()) {
            Profile profile = entry.getValue();
            if (profile.getPlayerName() != null && profile.getPlayerName().equalsIgnoreCase(name)) {
                found = profile;
                break;
            }
        }
        return found;
    }

    public static Profile getProfile(Player player) {
        return getProfile(player.getUniqueId());
    }

    public static Profile getProfile(UUID uuid) {
        Profile profile = Profile.getProfileMap().get(uuid);
        if (profile == null) {
            profile = new Profile(uuid);
        }
        return profile;
    }

    //RANK

    public static Rank getRank(Player player) {
        return getProfile(player).getRank();
    }

    public static Rank getRankByUUID(UUID uuid) {
        for (Rank rank : Rank.getRanks()) {
            if (rank.getId().equals(uuid)) {
                return rank;
            }
        }
        return null;
    }

    public static Rank getRankByName(String name) {
        for (Rank rank : Rank.getRanks()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    public static Rank getDefaultRank() {
        for (Rank rank : Rank.getRanks()) {
            if (rank.isDefaultRank()) {
                return rank;
            }
        }
        Rank newRank = new Rank("User");
        newRank.setDefaultRank(true);
        newRank.save();
        return newRank;
    }

    //PLAYER

    public static int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    //SERVER

    public static void sendToServer(Player player, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ConnectOther");
        output.writeUTF(player.getName());
        output.writeUTF(server);
        player.sendPluginMessage(AxisPlugin.getPlugin(), "BungeeCord", output.toByteArray());
    }

    public static void broadcast(String message, String permission) {
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.hasPermission(permission)) {
                online.sendMessage(ColorText.translate(message));
            }
        }
    }

    public static void executeCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static void sendToConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ColorText.translate(message));
    }

    public static Location getSpawnPoint(World world) {
        return world.getSpawnLocation();
    }

    //NAMEMC

    public static boolean isLiked(UUID uuid) {
        try {
            URL url = new URL("https://api.namemc.com/server/" + AxisConfiguration.getString("address") + "/likes?profile=" + uuid);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();
                if (line.contains("true")) {
                    return true;
                }
            }
            bufferedReader.close();
        } catch (Exception ignored) {
        }
        return false;
    }

    //COOLDOWN
    public static Cooldown getCooldown(String name) {
        Cooldown cooldown = Cooldown.getCooldownMap().get(name);
        if (cooldown != null) {
            return cooldown;
        }
        return null;
    }

    public static Cooldown getCooldownByPlayerID(UUID uuid) {
        for (Map.Entry<String, Cooldown> cooldown : Cooldown.getCooldownMap().entrySet()) {
            if (cooldown.getValue().getLongMap().containsKey(uuid)) {
                return cooldown.getValue();
            }
        }
        return null;
    }

    //TAG
    public static Tag getTagByName(String name) {
        for (Tag tag : Tag.getTags()) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

}