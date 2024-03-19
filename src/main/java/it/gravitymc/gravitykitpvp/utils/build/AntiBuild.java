package it.gravitymc.gravitykitpvp.utils.build;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class AntiBuild {

    private HashMap<UUID, Long> players = new HashMap<>();

    public void addPlayer(UUID uuid, int duration) {
        players.put(uuid, System.currentTimeMillis() + (duration * 1000L));
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public boolean isPlayer(UUID uuid) {
        return players.containsKey(uuid);
    }

    public boolean isExpired(UUID uuid) {
        return !(players.containsKey(uuid)) || System.currentTimeMillis() >= players.get(uuid);
    }

}
