package it.gravitymc.gravitykitpvp.ability;

import com.google.common.base.Enums;
import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.ability.actions.AbilityAction;
import it.gravitymc.gravitykitpvp.ability.custom.CustomAbility;
import it.gravitymc.gravitykitpvp.ability.type.AbilityType;
import it.gravitymc.gravitykitpvp.utils.item.ItemBuilder;

import java.util.*;

import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Data
public abstract class AbstractAbility {
    private final ItemStack item;
    private final String name;
    private final int ticksCooldown;
    private final int duration;
    private final int damage;
    private final List<PotionEffect> effects;
    private final HashMap<CustomAbility, List<AbilityAction>> abilityActions = new HashMap<>();
    private HashMap<UUID, Long> lastUsage = new HashMap<>();
    private final Sound sound;
    private final String message;

    public AbstractAbility(ConfigurationSection section) {
        this.item = parseItemStack(section);
        this.ticksCooldown = section.getInt("Cooldown") * 1000;
        this.duration = section.getInt("Duration");
        this.damage = section.getInt("Damage");
        this.name = section.getString("Name");
        this.effects = parsePotionEffects(section.getStringList("Effects"));
        this.sound = (Sound) Enums.getIfPresent(Sound.class, section.getString("Sound")).orNull();

        for (String action : section.getStringList("Actions")) {
            String[] splitted = action.split(" ");
            CustomAbility customAbility = (CustomAbility) Enums.getIfPresent(CustomAbility.class, splitted[0].replace("[", "").replace("]", "").toUpperCase()).orNull();
            if (customAbility == null)
                continue;
            AbilityAction abilityAction = new AbilityAction(this, customAbility, (AbilityType) Enums.getIfPresent(AbilityType.class, splitted[1].toUpperCase()).orNull());

            List<AbilityAction> actions = this.abilityActions.get(customAbility);
            if (actions == null) {
                actions = new ArrayList<>();
            }
            actions.add(abilityAction);
            this.abilityActions.put(customAbility, actions);
        }

        this.message = CC.translate(section.getString("Message"));
    }


    private ItemStack parseItemStack(ConfigurationSection section) {
        return (new ItemBuilder(Material.valueOf(section.getString("Material"))))
                .setName(CC.translate(section.getString("Name")))
                .setLore(CC.translateStrings(section.getStringList("Description")))
                .get();
    }

    private List<PotionEffect> parsePotionEffects(List<String> effects) {
        List<PotionEffect> potionEffects = new ArrayList<>();

        for (String str : effects) {
            String[] splitted = str.split(":");


            potionEffects.add(new PotionEffect(

                    PotionEffectType.getByName(splitted[0]),
                    Integer.parseInt(splitted[1]),
                    Integer.parseInt(splitted[2])));
        }


        return potionEffects;
    }

    public void execute(Player player, Player target) {
        if (this.sound != null) player.playSound(player.getLocation(), this.sound, 1.0F, 1.0F);
        if (this.message != null) player.sendMessage(this.message);
        if (this.effects != null) {
            Objects.requireNonNull(target);
            this.effects.forEach(target::addPotionEffect);
        }
        if (this.damage != 0) target.damage(this.damage);
        if (this.lastUsage.containsKey(player.getUniqueId()) && System.currentTimeMillis() - ((Long) this.lastUsage.get(player.getUniqueId())).longValue() < this.ticksCooldown) {
            player.sendMessage(CC.translate(
                    KitPvP.get().getMessages().getString("Abilities.Cooldown")
                            .replace("{time}", String.valueOf((this.ticksCooldown - System.currentTimeMillis() - ((Long) this.lastUsage.get(player.getUniqueId())).longValue()) / 1000L))));

            return;
        }

        for (List<AbilityAction> abilityActions : getAbilityActions().values()) {
            abilityActions.forEach(abilityAction -> abilityAction.execute(player, target));
        }
        this.lastUsage.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
    }
}