package it.gravitymc.gravitykitpvp.backend.data;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.thread.Tasks;
import lombok.Data;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bson.Document;
import org.bukkit.entity.Player;


@Data
public class PlayerData {
    private String name;
    private String realName;
    private UUID uuid;
    private int kills;
    private static Map<UUID, PlayerData> datas = new HashMap<>();
    private int killStreak;
    private int maxKillStreak;
    private int deaths;
    private int goldenHeadConsumed;
    private int playerBounty = 0;
    private double coins;

    public PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.realName = name;

        datas.put(this.uuid, this);

        load(true);
    }

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.name = (Bukkit.getPlayer(uuid) != null) ? Bukkit.getPlayer(uuid).getName() : Bukkit.getOfflinePlayer(uuid).getName();
        this.realName = this.name;

        datas.put(this.uuid, this);
    }

    public void load(boolean async) {
        if (async) {
            Tasks.runAsync(this::load);
        } else {
            load();
        }
    }

    private void load() {
        Document document = KitPvP.get().getDatabaseManager().getPlayer(this.uuid);
        System.out.println(document);
        if (document != null) {
            if (this.name == null) this.name = document.getString("name");
            this.kills = document.getInteger("kills");
            this.deaths = document.getInteger("deaths");
            this.coins = document.getDouble("coins");
            this.killStreak = document.getInteger("killStreak");
            this.maxKillStreak = document.getInteger("maxStreak");
            this.goldenHeadConsumed = document.getInteger("goldenAppleEaten");
            this.playerBounty = document.getInteger("playerElo");
        } else {
            save();
        }
    }

    public void save(boolean async) {
        if (async) {
            Tasks.runAsync(this::save);
        } else {
            save();
        }
    }

    private void save() {
        Document document = KitPvP.get().getDatabaseManager().getPlayer(this.uuid);
        if (document == null) document = new Document();
        document.put("uuid", this.uuid.toString());
        document.put("kills", Integer.valueOf(this.kills));
        document.put("deaths", Integer.valueOf(this.deaths));
        document.put("coins", Double.valueOf(this.coins));
        document.put("killStreak", Integer.valueOf(this.killStreak));
        document.put("maxStreak", Integer.valueOf(this.maxKillStreak));
        document.put("goldenAppleEaten", Integer.valueOf(this.goldenHeadConsumed));
        document.put("name", (this.realName != null) ? this.realName : this.name);
        document.put("playerElo", Integer.valueOf(this.playerBounty));
        KitPvP.get().getDatabaseManager().replacePlayer(this, document);
    }

    public void reset() {
        this.kills = 0;
        this.deaths = 0;
        this.coins = 0.0D;
        this.goldenHeadConsumed = 0;
        this.maxKillStreak = 0;
        this.playerBounty = 0;
        save();
    }

    public static PlayerData getByName(String name) {
        return getByUuid((Bukkit.getPlayer(name) == null) ? Bukkit.getOfflinePlayer(name).getUniqueId() : Bukkit.getPlayer(name).getUniqueId());
    }

    public static PlayerData getByUuid(UUID uuid) {
        PlayerData data = datas.get(uuid);

        if (data == null) {
            data = new PlayerData(uuid);
            data.load(true);
        }


        return data;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public double getKdr() {
        return (this.kills > 0 && this.deaths == 0) ? this.kills : (
                (this.kills == 0 && this.deaths == 0) ? 0.0D : (
                        (double) this.kills / this.deaths));
    }

    public double getKdr(Document document) {
        return (document.getInteger("kills") > 0 && document.getInteger("deaths") == 0) ? document.getInteger("kills").intValue() : (
                (document.getInteger("kills").intValue() == 0 && document.getInteger("deaths").intValue() == 0) ? 0.0D : (
                        (double) document.getInteger("kills").intValue() / document.getInteger("deaths").intValue()));
    }
}