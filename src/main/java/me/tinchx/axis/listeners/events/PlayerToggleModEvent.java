package me.tinchx.axis.listeners.events;

import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerToggleModEvent extends PlayerBase {

    @Getter
    private boolean inMod;

    public PlayerToggleModEvent(Player player, boolean inMod) {
        super(player);
        this.inMod = inMod;
    }
}
