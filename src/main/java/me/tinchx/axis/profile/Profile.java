package me.tinchx.axis.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Data;
import lombok.Getter;
import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.AxisPlugin;
import me.tinchx.axis.cosmetic.Tag;
import me.tinchx.axis.punishment.Punishment;
import me.tinchx.axis.punishment.PunishmentHelper;
import me.tinchx.axis.punishment.PunishmentType;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.item.ItemMaker;
import me.tinchx.axis.utilities.location.LocationUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

@Data
public class Profile {

    @Getter
    private static Map<UUID, Profile> profileMap = new HashMap<>();
    private Map<UUID, ItemStack[]> armorContents, inventoryContents;
    private UUID uuid, lastConversation;
    private String address, playerName, nickName, lastServer, email, profileReddit, profileYoutube, profileGithub, profileDiscord, profileTwitter;
    private boolean soundsEnabled, vanished, staffMode, messagesEnabled, tipsEnabled, frozen, teamSpeakRegistered, socialSpy;
    private long firstJoin, lastJoin, chatDelay;
    private Rank rank;
    private List<UUID> alts, ignoreList;
    private List<Punishment> punishments;
    private List<String> permissions;
    private Tag tag;
    private Location backLocation;
    private int teamID;
    private ChatType chatType;

    public Profile(UUID uuid) {
        this.uuid = uuid;

        this.armorContents = new HashMap<>();
        this.inventoryContents = new HashMap<>();

        if (AxisConfiguration.getBoolean("ranks")) {
            this.rank = AxisAPI.getDefaultRank();
        }
        this.soundsEnabled = true;
        this.messagesEnabled = true;
        this.tipsEnabled = true;
        this.alts = new ArrayList<>();
        this.punishments = new ArrayList<>();
        this.ignoreList = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.chatType = ChatType.PUBLIC;

        profileMap.put(uuid, this);
        loadProfile();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public Rank getRank() {
        if (rank == null) {
            rank = AxisAPI.getDefaultRank();
        }
        return rank;
    }

    public Tag getTag() {
        if (tag == null) {
            return null;
        }
        return tag;
    }

    public List<Punishment> getPunishmentByType(PunishmentType type) {
        List<Punishment> toReturn = new ArrayList<>();
        for (Punishment punishment : punishments) {
            if (punishment.getType() == type) {
                toReturn.add(punishment);
            }
        }
        return toReturn;
    }

    public Punishment getBannedPunishment() {
        for (Punishment punishment : punishments) {
            if (punishment.isBan() && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }

    public Punishment getMutedPunishment() {
        for (Punishment punishment : punishments) {
            if (punishment.isMute() && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }

    public long getChatDelay() {
        return chatDelay - System.currentTimeMillis();
    }

    public void rollbackInventory() {
        Player player = getPlayer();
        if (player == null) {
            return;
        }
        PlayerInventory inventory = player.getInventory();

        inventory.setContents(inventoryContents.get(uuid));
        inventory.setArmorContents(armorContents.get(uuid));

        player.updateInventory();
    }

    public void setVanished(boolean vanished) {
        this.vanished = vanished;

        Player player = getPlayer();

        if (player == null) {
            return;
        }

        if (vanished) {
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (online.hasPermission(AxisUtils.STAFF_PERMISSION)) {
                    continue;
                }

                online.hidePlayer(player);
            }
        } else {
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (!AxisAPI.getProfile(online).isStaffMode() && online.hasPermission(AxisUtils.STAFF_PERMISSION)) {
                    continue;
                }

                online.showPlayer(player);
            }
        }

        if (isStaffMode()) {
            player.getInventory().setItem(8, new ItemMaker(Material.INK_SACK).setDurability((isVanished() ? 10 : 8)).setDisplayname((isVanished() ? "&c&lBecome Invisible" : "&a&lBecome Visible")).create());
            player.updateInventory();
        }
    }

    public boolean isIgnored(UUID uuid) {
        return getIgnoreList().contains(uuid);
    }

    public void findAlts() {
        if (address == null) {
            return;
        }

        alts.clear();

        try (MongoCursor<Document> cursor = AxisPlugin.getPlugin().getAxisDatabase().getProfiles().find(Filters.eq("address", address)).iterator()) {
            cursor.forEachRemaining(document -> {
                UUID uuid = UUID.fromString(document.getString("uuid"));

                if (!uuid.equals(this.getUuid())) {
                    if (!this.alts.contains(uuid)) {
                        this.alts.add(uuid);
                    }
                }
            });
        }
    }

    public void updateProfile() {
        Player player = getPlayer();

        if (player != null) {

            if (AxisConfiguration.getBoolean("ranks")) {
                player.setDisplayName(ColorText.translate((getTag() != null && getTag().getPrefix() != null ? getTag().getPrefix() : "") + getRank().getPrefix() + getRank().getColor() + (nickName != null && AxisConfiguration.getBoolean("nick_command") ? nickName : playerName) + (getRank().getSuffix() != null ? getRank().getSuffix() : "")));

                PermissionAttachment attachment = player.addAttachment(AxisPlugin.getPlugin());
                for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
                    if (attachmentInfo.getAttachment() != null) {
                        attachmentInfo.getAttachment().getPermissions().forEach((s, aBoolean) -> attachmentInfo.getAttachment().unsetPermission(s));
                    }
                }

                if (!getRank().getPermissions().isEmpty()) {
                    getRank().getPermissions().forEach(s -> attachment.setPermission(s, true));
                }

                if (!getRank().getInheritances().isEmpty()) {
                    getRank().getInheritances().forEach(rankInheritance -> {
                        Rank rank = AxisAPI.getRankByName(rankInheritance);
                        if (rank != null) {
                            rank.getPermissions().forEach(s -> attachment.setPermission(s, true));
                        }
                    });
                }

                if (!permissions.isEmpty()) {
                    permissions.forEach(s -> attachment.setPermission(s, true));
                }

                WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
                if (worldEditPlugin != null && worldEditPlugin.isEnabled()) {
                    if (player.hasPermission(AxisUtils.STAFF_PERMISSION)) {
                        attachment.setPermission("worldedit.navigation.thru.tool", true);
                        attachment.setPermission("worldedit.navigation.jumpto.tool", true);
                    }
                }

                player.recalculatePermissions();
            } else {
                player.setDisplayName((getTag() != null && getTag().getPrefix() != null ? getTag().getPrefix() : "") + AxisPlugin.getPlugin().getChat().getPlayerPrefix(player) + AxisPlugin.getPlugin().getChat().getPrimaryGroup(player) + player.getName());
            }
            updateTab();
        }
    }

    public void updateTab() {
        Player player = getPlayer();

        if (player == null) return;

        if (AxisConfiguration.getBoolean("ranks")) {
            player.setPlayerListName(ColorText.translate(getRank().getColor() + playerName));
        } else {
            String prefix = AxisPlugin.getPlugin().getChat().getPrimaryGroup(player);
            player.setPlayerListName(getColorPrefix(prefix) + playerName);
        }
    }

    private String getColorPrefix(String prefix) {
        if (prefix.isEmpty()) {
            return "";
        }
        char code = 'f';
        char magic = 'f';
        int count = 0;
        for (String string : prefix.split("&")) {
            if ((!string.isEmpty()) && (ChatColor.getByChar(string.toCharArray()[0]) != null)) {
                if (count == 0) {
                    code = string.toCharArray()[0];
                } else {
                    magic = string.toCharArray()[0];
                }
                count++;
            }
        }
        ChatColor color = ChatColor.getByChar(code);
        ChatColor magicColor = ChatColor.getByChar(magic);
        return color.toString() + magicColor.toString();
    }

    private void loadProfile() {
        Document document = (Document) AxisPlugin.getPlugin().getAxisDatabase().getProfiles().find(Filters.eq("uuid", uuid.toString())).first();
        if (document == null) {
            return;
        }

        address = document.getString("address");
        playerName = document.getString("playerName");
        soundsEnabled = document.getBoolean("soundsEnabled");
        lastJoin = document.getLong("lastJoin");
        if (AxisConfiguration.getBoolean("ranks"))
            rank = (AxisAPI.getRankByName(document.getString("rank")) == null ? AxisAPI.getDefaultRank() : AxisAPI.getRankByName(document.getString("rank")));
        messagesEnabled = document.getBoolean("messagesEnabled");
        tipsEnabled = document.getBoolean("tipsEnabled");
        lastServer = document.getString("lastServer");
        if (document.containsKey("tag") && AxisConfiguration.getBoolean("cosmetics") && AxisAPI.getTagByName(document.getString("tag")) != null) {
            tag = AxisAPI.getTagByName(document.getString("tag"));
        }

        teamSpeakRegistered = document.getBoolean("teamSpeakRegistered");
        teamID = document.getInteger("teamID");

        if (document.containsKey("backLocation")) {
            try {
                backLocation = LocationUtils.getLocation(document.getString("backLocation"));
            } catch (Exception ignored) {
            }

        }

        try (MongoCursor<Document> mongoCursor = AxisPlugin.getPlugin().getAxisDatabase().getPunishments().find(Filters.eq("targetID", uuid.toString())).iterator()) {
            mongoCursor.forEachRemaining(document1 -> {
                Punishment punishment = new Punishment();

                PunishmentHelper.load(document1, punishment);
                punishments.add(punishment);
            });
        }
        if (document.containsKey("ignoreDocument")) {
            Document ignoreDocument = (Document) document.get("ignoreDocument");
            for (String key : ignoreDocument.keySet()) {
                ignoreList.add(UUID.fromString(ignoreDocument.getString(key)));
            }
        }
        if (document.containsKey("permissions")) {
            if (document.get("permissions") instanceof String) {
                JsonArray permsArray = new JsonParser().parse(document.getString("permissions")).getAsJsonArray();

                for (JsonElement jsonElement : permsArray) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    try {
                        permissions.add(jsonObject.get("perm").getAsString());
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        if (document.containsKey("email")) {
            email = document.getString("email");
        }

        if (document.containsKey("nickName")) {
            nickName = document.getString("nickName");
        }

        if (document.containsKey("profileReddit")) {
            profileReddit = document.getString("profileReddit");
        }

        if (document.containsKey("profileYoutube")) {
            profileYoutube = document.getString("profileYoutube");
        }

        if (document.containsKey("profileGithub")) {
            profileGithub = document.getString("profileGithub");
        }

        if (document.containsKey("profileDiscord")) {
            profileDiscord = document.getString("profileDiscord");
        }

        if (document.containsKey("profileTwitter")) {
            profileTwitter = document.getString("profileTwitter");
        }

        if (document.containsKey("chatType")) {
            chatType = ChatType.valueOf(document.getString("chatType"));
        }
    }

    public void save() {
        Document document = new Document();
        document.put("uuid", uuid.toString());
        document.put("address", address);
        document.put("playerName", playerName);
        document.put("playerNameLowerCase", playerName.toLowerCase());
        document.put("soundsEnabled", soundsEnabled);
        document.put("firstJoin", firstJoin);
        document.put("lastJoin", lastJoin);
        if (AxisConfiguration.getBoolean("ranks")) document.put("rank", getRank().getName());
        document.put("messagesEnabled", messagesEnabled);
        document.put("tipsEnabled", tipsEnabled);
        document.put("lastServer", lastServer);
        document.put("teamSpeakRegistered", teamSpeakRegistered);
        document.put("teamID", teamID);

        if (backLocation != null) {
            document.put("backLocation", LocationUtils.getString(backLocation));
        }

        if (AxisConfiguration.getBoolean("cosmetics") && getTag() != null) document.put("tag", getTag().getName());
        if (!ignoreList.isEmpty()) {
            Document ignoreDocument = new Document();
            for (UUID uuid : ignoreList) {
                String name = Bukkit.getOfflinePlayer(uuid).getName();
                ignoreDocument.put(name, uuid.toString());
            }
            document.put("ignoreDocument", ignoreDocument);
        }

        JsonArray permsArray = new JsonArray();

        for (String permission : permissions) {
            JsonObject object = new JsonObject();
            object.addProperty("perm", permission);
            permsArray.add(object);
        }

        if (permsArray.size() > 0) {
            document.put("permissions", permsArray.toString());
        }

        if (email != null) {
            document.put("email", email);
        }

        if (nickName != null) {
            document.put("nickName", nickName);
        }

        if (profileReddit != null) {
            document.put("profileReddit", profileReddit);
        }

        if (profileYoutube != null) {
            document.put("profileYoutube", profileYoutube);
        }

        if (profileGithub != null) {
            document.put("profileGithub", profileGithub);
        }

        if (profileDiscord != null) {
            document.put("profileDiscord", profileDiscord);
        }

        if (profileTwitter != null) {
            document.put("profileTwitter", profileTwitter);
        }

        document.put("chatType", chatType.name());

        Bson bson = Filters.eq("uuid", uuid.toString());
        FindIterable iterable = AxisPlugin.getPlugin().getAxisDatabase().getProfiles().find(bson);

        if (iterable.first() == null) {
            AxisPlugin.getPlugin().getAxisDatabase().getProfiles().insertOne(document);
        } else {
            AxisPlugin.getPlugin().getAxisDatabase().getProfiles().replaceOne(Filters.eq("uuid", uuid.toString()), document);
        }
    }

    void removeFromMap() {
        profileMap.remove(uuid);
    }

}