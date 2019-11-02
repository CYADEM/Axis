package me.tinchx.axis.listeners;

import me.tinchx.axis.utilities.AxisUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerFixListener implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if ((event.getFoodLevel() < ((Player) event.getEntity()).getFoodLevel()) && (AxisUtils.getRandomNumber(100) > 4)) {
            event.setCancelled(true);
        }
    }
}