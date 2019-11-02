package me.tinchx.axis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import me.tinchx.axis.commands.staff.whitelist.Whitelist;
import me.tinchx.axis.configuration.Config;
import me.tinchx.axis.cooldown.Cooldown;
import me.tinchx.axis.cosmetic.Tag;
import me.tinchx.axis.database.AxisDatabase;
import me.tinchx.axis.inventory.MakerListener;
import me.tinchx.axis.listeners.*;
import me.tinchx.axis.profile.Profile;
import me.tinchx.axis.profile.ProfileListener;
import me.tinchx.axis.rank.Rank;
import me.tinchx.axis.scoreboard.AridiManager;
import me.tinchx.axis.scoreboard.listeners.AridiListener;
import me.tinchx.axis.task.AnnounceTask;
import me.tinchx.axis.utilities.AxisUtils;
import me.tinchx.axis.utilities.command.AxisCache;
import me.tinchx.axis.utilities.item.ItemMaker;
import me.tinchx.axis.utilities.task.TaskUtil;
import me.tinchx.axis.utilities.time.TimeUtils;
import me.tinchx.sponsor.SponsorPlugin;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

@Getter
public class AxisPlugin extends JavaPlugin {

    @Getter
    private static AxisPlugin plugin;
    private Type LIST_STRING_TYPE = new TypeToken<List<String>>() {
    }.getType();
    private Gson gson = new Gson();
    private AxisDatabase axisDatabase;
    private boolean loaded;

    private JedisPool jedisPool;
    private Chat chat;
    @Setter
    private AridiManager aridiManager;

    /*private TS3Config ts3Config = new TS3Config();
    private TS3Query ts3Query = new TS3Query(ts3Config);
    private TS3Api ts3Api;*/

    @Override
    public void onEnable() {
        load();
    }

    @Override
    public void onDisable() {
        /*if (AxisConfiguration.getBoolean("teamspeak_api")) {
            try {

                ts3Query.exit();

                System.out.println("[Bot] " + AxisConfiguration.getString("teamspeak_nickname") + " was successfully disconnected.");
            } catch (Exception ex) {
                getLogger().warning(ex.getMessage());
            }
        }*/
        SponsorPlugin.getInstance().getRedisPublisher().write("serverstop;" + Bukkit.getServerName());
        //redisPublisher.write("serverstop;" + Bukkit.getServerName());
        axisDatabase.getClient().close();
    }

    private void load() {
        plugin = this;

        Config config = new Config(this, "settings.yml");

        AxisConfiguration.setUpData(config);

        /*if (AxisConfiguration.getBoolean("teamspeak_api")) {

            try {
                ts3Config.setHost(AxisConfiguration.getString("teamspeak_host"));
                //ts3Config.setDebugLevel(Level.ALL);

                ts3Query.connect();

                ts3Api = ts3Query.getApi();
                ts3Api.selectVirtualServerByPort(AxisConfiguration.getInteger("teamspeak_port"));
                ts3Api.login(AxisConfiguration.getString("teamspeak_username"), AxisConfiguration.getString("teamspeak_password"));
                ts3Api.setNickname(AxisConfiguration.getString("teamspeak_nickname"));

                new TeamSpeakListener(ts3Api);
                System.out.println("[Bot] " + AxisConfiguration.getString("teamspeak_nickname") + " was successfully connected.");
            } catch (Exception ex) {
                getLogger().warning(ex.getMessage());
            }

        }*/

        //if (!new AdvancedLicense(AxisConfiguration.getString("key"), "http://licensesmc.000webhostapp.com/verify.php", this).debug().register()) return;

        if (AxisConfiguration.getBoolean("announce_enabled")) {
            new AnnounceTask();
        }

        this.jedisPool = new JedisPool(config.getString("database.redis.host"), 6379);


        new AxisCache(this);
        this.axisDatabase = new AxisDatabase(this);

        if (AxisConfiguration.getBoolean("ranks")) {
            Rank.loadCache();
            new Config(this, "ranks.yml");
        } else {
            RegisteredServiceProvider<Chat> serviceProvider = getServer().getServicesManager().getRegistration(Chat.class);
            if (serviceProvider != null) chat = serviceProvider.getProvider();
        }

        if (AxisConfiguration.getBoolean("cosmetics")) Tag.loadTags();

        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            Profile profile = new Profile(online.getUniqueId());

            profile.setAddress(online.getAddress().getAddress().getHostAddress());
            profile.setLastJoin(System.currentTimeMillis());
            profile.setPlayerName(online.getName());

            profile.updateProfile();
        }

        registerListeners();

        new Cooldown("report", TimeUtils.parse("1m"));
        new Cooldown("request", TimeUtils.parse("1m"));

        TaskUtil.runTaskTimer(() -> {
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                Profile profile = AxisAPI.getProfile(online);
                if (profile.isFrozen()) {
                    online.sendMessage(AxisConfiguration.getString("freeze_message"));
                }
            }
        }, 0L, 30 * 20L);

        TaskUtil.runTaskLater(() -> {
            if (aridiManager != null) {
                TaskUtil.runTaskTimer(aridiManager::sendScoreboard, 0L, 2L);
            }
        }, 2 * 20L);

        TaskUtil.runTaskLater(() -> loaded = true, 3 * 20L);

        Whitelist.run(this);

        removeRecipe(Material.WRITTEN_BOOK);

        SponsorPlugin.getInstance().getRedisPublisher().write("serverstart;" + Bukkit.getServerName());

        TaskUtil.runTaskTimer(() -> SponsorPlugin.getInstance().getRedisPublisher().write("serverprofile;" + Bukkit.getServerName() + ';' + Bukkit.getOnlinePlayers().size() + ';' + Bukkit.getMaxPlayers() + ';' + AxisUtils.getOnlineStaff().size() + ';' + AxisUtils.getOnlineOperators().size() + ';' + Bukkit.hasWhitelist()), 0L, 60L * 20L);
    }

    public void removeRecipe(Material... materials) {
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        while (recipeIterator.hasNext()) {
            for (Material material : materials) {
                if (recipeIterator.next().getResult().getType() == material) {
                    recipeIterator.remove();
                }
            }
        }
    }

    private void registerListeners() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new ChatListener(), this);
        manager.registerEvents(new ProfileListener(), this);
        manager.registerEvents(new HungerFixListener(), this);
        manager.registerEvents(new CoreListener(), this);
        manager.registerEvents(new StaffListener(), this);
        manager.registerEvents(new PacketListener(), this);
        manager.registerEvents(new MakerListener(), this);
        manager.registerEvents(new ItemMaker.ItemMakerListener(), this);
        TaskUtil.runTaskLater(() -> {
            if (aridiManager != null) {
                manager.registerEvents(new AridiListener(), this);

            }
        }, 2 * 20L);
    }

}