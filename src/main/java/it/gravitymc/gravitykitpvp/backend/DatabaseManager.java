package it.gravitymc.gravitykitpvp.backend;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import it.gravitymc.gravitykitpvp.utils.config.ConfigFile;
import lombok.Data;
import org.bson.Document;

@Data
public class DatabaseManager {
    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> players;


    public DatabaseManager() {
        ConfigFile config = KitPvP.get().getConfig();

        if (config.getBoolean("Mongo.Authentication.Enabled")) {
            MongoCredential credential = MongoCredential.createCredential(config
                    .getString("Mongo.Authentication.Username"), config
                    .getString("Mongo.Database"), config
                    .getString("Mongo.Authentication.Password").toCharArray());

            this.client = new MongoClient(new ServerAddress(config.getString("Mongo.Host"), config.getInt("Mongo.Port")), List.of(credential));
        } else {
            this.client = new MongoClient(new ServerAddress(config.getString("Mongo.Host"), config.getInt("Mongo.Port")));
        }

        this.database = this.client.getDatabase(config.getString("Mongo.Database"));
        this.players = this.database.getCollection("players");
    }


    public Document getPlayer(UUID uuid) {
        return this.players.find(Filters.eq("uuid", uuid.toString())).first();
    }

    public void replacePlayer(PlayerData data, Document document) {
        this.players.replaceOne(Filters.eq("uuid", data.getUuid().toString()), document, (new ReplaceOptions())
                .upsert(true));
    }


    public MongoCollection<Document> getPlayers() {
        return this.players;
    }

    public void insertPlayer(Document document) {
        this.players.insertOne(document);
    }

    public Serializable getStatus(String table, String where, String what, Object value, Serializable type) {
        Document document = (Document) KitPvP.get().getDatabaseManager().getDatabase().getCollection(table).find(Filters.eq(where, what)).first();

        if (document != null) {
            if (type == Integer.class)
                return document.getInteger(value.toString());
            if (type == String.class)
                return document.getString(value.toString());
            if (type == Double.class)
                return document.getDouble(value.toString());
            if (type == Long.class)
                return document.getLong(value.toString());
            if (type == Boolean.class) {
                return document.getBoolean(value.toString());
            }
        }

        if (type == Integer.class)
            return Integer.valueOf(-1);
        if (type == String.class)
            return null;
        if (type == Float.class)
            return Float.valueOf(-1.0F);
        if (type == Double.class)
            return Double.valueOf(-1.0D);
        if (type == Long.class)
            return Long.valueOf(-1L);
        if (type == Boolean.class) {
            return Boolean.valueOf(false);
        }

        return null;
    }
}