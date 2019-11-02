package me.tinchx.axis.cooldown;

import lombok.Getter;
import me.tinchx.axis.listeners.events.CustomEvent;
import me.tinchx.axis.listeners.events.PlayerBase;
import org.bukkit.entity.Player;

@Getter
public class CooldownExpiredEvent extends PlayerBase {

    private Cooldown cooldown;
    private boolean forced;

    CooldownExpiredEvent(Player player, Cooldown cooldown) {
        super(player);
        this.cooldown = cooldown;
    }

    public CustomEvent setForced(boolean forced) {
        this.forced = forced;
        return this;
    }
}
