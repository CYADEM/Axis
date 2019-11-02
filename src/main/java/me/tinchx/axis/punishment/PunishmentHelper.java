package me.tinchx.axis.punishment;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.tinchx.axis.AxisPlugin;
import org.bson.Document;

import java.util.UUID;

public class PunishmentHelper {

    public static void load(Document document, Punishment punishment) {
        if (document != null) {
            punishment.setUuid(UUID.fromString(document.getString("uuid")));
            punishment.setType(PunishmentType.valueOf(document.getString("type")));
            punishment.setTargetID(UUID.fromString(document.getString("targetID")));
            punishment.setAddedReason(document.getString("addedReason"));
            punishment.setAddedAt(document.getLong("addedAt"));
            punishment.setSilent(document.getBoolean("silent"));

            if (document.containsKey("addedBy")) {
                punishment.setAddedBy(UUID.fromString(document.getString("addedBy")));
            }

            if (document.containsKey("removedAt")) {
                punishment.setRemovedAt(document.getLong("removedAt"));
            }

            if (document.containsKey("removedBy")) {
                punishment.setRemovedBy(UUID.fromString(document.getString("removedBy")));
            }

            if (document.containsKey("removedReason")) {
                punishment.setRemovedReason(document.getString("removedReason"));
            }

            if (document.containsKey("expiration")) {
                punishment.setExpiration(document.getLong("expiration"));
            }
        }
    }

    public static void save(Punishment punishment) {
        Document document = new Document();

        document.put("uuid", punishment.getUuid().toString());
        document.put("type", punishment.getType().name());
        document.put("targetID", punishment.getTargetID().toString());
        document.put("addedReason", punishment.getAddedReason());
        document.put("addedAt", punishment.getAddedAt());
        document.put("silent", punishment.isSilent());

        if (punishment.getAddedBy() != null) {
            document.put("addedBy", punishment.getAddedBy().toString());
        }

        if (punishment.getRemovedAt() > 0L) {
            document.put("removedAt", punishment.getRemovedAt());
        }

        if (punishment.getRemovedBy() != null) {
            document.put("removedBy", punishment.getRemovedBy().toString());
        }

        if (punishment.getRemovedReason() != null) {
            document.put("removedReason", punishment.getRemovedReason());
        }

        if (punishment.getExpiration() > 0L) {
            document.put("expiration", punishment.getExpiration());
        }

        AxisPlugin.getPlugin().getAxisDatabase().getPunishments().replaceOne(Filters.eq("uuid", punishment.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

}