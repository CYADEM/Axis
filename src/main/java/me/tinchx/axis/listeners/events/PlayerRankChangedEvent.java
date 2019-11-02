package me.tinchx.axis.listeners.events;

import lombok.Getter;
import me.tinchx.axis.rank.Rank;
import org.bukkit.entity.Player;

public class PlayerRankChangedEvent extends PlayerBase {

    @Getter
    private Rank rank;

    public PlayerRankChangedEvent(Player player, Rank rank) {
        super(player);
        this.rank = rank;
    }
}