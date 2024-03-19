package it.gravitymc.gravitykitpvp.utils.build;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class Builders {

    private Set<UUID> players = new HashSet<>();

    public void addPlayer(UUID uuid) {
        players.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public boolean isBuilder(UUID uuid) {
        return players.contains(uuid);
    }

}
