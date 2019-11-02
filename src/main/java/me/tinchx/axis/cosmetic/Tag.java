package me.tinchx.axis.cosmetic;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import me.tinchx.axis.AxisPlugin;
import me.tinchx.axis.utilities.chat.ColorText;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tag {

    @Getter
    private static List<Tag> tags = new ArrayList<>();
    @Getter
    @Setter
    private String name, prefix, permission, section;

    public Tag(String name) {
        this.name = name;
        this.section = "DEFAULT";

        tags.add(this);
    }

    public static void loadTags() {
        try (MongoCursor mongoCursor = AxisPlugin.getPlugin().getAxisDatabase().getTags().find().iterator()) {
            while (mongoCursor.hasNext()) {
                Document document = (Document) mongoCursor.next();
                Tag tag = new Tag(document.getString("name"));

                if (document.containsKey("prefix")) {
                    tag.setPrefix(ColorText.translate(document.getString("prefix")));
                }
                if (document.containsKey("permission")) {
                    tag.setPermission(document.getString("permission"));
                }
                if (document.containsKey("section")) {
                    tag.setSection(document.getString("section"));
                }
            }
        }
    }

    public void save() {
        Document document = new Document();
        document.put("name", name);
        if (prefix != null) {
            document.put("prefix", prefix);
        }
        if (permission != null) {
            document.put("permission", permission);
        }
        document.put("section", section);
        AxisPlugin.getPlugin().getAxisDatabase().getTags().replaceOne(Filters.eq("name", name), document, new UpdateOptions().upsert(true));
    }

    public void remove() {
        tags.remove(name);
        AxisPlugin.getPlugin().getAxisDatabase().getTags().deleteOne(Filters.eq("name", name));
    }

    public static List<Tag> getTagsBySection(String section) {
        return tags.stream().filter(tag -> tag.getSection().equalsIgnoreCase(section)).collect(Collectors.toList());
    }
}