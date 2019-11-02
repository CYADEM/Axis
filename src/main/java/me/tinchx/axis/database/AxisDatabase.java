package me.tinchx.axis.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.tinchx.axis.configuration.Config;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class AxisDatabase {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection profiles, ranks, punishments, tags;

    public AxisDatabase(JavaPlugin javaPlugin) {
        Config configuration = new Config(javaPlugin, "settings.yml");
        if (configuration.getBoolean("database.mongo.authentication.enabled")) {
            ServerAddress serverAddress = new ServerAddress(configuration.getString("database.mongo.host"),
                    configuration.getInt("database.mongo.port"));

            MongoCredential credential = MongoCredential.createCredential(
                    configuration.getString("database.mongo.authentication.user"), "admin",
                    configuration.getString("database.mongo.authentication.password").toCharArray());

            client = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build());
        } else {
            client = new MongoClient(configuration.getString("database.mongo.host"), configuration.getInt("database.mongo.port"));
        }
        this.database = client.getDatabase("axis");
        this.profiles = this.database.getCollection("profiles");
        this.ranks = this.database.getCollection("ranks");
        this.punishments = this.database.getCollection("punishments");
        this.tags = this.database.getCollection("tags");
    }

}