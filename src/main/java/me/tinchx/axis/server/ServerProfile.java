package me.tinchx.axis.server;

import lombok.Data;

@Data
public class ServerProfile {

    private String name;
    private int onlinePlayers, maxPlayers, onlineStaffs, onlineOperators;
    //private double tps[];
    private boolean whitelisted;

    public ServerProfile(String name) {
        this.name = name;
    }
}