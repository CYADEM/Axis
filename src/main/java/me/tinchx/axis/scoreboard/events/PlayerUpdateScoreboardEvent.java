package me.tinchx.axis.scoreboard.events;

import lombok.Getter;
import me.tinchx.axis.listeners.events.PlayerBase;
import me.tinchx.axis.scoreboard.Aridi;
import org.bukkit.entity.Player;

@Getter
public class PlayerUpdateScoreboardEvent extends PlayerBase {

    private Aridi scoreboard;

    public PlayerUpdateScoreboardEvent(Player player, Aridi scoreboard) {
        super(player);
        this.scoreboard = scoreboard;
    }
}