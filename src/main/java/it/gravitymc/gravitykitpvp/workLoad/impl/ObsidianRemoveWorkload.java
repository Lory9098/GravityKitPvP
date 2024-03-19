package it.gravitymc.gravitykitpvp.workLoad.impl;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.workLoad.Workload;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

@RequiredArgsConstructor
public class ObsidianRemoveWorkload implements Workload {
    private final Location location;

    @Override
    public void compute() {
        Block block = location.getBlock();

        Bukkit.getScheduler().runTaskLater(KitPvP.get(), () -> {
            if(block.getType().equals(Material.OBSIDIAN)) block.setType(Material.AIR);
        }, 500L);
    }
}
