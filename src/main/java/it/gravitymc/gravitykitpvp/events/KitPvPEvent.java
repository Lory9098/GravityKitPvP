package it.gravitymc.gravitykitpvp.events;

import lombok.Data;
import org.bukkit.entity.Player;

@Data
public abstract class KitPvPEvent {

    private final String name, kitName;

    public abstract void start(Player hoster);

}
