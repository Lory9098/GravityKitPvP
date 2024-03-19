package it.gravitymc.gravitykitpvp.listener;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.build.AntiBuild;
import it.gravitymc.gravitykitpvp.utils.build.Builders;
import it.gravitymc.gravitykitpvp.workLoad.impl.CobwebPlaceWorkload;
import it.gravitymc.gravitykitpvp.workLoad.impl.ObsidianRemoveWorkload;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

public class AbilitiesListener implements Listener {
    public AbilitiesListener(KitPvP instance) {
        this.instance = instance;
    }

    private final KitPvP instance;

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInHand();

        if (itemStack == null) {
            return;
        }

        this.instance.getAbilityManager().getAbility(itemStack)
                .ifPresent(ability -> ability.execute(event.getPlayer(), (event.getRightClicked() instanceof Player) ? (Player) event.getRightClicked() : null));
    }

    @EventHandler
    public void onBlockBreak(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if (Builders.isBuilder(player.getUniqueId())) {
            return;
        }

        if (!DataListener.getDownPlayers().contains(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack itemStack = player.getItemInHand();

        if (Builders.isBuilder(player.getUniqueId())) {
            return;
        }

        if (!DataListener.getDownPlayers().contains(player.getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        if (itemStack == null) {
            return;
        }
        if (itemStack.getType() != Material.WEB && itemStack.getType() != Material.OBSIDIAN) {
            return;
        }

        if (!AntiBuild.isExpired(player.getUniqueId())) {
            e.setCancelled(true);
        }

        if (e.getBlockPlaced().getType().equals(Material.WEB)) {
            instance.getWorkloadThread().addWorkload(new CobwebPlaceWorkload(e.getBlockPlaced().getLocation()));
        } else if (e.getBlockPlaced().getType().equals(Material.OBSIDIAN)) {
            instance.getWorkloadThread().addWorkload(new ObsidianRemoveWorkload(e.getBlockPlaced().getLocation()));
        }
    }

}