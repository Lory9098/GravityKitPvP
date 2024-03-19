package it.gravitymc.gravitykitpvp.utils.cooldown;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;




public class CooldownManager {
    private final HashMap<UUID, Integer> cooldownMap = new HashMap<>();


    public void putCooldown(UUID uuid, Integer seconds) {
        this.cooldownMap.put(uuid, Integer.valueOf((int) (Instant.now().getEpochSecond() + seconds.intValue())));
    }

    public void remove(UUID uuid) {
        this.cooldownMap.remove(uuid);
    }

    public boolean isInCooldown(UUID uuid) {
        if (!this.cooldownMap.containsKey(uuid)) {
            return false;
        }
        return (((Integer) this.cooldownMap.getOrDefault(uuid, Integer.valueOf(0))).intValue() > (int) Instant.now().getEpochSecond());
    }

    public double getRemainingSeconds(UUID uuid) {
        if (!this.cooldownMap.containsKey(uuid)) {
            return 0.0D;
        }
        return Math.max(0L, ((Integer) this.cooldownMap.getOrDefault(uuid, Integer.valueOf(0))).intValue() - Instant.now().getEpochSecond());
    }
}