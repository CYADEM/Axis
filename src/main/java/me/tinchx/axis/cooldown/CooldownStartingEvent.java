package me.tinchx.axis.cooldown;

import lombok.Getter;
import lombok.Setter;
import me.tinchx.axis.listeners.events.PlayerBase;
import org.bukkit.entity.Player;

@Getter
public class CooldownStartingEvent extends PlayerBase {

    private Cooldown cooldown;
    @Setter
    private String reason;

    public CooldownStartingEvent(Player player, Cooldown cooldown) {
        super(player);
        this.cooldown = cooldown;
    }

}
