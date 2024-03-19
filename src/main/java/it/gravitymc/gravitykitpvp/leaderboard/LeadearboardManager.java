package it.gravitymc.gravitykitpvp.leaderboard;

import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;

public class LeadearboardManager {
    private final Map<String, List<String>> leaderboards = new HashMap<>();
    public static int index = 0;

    public LeadearboardManager() {
        loadLeaderboards();
    }

    private void loadLeaderboards() {
        if (KitPvP.get().getDatabaseManager() != null) {
            reloadLeaderboard("kills");
            reloadLeaderboard("deaths");
            reloadLeaderboard("maxStreak");
            reloadLeaderboard("goldenAppleEaten");
            reloadLeaderboard("playerElo");
            reloadLeaderboard("coins");
        }

        new BukkitRunnable() {
            public void run() {
                switch (LeadearboardManager.index++) {
                    case 1:
                        LeadearboardManager.this.reloadLeaderboard("kills");
                        break;
                    case 2:
                        LeadearboardManager.this.reloadLeaderboard("deaths");
                        break;
                    case 3:
                        LeadearboardManager.this.reloadLeaderboard("maxStreak");
                        break;
                    case 4:
                        LeadearboardManager.this.reloadLeaderboard("goldenAppleEaten");
                        break;
                    case 5:
                        LeadearboardManager.this.reloadLeaderboard("playerElo");
                        break;
                    case 6:
                        LeadearboardManager.this.reloadLeaderboard("coins");
                        LeadearboardManager.index = 0;
                        break;
                }

            }
        }.runTaskTimerAsynchronously(KitPvP.get(), 0L, 200L);
    }


    private void reloadLeaderboard(String type) {
        try {
            this.leaderboards.remove(type);
            AtomicInteger count = new AtomicInteger(1);
            this.leaderboards.putIfAbsent(type, new ArrayList<>());
            List<Document> documents = KitPvP.get().getDatabaseManager().getPlayers().find().limit(10).sort(new BasicDBObject(type, -1)).into(new ArrayList<>());
            int size = this.leaderboards.get(type).size();

            for (Document document : documents) {
                this.leaderboards.get(type).add(CC.translate("&7" + count.getAndIncrement() + ". &f" + document
                        .getString("name") + "&7 [" + document
                        .get(type) + "]"));
            }

            for (int i = size + 1; i <= 10; i++)
                this.leaderboards.get(type).add(CC.translate("&7" + i + ". " + ChatColor.WHITE + "Caricamento..."));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}