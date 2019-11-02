package me.tinchx.axis.commands.staff;

import me.tinchx.axis.AxisAPI;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.command.AxisCommand;
import me.tinchx.axis.utilities.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class KillCommand extends AxisCommand {

    public KillCommand() {
        super("kill");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <playerName>"));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!AxisUtils.isOnline(target)) {
                sender.sendMessage(AxisUtils.getPlayerNotFoundMessage(args[0]));
                return false;
            }

            if (AxisAPI.getProfile(target).isStaffMode()) {
                sender.sendMessage(ColorText.translate("&cYou can't kill " + target.getName() +  " because has StaffMode enabled!"));
                return false;
            }

            PlayerInventory playerInventory = target.getInventory();

            Map<UUID, ItemStack[]> contentsList = new HashMap<>();
            Map<UUID, ItemStack[]> armorList = new HashMap<>();

            contentsList.put(target.getUniqueId(), playerInventory.getContents());
            armorList.put(target.getUniqueId(), playerInventory.getArmorContents());

            target.setHealth(0);
            TaskUtil.runTask(() -> target.spigot().respawn());

            boolean isLobby = Bukkit.getServerName().toLowerCase().contains("hub") || Bukkit.getServerName().toLowerCase().contains("lobby");

            if (isLobby) {
                TaskUtil.runTaskLater(() ->{
                    if (target.isOnline()) {
                        playerInventory.setContents(contentsList.get(target.getUniqueId()));
                        playerInventory.setArmorContents(armorList.get(target.getUniqueId()));

                        target.updateInventory();
                    }
                }, 20L);
            }

            Command.broadcastCommandMessage(sender, ColorText.translate("&e" + target.getName() + " killed!"));
        }
        return true;
    }
}