package it.gravitymc.gravitykitpvp.utils.bar;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.nms.Versioning;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarManager {
    public static void sendActionBar(Player player, String msg) {
        if (msg == null) {
            msg = "";
        }
        if (Bukkit.getVersion().equalsIgnoreCase("v1_7_R4.")) {
            return;
        }
        NMS_ActionBar m = new NMS_ActionBar(KitPvP.get());
        m.sendActionBar(player, msg);
    }

    public static void sendActionBar(Player player, String msg, int duration) {
        if (msg == null) {
            msg = "";
        }
        if (Versioning.getServerVersion().equalsIgnoreCase("v1_7_R4.")) {
            return;
        }
        NMS_ActionBar m = new NMS_ActionBar(KitPvP.get());
        m.sendActionBar(player, msg, duration);
    }
}