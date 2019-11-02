package me.tinchx.axis.rank;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Data;
import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.AxisPlugin;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static java.util.UUID.fromString;

@Data
public class Rank {

    private static List<Rank> ranks = new ArrayList<>();
    private static MongoCollection collection = AxisPlugin.getPlugin().getAxisDatabase().getRanks();

    private UUID id;
    private String name, prefix, suffix, color;
    private int priority;
    private List<String> permissions = new ArrayList<>(), inheritances = new ArrayList<>();
    private boolean defaultRank;

    public Rank(String name) {
        this(UUID.randomUUID(), name, 0);
    }

    private Rank(UUID id, String name, int priority) {
        this(id, name, "&f", null, "&f", priority, false);
    }

    public Rank(UUID id, String name, String prefix, String suffix, String color, int priority, boolean defaultRank) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.priority = priority;
        this.defaultRank = defaultRank;

        ranks.add(this);
    }

    public static void loadCache() {
        try (MongoCursor mongoCursor = collection.find().iterator()) {
            while (mongoCursor.hasNext()) {
                Document document = (Document) mongoCursor.next();

                Rank rank = new Rank(fromString(document.getString("id")), document.getString("name"), document.getInteger("priority"));

                rank.prefix = document.getString("prefix");
                if (document.containsKey("suffix")) {
                    rank.suffix = document.getString("suffix");
                }
                rank.color = document.getString("color");
                rank.defaultRank = document.getBoolean("defaultRank");

                if (document.containsKey("inheritances")) {
                    ((List<String>) AxisPlugin.getPlugin()
                            .getGson()
                            .fromJson(document.getString("inheritances"), AxisPlugin.getPlugin().getLIST_STRING_TYPE()))
                            .forEach(rankName -> rank.getInheritances().add(rankName));
                }

                ((List<String>) AxisPlugin.getPlugin().getGson().fromJson(document.getString("permissions"), AxisPlugin.getPlugin().getLIST_STRING_TYPE())).forEach(perm -> rank.getPermissions().add(perm));
            }
        }
        AxisAPI.getDefaultRank();
    }

    public void save() {
        Document document = new Document();
        document.put("id", id.toString());
        document.put("name", name);
        document.put("prefix", prefix);
        if (suffix != null) {
            document.put("suffix", suffix);
        }
        document.put("color", color);
        document.put("defaultRank", defaultRank);
        document.put("permissions", AxisPlugin.getPlugin().getGson().toJson(permissions));
        document.put("priority", priority);
        if (!getInheritances().isEmpty()) {
            document.put("inheritances", AxisPlugin.getPlugin().getGson().toJson(inheritances));
        }
        collection.replaceOne(eq("id", id.toString()), document, new UpdateOptions().upsert(true));
    }

    public void delete() {
        ranks.remove(id);
        collection.deleteOne(Filters.eq("id", id.toString()));
    }

    public static void reloadCache() {
        ranks.clear();
        loadCache();
    }

    public static List<Rank> getRanks() {
        return ranks;
    }
}