package it.gravitymc.gravitykitpvp.provider;

import it.gravitymc.gravitykitpvp.KitPvP;
import org.bukkit.plugin.Plugin;

public class AdapterManager {
    public AdapterManager() {
        KitPvPBoard kitPvPBoard = new KitPvPBoard();
        kitPvPBoard.runTaskTimer((Plugin) KitPvP.get(), 0L, 1L);
    }
}