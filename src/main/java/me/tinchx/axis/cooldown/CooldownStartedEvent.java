package me.tinchx.axis.cooldown;

import lombok.Getter;
import me.tinchx.axis.listeners.events.PlayerBase;
import org.bukkit.entity.Player;

public class CooldownStartedEvent extends PlayerBase {

    @Getter
    private Cooldown cooldown;

    public CooldownStartedEvent(Player player, Cooldown cooldown) {
        super(player);
        this.cooldown = cooldown;
    }
}
