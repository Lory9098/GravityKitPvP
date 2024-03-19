package it.gravitymc.gravitykitpvp.ability.manager;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.ability.effective.Ability;
import org.bukkit.inventory.ItemStack;

public class AbilityManager {
    private HashMap<ItemStack, Ability> abilities = new HashMap<>();

    public AbilityManager(KitPvP instance) {
        for (String key : instance.getAbilities().getConfigurationSection("Abilities").getKeys(false)) {
            System.out.println(key);
            Ability ability = new Ability(instance.getAbilities().getConfigurationSection("Abilities." + key));
            this.abilities.put(ability
                    .getItem(), ability);
        }
    }


    public void registerAbility(ItemStack itemStack, Ability ability) {
        this.abilities.put(itemStack, ability);
    }

    public Optional<Ability> getAbility(ItemStack itemStack) {
        return this.abilities.keySet().stream().filter(item -> compare(itemStack, item)).findFirst().map(this.abilities::get);
    }

    public Optional<Ability> getAbility(String name) {
        System.out.println(abilities);
        return this.abilities.values().stream().filter(ability -> ability.getName().equalsIgnoreCase(name)).findFirst();
    }

    private boolean compare(ItemStack itemStack, ItemStack itemStack2) {
        return (itemStack2.getType().equals(itemStack.getType()) && itemStack2.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName()) && itemStack2.getItemMeta().getLore().equals(itemStack.getItemMeta().getLore()) && itemStack2.getItemMeta().getEnchants().equals(itemStack.getItemMeta().getEnchants()));
    }
}