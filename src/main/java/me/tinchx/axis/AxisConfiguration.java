package me.tinchx.axis;

import me.tinchx.axis.configuration.Config;
import me.tinchx.axis.utilities.chat.ColorText;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AxisConfiguration {

    private static Map<String, String> stringMap = new HashMap<>();
    private static Map<String, Boolean> booleanMap = new HashMap<>();
    private static Map<String, List<String>> listMap = new HashMap<>();
    private static Map<String, Integer> integerMap = new HashMap<>();

    static void setUpData(MemorySection config) {
        Config lang = new Config(AxisPlugin.getPlugin(), "lang.yml");
        stringMap.put("key", config.getString("server.key"));
        stringMap.put("loading_data", lang.getString("lang.loading_data"));
        stringMap.put("only_players", lang.getString("lang.only_players"));
        stringMap.put("player_ping", lang.getString("lang.player_ping"));
        stringMap.put("staff_permission", config.getString("permissions.staff"));
        stringMap.put("donator_permission", config.getString("permissions.donator"));
        stringMap.put("donator_top_permission", config.getString("permissions.donator_top"));
        stringMap.put("no_permissions", lang.getString("lang.no_permissions"));
        stringMap.put("argument_not_found", lang.getString("lang.argument_not_found"));
        stringMap.put("clear_chat", lang.getString("lang.public_chat_cleared"));
        stringMap.put("mute_chat", lang.getString("lang.public_chat_muted"));
        stringMap.put("unmute_chat", lang.getString("lang.public_chat_unmuted"));
        stringMap.put("discord", config.getString("server.discord"));
        stringMap.put("address", config.getString("server.server_ip"));
        stringMap.put("server_start", lang.getString("lang.server_start"));
        stringMap.put("server_stop", lang.getString("lang.server_stop"));
        stringMap.put("chat_mode_enabled", lang.getString("lang.chat_mode_enabled"));
        stringMap.put("chat_mode_disabled", lang.getString("lang.chat_mode_disabled"));
        stringMap.put("staff_chat_format", lang.getString("lang.staff_chat_format"));
        stringMap.put("admin_chat_format", lang.getString("lang.admin_chat_format"));
        stringMap.put("owner_chat_format", lang.getString("lang.owner_chat_format"));
        stringMap.put("staff_join", lang.getString("lang.staff_join"));
        stringMap.put("staff_leave", lang.getString("lang.staff_leave"));
        stringMap.put("request", lang.getString("lang.request"));
        stringMap.put("report", lang.getString("lang.report"));
        stringMap.put("teamspeak", config.getString("server.teamspeak"));
        stringMap.put("server_lobby", config.getString("server.lobby_name"));
        stringMap.put("connecting_to", lang.getString("lang.connecting_to"));
        stringMap.put("rank_updated_message", lang.getString("lang.rank_updated"));
        stringMap.put("target_rank_updated", lang.getString("lang.target_rank_updated"));
        stringMap.put("rank_data_updated", lang.getString("lang.rank_data_updated"));
        stringMap.put("ranks_successfully_imported", lang.getString("lang.ranks_successfully_imported"));
        stringMap.put("whitelist_kick", lang.getString("lang.whitelist_kick"));
        stringMap.put("punishment_format", lang.getString("lang.punishment_format"));
        stringMap.put("lang_discord", lang.getString("lang.discord"));
        stringMap.put("message_to", lang.getString("lang.message_to"));
        stringMap.put("message_from", lang.getString("lang.message_from"));
        stringMap.put("tips_enabled", lang.getString("lang.tips_enabled"));
        stringMap.put("tips_disabled", lang.getString("lang.tips_disabled"));
        stringMap.put("sounds_enabled", lang.getString("lang.sounds_enabled"));
        stringMap.put("sounds_disabled", lang.getString("lang.sounds_disabled"));
        stringMap.put("messages_enabled", lang.getString("lang.messages_enabled"));
        stringMap.put("messages_disabled", lang.getString("lang.messages_disabled"));
        stringMap.put("skull_given", lang.getString("lang.skull_given"));
        stringMap.put("freeze_message", lang.getString("lang.freeze_message"));
        stringMap.put("target_frozen", lang.getString("lang.target_frozen"));
        stringMap.put("target_unfrozen", lang.getString("lang.target_unfrozen"));
        stringMap.put("sender_target_frozen", lang.getString("lang.sender_target_frozen"));
        stringMap.put("sender_target_unfrozen", lang.getString("lang.sender_target_unfrozen"));
        stringMap.put("target_frozen_alert", lang.getString("lang.target_frozen_alert"));
        stringMap.put("target_unfrozen_alert", lang.getString("lang.target_unfrozen_alert"));
        stringMap.put("cant_freeze_staff", lang.getString("lang.cant_freeze_staff"));
        stringMap.put("target_left_frozen", lang.getString("lang.target_left_frozen"));
        stringMap.put("staff_mode_enabled", lang.getString("lang.staff_mode_enabled"));
        stringMap.put("staff_mode_disabled", lang.getString("lang.staff_mode_disabled"));
        stringMap.put("vanish_enabled", lang.getString("lang.vanish_enabled"));
        stringMap.put("vanish_disabled", lang.getString("lang.vanish_disabled"));
        stringMap.put("player_muted_alert", lang.getString("lang.player_muted_alert"));
        stringMap.put("tag_removed", lang.getString("lang.tag_removed"));
        stringMap.put("tag_target_removed", lang.getString("lang.tag_target_removed"));
        stringMap.put("player_list", lang.getString("lang.player_list"));
        stringMap.put("request_sent", lang.getString("lang.request_sent"));
        stringMap.put("report_sent", lang.getString("lang.report_sent"));
        stringMap.put("whitelist_running", lang.getString("lang.whitelist_running"));
        stringMap.put("whitelist_unwhitelisted", lang.getString("lang.whitelist_unwhitelisted"));
        stringMap.put("whitelist_unwhitelisted_tooltip", lang.getString("lang.whitelist_unwhitelisted_tooltip"));
        stringMap.put("web_direct_url", config.getString("server.web_direct_url"));
        stringMap.put("web_player_registered", lang.getString("lang.web_player_registered"));
        stringMap.put("nick_name_changed", lang.getString("lang.nick_name_changed"));
        stringMap.put("social_spy_enabled", lang.getString("lang.social_spy_enabled"));
        stringMap.put("social_spy_disabled", lang.getString("lang.social_spy_disabled"));
        stringMap.put("slow_chat_removed", lang.getString("lang.slow_chat_removed"));
        stringMap.put("slow_chat_set", lang.getString("lang.slow_chat_set"));

        booleanMap.put("announce_enabled", config.getBoolean("announce.enabled"));
        booleanMap.put("do_mob_spawn", config.getBoolean("server.do_mob_spawn"));
        booleanMap.put("do_leaves_decay", config.getBoolean("server.do_leaves_decay"));
        booleanMap.put("do_weather_change", config.getBoolean("server.do_weather_change"));
        booleanMap.put("do_thunder_change", config.getBoolean("server.do_thunder_change"));
        booleanMap.put("do_block_burn", config.getBoolean("server.do_block_burn"));
        booleanMap.put("do_block_spread", config.getBoolean("server.do_block_spread"));
        booleanMap.put("do_block_explode", config.getBoolean("server.do_entity_explode"));
        booleanMap.put("staff_mode", config.getBoolean("server.staff_mode"));
        booleanMap.put("hub_command", config.getBoolean("server.hub_command"));
        booleanMap.put("join_command", config.getBoolean("server.join_command"));
        booleanMap.put("do_world_load_clean", config.getBoolean("server.do_world_load_clean"));
        booleanMap.put("do_world_load_remove_entities", config.getBoolean("server.do_world_load_remove_entities"));
        booleanMap.put("cosmetics", config.getBoolean("server.cosmetics"));
        booleanMap.put("ranks", config.getBoolean("server.ranks"));
        booleanMap.put("web_register", config.getBoolean("server.web_register"));
        booleanMap.put("nick_command", config.getBoolean("server.nick_command"));
        booleanMap.put("social_links", config.getBoolean("server.social_links"));

        listMap.put("announce_messages", config.getStringList("announce.messages"));
        listMap.put("filtered_words", config.getStringList("filtered_words"));
        listMap.put("allowed_links", config.getStringList("allowed_links"));
        listMap.put("disallowed_commands", config.getStringList("disallowed_commands"));

        integerMap.put("announce_delay", config.getInt("announce.delay"));

    }

    public static String getString(String string) {
        if (stringMap.containsKey(string)) {
            return ColorText.translate(stringMap.get(string));
        }
        return "¡String '" + string + "' could not be found!";
    }

    public static boolean getBoolean(String string) {
        if (booleanMap.containsKey(string)) {
            return booleanMap.get(string);
        }
        return false;
    }

    public static List<String> getArray(String string) {
        if (listMap.containsKey(string)) {
            return ColorText.translate(listMap.get(string));
        }
        return new ArrayList<>();
    }

    public static int getInteger(String string) {
        if (integerMap.containsKey(string)) {
            return integerMap.get(string);
        }
        return 0;
    }
}