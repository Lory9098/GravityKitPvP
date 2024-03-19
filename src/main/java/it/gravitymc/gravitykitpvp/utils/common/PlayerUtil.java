package it.gravitymc.gravitykitpvp.utils.common;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.location.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class PlayerUtil {
    public static void clearPlayer(Player player, boolean clearInventory, boolean forceKitGive) {
        if (clearInventory) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.getActivePotionEffects().clear();
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(LocationUtil.deserializeLocation(KitPvP.get().getConfig().getString("Spawn")));

        if (forceKitGive) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kit give base " + player.getName());
        } else {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    return;
                }
            }
            for (ItemStack item : player.getInventory().getArmorContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    return;
                }
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kit give base " + player.getName());
        }
    }
}