package me.tinchx.axis.listeners.events;

import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerFreezeEvent extends PlayerBase {

    @Getter
    private boolean isFrozen;

    public PlayerFreezeEvent(Player player, boolean isFrozen) {
        super(player);
        this.isFrozen = isFrozen;
    }
}
