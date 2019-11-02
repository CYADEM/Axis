package me.tinchx.axis.utilities.command;

import lombok.Getter;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.chat.ColorText;
import me.tinchx.axis.utilities.chat.MessageUtil;
import me.tinchx.axis.AxisPlugin;
import me.tinchx.axis.AxisConfiguration;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AxisCommand implements CommandExecutor, TabCompleter {

    public AxisPlugin axis = AxisPlugin.getPlugin();

    private String name, description;
    private String[] aliases;

    @Getter
    private List<AxisArgument> axisArguments = new ArrayList<>();

    public AxisCommand(String name) {
        this(name, null);
    }

    public AxisCommand(String name, String description) {
        this(name, description, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public AxisCommand(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = Arrays.copyOf(aliases, aliases.length);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 50)));
            sender.sendMessage(ColorText.translate("&cAvailable sub-command(s) for '&7" + command.getName() + "&c'."));
            sender.sendMessage("");

            for (AxisArgument axisArgument : axisArguments) {
                if (axisArgument.permission != null && !sender.hasPermission(axisArgument.permission)) {
                    continue;
                }
                sender.sendMessage(ColorText.translate(" &e" + axisArgument.getUsage(label) + (axisArgument.description != null ? " &7- &f" + axisArgument.description : "")));
            }
            sender.sendMessage(ColorText.translate("&7&m" + StringUtils.repeat("-", 50)));
        } else {
            AxisArgument axisArgument = getArgument(args[0]);
            if (axisArgument == null || (axisArgument.permission != null && !sender.hasPermission(axisArgument.permission))) {
                sender.sendMessage(new MessageUtil().setVariable("{0}", WordUtils.capitalize(name)).setVariable("{1}", args[0]).format(AxisConfiguration.getString("argument_not_found")));
            } else {
                if (axisArgument.onlyplayers && sender instanceof ConsoleCommandSender) {
                    Bukkit.getConsoleSender().sendMessage(AxisUtils.ONLY_PLAYERS);
                    return false;
                }
                axisArgument.onExecute(sender, label, args);
            }
        }
        return true;
    }

    public AxisArgument getArgument(String name) {
        for (AxisArgument axisArgument : axisArguments) {
            if (axisArgument.name.equalsIgnoreCase(name) || Arrays.asList(axisArgument.aliases).contains(name.toLowerCase())) {
                return axisArgument;
            }
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length < 2) {

            for (AxisArgument axisArgument : axisArguments) {
                String permission = axisArgument.permission;
                if (permission == null || sender.hasPermission(permission)) {
                    results.add(axisArgument.name);
                }
            }

            if (results.isEmpty()) {
                return null;
            }
        } else {
            AxisArgument axisArgument = getArgument(args[0]);
            if (axisArgument == null) {
                return results;
            }

            String permission = axisArgument.permission;
            if (permission == null || sender.hasPermission(permission)) {
                results = axisArgument.onTabComplete(sender, label, args);

                if (results == null) {
                    return null;
                }
            }
        }

        return AxisUtils.getCompletions(args, results);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void registerArgument(AxisArgument axisArgument) {
        this.axisArguments.add(axisArgument);
    }

}