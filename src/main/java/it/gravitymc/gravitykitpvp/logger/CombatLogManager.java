package it.gravitymc.gravitykitpvp.logger;

import it.gravitymc.gravitykitpvp.KitPvP;

import it.gravitymc.gravitykitpvp.logger.CombatListener;
import it.gravitymc.gravitykitpvp.utils.cooldown.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class CombatLogManager {
    private final CooldownManager combatTime = new CooldownManager();

    public CombatLogManager() {
        Bukkit.getPluginManager().registerEvents(new CombatListener(), (Plugin) KitPvP.get());
    }

    public CooldownManager getCombatTime() {
        return this.combatTime;
    }

    public boolean combatTagged(Player player) {
        return this.combatTime.isInCooldown(player.getUniqueId());
    }
}