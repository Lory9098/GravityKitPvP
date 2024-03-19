package it.gravitymc.gravitykitpvp.ability.actions;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.ability.AbstractAbility;
import it.gravitymc.gravitykitpvp.ability.custom.CustomAbility;
import it.gravitymc.gravitykitpvp.ability.type.AbilityType;
import it.gravitymc.gravitykitpvp.utils.build.AntiBuild;
import it.gravitymc.gravitykitpvp.utils.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


public class AbilityAction {
    private CustomAbility customAbility;
    private AbilityType abilityType;
    private AbstractAbility ability;

    public AbilityAction(AbstractAbility ability, CustomAbility customAbility, AbilityType abilityType) {
        this.customAbility = customAbility;
        this.abilityType = abilityType;
        this.ability = ability;
    }

    public void execute(Player player, Player target) {
        System.out.println("execute");
        int jumpHeight, tickInterval;
        if (player == null)
            return;
        if (this.abilityType.equals(AbilityType.TARGET) && target == null)
            return;
        final Player targetPlayer = this.abilityType.equals(AbilityType.TARGET) ? target : player;
        System.out.println(targetPlayer);

        this.ability.getLastUsage().put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));

        switch (this.customAbility) {
            case LEVITATION:
                jumpHeight = 70;

                tickInterval = 1;

                (new BukkitRunnable() {
                    int currentHeight = 0;


                    public void run() {
                        targetPlayer.teleport(targetPlayer.getLocation().add(0.0D, 0.1D, 0.0D));
                        this.currentHeight++;

                        if (this.currentHeight >= 70) {
                            cancel();
                        }
                    }
                }).runTaskTimer((Plugin) KitPvP.get(), 0L, 1L);
                break;
            case ANTI_BUILD:
                AntiBuild.addPlayer(targetPlayer.getUniqueId(), ability.getDuration());
                break;
            case ROCKET:
                targetPlayer.setVelocity(targetPlayer.getLocation().getDirection().multiply(2.0D).setY(1.0D));
                break;
            case BLINDNESS:
                targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, this.ability.getDuration() * 20, 1));
                break;
            case PARALYZED:
                targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, this.ability.getDuration() * 20, 100));
                break;
            case GIVE:
                ItemStack itemStack = ability.getItem();
                if (itemStack != null) {
                    targetPlayer.getInventory().addItem(itemStack);
                }
                break;
        }
        if (ability.getSound() != null) {
            player.playSound(player.getLocation(), ability.getSound(), 1.0F, 1.0F);
        }
        if (ability.getMessage() != null) {
            player.sendMessage(ability.getMessage());
        }
    }
}