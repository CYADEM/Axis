package me.tinchx.axis.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

public interface AridiAdapter {

    String getTitle(Player player);

    List<String> getLines(Player player);

    AridiStyle getAridiStyle(Player player);
}