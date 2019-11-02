package me.tinchx.axis.server;

import java.util.HashMap;
import java.util.Map;

public class Server {

    private static Map<String, ServerProfile> profileMap = new HashMap<>();

    public static void addServer(ServerProfile profile) {
        if (!profileMap.containsKey(profile.getName())) {
            profileMap.put(profile.getName(), profile);
        }
    }

    public static void removeServer(ServerProfile profile) {
        profileMap.remove(profile.getName());
    }

    public static ServerProfile getServerByName(String name) {
        return profileMap.get(name);
    }
}