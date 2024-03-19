package it.gravitymc.gravitykitpvp.logger;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import it.gravitymc.gravitykitpvp.listener.DataListener;
import it.gravitymc.gravitykitpvp.utils.bar.ActionBarManager;
import it.gravitymc.gravitykitpvp.utils.common.PlayerUtil;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import javax.xml.crypto.Data;

public class CombatListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            Entity entity1 = event.getEntity();
            if (entity1 instanceof Player) {
                if (!DataListener.getDownPlayers().contains(player.getUniqueId()) || !DataListener.getDownPlayers().contains(entity1.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
                Player target = (Player) entity1;

                if (!player.hasPermission("kitpvp.combat.bypass")) {
                    KitPvP.get().getCombatLogManager().getCombatTime().putCooldown(player.getUniqueId(), Integer.valueOf(15));
                    checkQueryCombat(player);
                }

                if (!target.hasPermission("kitpvp.combat.bypass")) {
                    KitPvP.get().getCombatLogManager().getCombatTime().putCooldown(target.getUniqueId(), Integer.valueOf(15));
                    checkQueryCombat(target);
                }
            }
        }


        entity = event.getDamager();
        if (entity instanceof Arrow) {
            Arrow arrow = (Arrow) entity;
            ProjectileSource projectileSource = arrow.getShooter();
            if (projectileSource instanceof Player) {
                Player target = (Player) projectileSource;
                Entity entity1 = event.getEntity();
                if (entity1 instanceof Player) {
                    Player player = (Player) entity1;

                    if (!player.hasPermission("kitpvp.combat.bypass")) {
                        KitPvP.get().getCombatLogManager().getCombatTime().putCooldown(player.getUniqueId(), Integer.valueOf(15));
                        checkQueryCombat(player);
                    }

                    if (!target.hasPermission("kitpvp.combat.bypass")) {
                        KitPvP.get().getCombatLogManager().getCombatTime().putCooldown(target.getUniqueId(), Integer.valueOf(15));
                        checkQueryCombat(target);
                    }
                }
            }
        }


        entity = event.getDamager();
        if (entity instanceof FishHook) {
            FishHook hook = (FishHook) entity;
            ProjectileSource projectileSource = hook.getShooter();
            if (projectileSource instanceof Player) {
                Player target = (Player) projectileSource;
                Entity entity1 = event.getEntity();
                if (entity1 instanceof Player) {
                    Player player = (Player) entity1;

                    if (!player.hasPermission("kitpvp.combat.bypass")) {
                        KitPvP.get().getCombatLogManager().getCombatTime().putCooldown(player.getUniqueId(), Integer.valueOf(15));
                        checkQueryCombat(player);
                    }

                    if (!target.hasPermission("kitpvp.combat.bypass")) {
                        KitPvP.get().getCombatLogManager().getCombatTime().putCooldown(target.getUniqueId(), Integer.valueOf(15));
                        checkQueryCombat(target);
                    }
                }
            }
        }

    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        DataListener.getDownPlayers().remove(player.getUniqueId());
        if (KitPvP.get().getCombatLogManager().getCombatTime().isInCooldown(player.getUniqueId())) {
            KitPvP.get().getServer().getScheduler().scheduleSyncDelayedTask((Plugin) KitPvP.get(), () -> {
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.getType() != Material.AIR) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
                for (ItemStack item : player.getInventory().getArmorContents()) {
                    if (item != null && item.getType() != Material.AIR) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
                PlayerUtil.clearPlayer(player, true, false);
                player.damage(9999.0D);
                player.spigot().respawn();
                KitPvP.get().getCombatLogManager().getCombatTime().remove(player.getUniqueId());
                PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
                playerData.setDeaths(playerData.getDeaths() + 1);
                playerData.save(true);
            }, 1L);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExecuteCommand(PlayerCommandPreprocessEvent event) {
        if (KitPvP.get().getCombatLogManager().getCombatTime().isInCooldown(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.translate("&cNon puoi eseguire questo comando mentre sei in combattimento!"));
        }
    }


    public void checkQueryCombat(final Player player) {
        (new BukkitRunnable() {
            public void run() {
                if (!KitPvP.get().getCombatLogManager().getCombatTime().isInCooldown(player.getUniqueId())) cancel();
                ActionBarManager.sendActionBar(player, CC.translate("&cIn Combattimento per &l" + KitPvP.get().getCombatLogManager().getCombatTime().getRemainingSeconds(player.getUniqueId())));
            }
        }).runTaskTimerAsynchronously((Plugin) KitPvP.get(), 0L, 20L);
    }
}