package me.tinchx.axis.utilities.command;

import me.tinchx.axis.AxisPlugin;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AxisArgument {

    public AxisPlugin axis = AxisPlugin.getPlugin();

    public String name, description, permission;
    public String[] aliases;
    public boolean onlyplayers;

    public AxisArgument(String name) {
        this(name, null);
    }

    public AxisArgument(String name, String description) {
        this(name, description, null);
    }

    public AxisArgument(String name, String description, String permission) {
        this(name, description, permission, ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public AxisArgument(String name, String description, String permission, String... aliases) {
        this(name, description, permission, aliases, false);
    }

    public AxisArgument(String name, String description, String permission, String[] aliases, boolean onlyplayers) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = Arrays.copyOf(aliases, aliases.length);
        this.onlyplayers = onlyplayers;
    }

    public abstract String getUsage(String label);

    public abstract void onExecute(CommandSender sender , String label, String[] args);

    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return Collections.emptyList();
    }
}