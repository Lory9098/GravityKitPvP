package it.gravitymc.gravitykitpvp.listener;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import it.gravitymc.gravitykitpvp.utils.common.PlayerUtil;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.nettychannell.api.GangsAPI;
import me.nettychannell.api.gang.IGang;
import me.nettychannell.api.manager.IGangManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DataListener implements Listener {

  @Getter
  private static List<UUID> downPlayers = new ArrayList<>();

  @EventHandler
  public void onPlayerDeath(EntityDamageEvent event) {
    Player player = (Player) event.getEntity();
    if (event.getCause() == EntityDamageEvent.DamageCause.FALL && player.getFallDistance() >= 60) {
      event.setCancelled(true);
      downPlayers.add(player.getUniqueId());
      return;
    }

    if (!downPlayers.contains(player.getUniqueId())) {
      event.setCancelled(true);
      return;
    }

    if (player.getHealth() - event.getFinalDamage() > 0.0D) {
      return;
    }
    player.spigot().respawn();
    Player killer = player.getKiller();

    PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
    playerData.setDeaths(playerData.getDeaths() + 1);
    if (killer != null) {
      PlayerData killerData = PlayerData.getByUuid(killer.getUniqueId());
      killerData.setCoins(killerData.getCoins() + playerData.getPlayerBounty());
    }
    playerData.setPlayerBounty(0);

    KitPvP.get().getCombatLogManager().getCombatTime().remove(player.getUniqueId());

    playerData.setKillStreak(0);
    playerData.save(true);

    downPlayers.remove(player.getUniqueId());

    IGangManager gangManager = GangsAPI.getInstance().getGangManager();

    IGang targetGang = gangManager.getGangByPlayer(player.getUniqueId());

    if (targetGang != null) {
      targetGang.setDeaths(targetGang.getDeaths() + 1);
    }

    if (killer != null) {
      PlayerData killerData = PlayerData.getByUuid(killer.getUniqueId());

      if (killerData.getKillStreak() % 20 == 0) {
        Bukkit.broadcastMessage(
                PlaceholderAPI.setPlaceholders(
                        killer,
                        CC.translate(
                                KitPvP.get().getMessages().getString("StreakMessage")
                                        .replace("{player}", killer.getName())
                                        .replace("{streak}", String.valueOf(killerData.getKillStreak()))
                        )
                )
        );
      }

      IGang killerGang = gangManager.getGangByPlayer(killer.getUniqueId());

      if (killerGang != null) {
        killerGang.setKills(killerGang.getKills() + 1);
      }

      killer.sendMessage(
              PlaceholderAPI.setPlaceholders(
                        killer,
                        CC.translate(
                                KitPvP.get().getMessages().getString("DeathMessages.KillerMessage")
                                        .replace("{target}", player.getName())
                        )
              )
      );
      killerData.setKills(killerData.getKills() + 1);
      killerData.setKillStreak(killerData.getKillStreak() + 1);

      Random random = new Random();
      int percentage = random.nextInt(100);
      if (percentage <= 40) {
        int elo = random.nextInt(10000 - 1000) + 1000;

        killerData.setPlayerBounty(killerData.getPlayerBounty() + elo);
      }

      if (killerData.getKillStreak() > killerData.getMaxKillStreak())
        killerData.setMaxKillStreak(killerData.getKillStreak());
      killerData.save(true);
      killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 8, 4));
    } else {
      Bukkit.broadcastMessage(
              PlaceholderAPI.setPlaceholders(
                      player,
                      CC.translate(
                              KitPvP.get().getMessages().getString("DeathMessages.UnknownMessage")
                                      .replace("{player}", player.getName())
                      )
              )
      );
      return;
    }
    player.sendMessage(
            PlaceholderAPI.setPlaceholders(
                    player,
                    CC.translate(
                            KitPvP.get().getMessages().getString("DeathMessages.VictimMessageKiller")
                                    .replace("{killer}", killer.getName())
                    ).replace("{killer}", killer.getName())
            )
    );
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
    PlayerUtil.clearPlayer(player, true, true);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onJoinData(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    player.setHealth(20.0D);
    player.setFoodLevel(20);
    new PlayerData(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    if (KitPvP.get().getMessages().getBoolean("Join.Enabled")) {
      event.setJoinMessage(CC.translate(event.getPlayer(), KitPvP.get().getMessages().getString("Join.Format")));
    } else {
      event.setJoinMessage(null);
    }
    player.addPotionEffect(
            new PotionEffect(
                    PotionEffectType.SPEED,
                    Integer.MAX_VALUE,
                    1
            ),
            true
    );
    player.addPotionEffect(
            new PotionEffect(
                    PotionEffectType.INCREASE_DAMAGE,
                    Integer.MAX_VALUE,
                    2
            ),
            true
    );
    downPlayers.remove(player.getUniqueId());
    PlayerUtil.clearPlayer(player, false, false);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    PlayerData playerData = PlayerData.getByUuid(event.getPlayer().getUniqueId());
    playerData.save(true);
    downPlayers.remove(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void ItemConsume(PlayerItemConsumeEvent event) {
    if (event.getItem().getType() == Material.GOLDEN_APPLE) {
      Player player = event.getPlayer();
      PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
      playerData.setGoldenHeadConsumed(playerData.getGoldenHeadConsumed() + 1);
      playerData.save(true);
    }
  }

  @EventHandler
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Player player) || !(event.getDamager() instanceof Player damager)) {
      return;
    }

    System.out.println(downPlayers);

    if (!downPlayers.contains(player.getUniqueId())) {
      event.setCancelled(true);
      return;
    }

    if (damager.getInventory().getItemInHand().getType().name().contains("AXE")) {
      player.getInventory().getBoots().setDurability((short) (player.getInventory().getBoots().getDurability() - 2));
    }
  }

}