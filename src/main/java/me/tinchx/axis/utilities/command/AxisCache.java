package me.tinchx.axis.utilities.command;

import me.tinchx.axis.AxisConfiguration;
import me.tinchx.axis.commands.general.*;
import me.tinchx.axis.commands.general.ignore.IgnoreCommand;
import me.tinchx.axis.commands.general.message.MessageCommand;
import me.tinchx.axis.commands.general.message.ReplyCommand;
import me.tinchx.axis.commands.general.message.ToggleMessagesCommand;
import me.tinchx.axis.commands.staff.*;
import me.tinchx.axis.commands.staff.chat.ChatCommand;
import me.tinchx.axis.commands.staff.gamemode.GameModeCommand;
import me.tinchx.axis.commands.staff.permission.PermissionCommand;
import me.tinchx.axis.commands.staff.punishment.*;
import me.tinchx.axis.commands.staff.server.ServerManagerCommand;
import me.tinchx.axis.commands.staff.whitelist.WhitelistCommand;
import me.tinchx.axis.cosmetic.PrefixCommand;
import me.tinchx.axis.cosmetic.TagCommand;
import me.tinchx.axis.profile.commands.ProfileCommand;
import me.tinchx.axis.rank.RankCommand;
import me.tinchx.axis.utilities.AxisUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public class AxisCache {

    private JavaPlugin javaPlugin;
    private CommandMap commandMap;

    public AxisCache(JavaPlugin main) {
        javaPlugin = main;
        registerCommand(new ClearChatCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new ClearInventoryCommand(), AxisUtils.PERMISSION + "clearinventory");
        registerCommand(new PingCommand());
        registerCommand(new RenameCommand(), AxisUtils.PERMISSION + "rename");
        registerCommand(new TeamSpeakCommand());
        registerCommand(new GameModeCommand(), AxisUtils.PERMISSION + "gamemode");
        registerCommand(new BroadcastCommand(), AxisUtils.PERMISSION + "broadcast");
        registerCommand(new ChatModeCommand(), AxisUtils.STAFF_PERMISSION);
        if (AxisConfiguration.getBoolean("ranks")) registerCommand(new RankCommand(), AxisUtils.PERMISSION + "rank");
        registerCommand(new HealCommand(), AxisUtils.PERMISSION + "heal");
        registerCommand(new ProfileCommand(), AxisUtils.PERMISSION + "profile");
        registerCommand(new KillCommand(), AxisUtils.PERMISSION + "kill");
        registerCommand(new FeedCommand(), AxisUtils.PERMISSION + "feed");
        registerCommand(new TeleportCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new TeleportHereCommand(), AxisUtils.PERMISSION + "tphere");
        registerCommand(new TeleportPosCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new ChatCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new DiscordCommand());
        registerCommand(new ListCommand());
        registerCommand(new MessageCommand());
        registerCommand(new ReplyCommand());
        registerCommand(new ToggleTipsCommand());
        registerCommand(new FlightCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new MassSayCommand(), AxisUtils.PERMISSION + "massay");
        registerCommand(new SudoCommand(), AxisUtils.PERMISSION + "sudo");
        registerCommand(new ToggleMessagesCommand());
        registerCommand(new ToggleSoundsCommand());
        registerCommand(new SkullCommand(), AxisUtils.PERMISSION + "skull");
        registerCommand(new HaltCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new VanishCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new IgnoreCommand());
        registerCommand(new EnchantCommand(), AxisUtils.PERMISSION + "enchant");
        registerCommand(new SlotsCommand(), AxisUtils.PERMISSION + "slots");
        registerCommand(new AltsCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new ServerManagerCommand(), AxisUtils.PERMISSION + "servermanager");
        if (AxisConfiguration.getBoolean("cosmetics")) {
            registerCommand(new TagCommand(), AxisUtils.PERMISSION + "tag");
            registerCommand(new PrefixCommand());
        }
        if (AxisConfiguration.getBoolean("hub_command")) registerCommand(new HubCommand());
        if (AxisConfiguration.getBoolean("join_command")) registerCommand(new JoinCommand());
        if (AxisConfiguration.getBoolean("staff_mode")) registerCommand(new StaffCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new ReportCommand());
        registerCommand(new RequestCommand());
        registerCommand(new BackCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new InvSeeCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new WorldCommand(), AxisUtils.PERMISSION + "world");
        registerCommand(new WhitelistCommand(), AxisUtils.PERMISSION + "wl");
        registerCommand(new PermissionCommand(), AxisUtils.PERMISSION + "permission");
        registerCommand(new SpyCommand(), AxisUtils.STAFF_PERMISSION);
        /*if (AxisConfiguration.getBoolean("teamspeak_api")) {
            registerCommand(new TeamCastCommand(), AxisUtils.PERMISSION + "teamcast");
            registerCommand(new TeamAuthCommand(), null);
            registerCommand(new LinkAccountCommand(), null);
        }*/
        if (AxisConfiguration.getBoolean("web_register")) registerCommand(new WebRegisterCommand());
        if (AxisConfiguration.getBoolean("nick_command")) {
            registerCommand(new NickCommand(), AxisUtils.PERMISSION + "nick");
            registerCommand(new RealNameCommand(), AxisUtils.PERMISSION + "realname");
        }
        if (AxisConfiguration.getBoolean("social_links")) registerCommand(new SocialCommand());

        //punishment
        registerCommand(new CheckCommand(), AxisUtils.STAFF_PERMISSION);

        registerCommand(new WarnCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new KickCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new TempMuteCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new MuteCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new TempBanCommand(), AxisUtils.STAFF_PERMISSION);
        registerCommand(new BanCommand(), AxisUtils.PERMISSION + "ban");
        registerCommand(new UnbanCommand(), AxisUtils.PERMISSION + "unban");
        registerCommand(new UnmuteCommand(), AxisUtils.PERMISSION + "unmute");
        registerCommand(new CheckbanCommand(), AxisUtils.PERMISSION + "checkban");
        registerCommand(new BlacklistCommand(), AxisUtils.PERMISSION + "blacklist");


        registerCommand(new DevCommand(), AxisUtils.PERMISSION + "dev");
    }

    private void registerCommand(AxisCommand axisCommand) {
        registerCommand(axisCommand, null);
    }

    private void registerCommand(AxisCommand axisCommand, String permission) {
        PluginCommand command = getCommand(axisCommand.getName(), javaPlugin);

        command.setPermissionMessage(AxisConfiguration.getString("no_permissions"));

        if (permission != null) {
            command.setPermission(permission.toLowerCase());
        }

        if (axisCommand.getDescription() != null) {
            command.setDescription(axisCommand.getDescription());
        }

        command.setAliases(Arrays.asList(axisCommand.getAliases()));

        command.setExecutor(axisCommand);
        command.setTabCompleter(axisCommand);

        if (!getCommandMap().register(axisCommand.getName(), command)) {
            command.unregister(getCommandMap());
            getCommandMap().register(axisCommand.getName(), command);
        }
    }

    private CommandMap getCommandMap() {
        if (commandMap != null) {
            return commandMap;
        }

        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);

            commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
        } catch (Exception ignored) {
        }

        return commandMap;
    }

    private PluginCommand getCommand(String name, Plugin owner) {
        PluginCommand command = null;

        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            command = constructor.newInstance(name, owner);
        } catch (Exception ignored) {
        }

        return command;
    }
}