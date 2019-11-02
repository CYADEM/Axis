package me.tinchx.axis.scoreboard.events;

import lombok.Getter;
import me.tinchx.axis.listeners.events.PlayerBase;
import me.tinchx.axis.scoreboard.Aridi;
import org.bukkit.entity.Player;

@Getter
public class PlayerUnloadScoreboardEvent extends PlayerBase {

    private Aridi scoreboard;

    public PlayerUnloadScoreboardEvent(Player player, Aridi scoreboard) {
        super(player);
        this.scoreboard = scoreboard;
    }
}