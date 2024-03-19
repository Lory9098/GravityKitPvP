package it.gravitymc.gravitykitpvp.events.juggernaut;

import it.gravitymc.gravitykitpvp.events.KitPvPEvent;

import org.bukkit.entity.Player;

import java.util.UUID;

public class JuggerNautEvent extends KitPvPEvent {
    public JuggerNautEvent(String name, String kitName) {
        super(name, kitName);
    }


    @Override
    public void start(Player hoster) {
        hoster.performCommand("kits give " + hoster.getName() + " " + getKitName());
    }
}
