package me.tinchx.axis.scoreboard;

import lombok.Getter;
import me.tinchx.axis.utilities.AxisUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Aridi {

    private List<AridiEntry> entries = new ArrayList<>();
    private List<String> strings = new ArrayList<>();
    private Scoreboard scoreboard;
    private Objective objective;
    private UUID id;
    private AridiManager aridiManager;

    public Aridi(Player player, AridiManager aridiManager) {
        this.id = player.getUniqueId();
        this.aridiManager = aridiManager;
        setUp(player);
    }

    public void setUp(Player player) {
        if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
            scoreboard = player.getScoreboard();
        } else {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        objective = scoreboard.registerNewObjective("AxisPlugin", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(aridiManager.getAdapter().getTitle(player));

        player.setScoreboard(scoreboard);
    }

    String getUniqueString() {
        String string = getRandomColor();
        while (strings.contains(string)) {
            string = string + getRandomColor();
        }

        if (string.length() > 16) {
            return getUniqueString();
        }

        strings.add(string);
        return string;
    }

    AridiEntry getEntryAtPosition(int position) {
        if (position >= entries.size()) {
            return null;
        }
        return entries.get(position);
    }

    private static String getRandomColor() {
        return ChatColor.values()[AxisUtils.getRandomNumber(ChatColor.values().length)].toString();
    }
}