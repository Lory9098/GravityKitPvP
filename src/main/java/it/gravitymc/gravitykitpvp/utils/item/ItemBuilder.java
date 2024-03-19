package it.gravitymc.gravitykitpvp.utils.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import it.gravitymc.gravitykitpvp.utils.xseries.XMaterial;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

public class ItemBuilder {
    public ItemBuilder(ItemStack itemStack, ItemMeta itemMeta) {
        this.itemStack = itemStack;
        this.itemMeta = itemMeta;
    }

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public static ItemBuilder copyOf(ItemBuilder builder) {
        return new ItemBuilder(builder.get());
    }

    public static ItemBuilder copyOf(ItemStack item) {
        return new ItemBuilder(item);
    }

    public ItemBuilder(Material material, int amount, short damage) {
        this.itemStack = new ItemStack(material, amount, damage);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(material, 1, (short) 0);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(Math.min(amount, 64));
        return this;
    }

    public ItemBuilder setFlags(ItemFlag... flags) {
        for (ItemFlag flag : flags) {
            this.itemMeta.addItemFlags(flags);
        }
        return this;
    }

    public ItemBuilder setName(String name) {
        this.itemMeta.setDisplayName(CC.translate(name));
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        List<String> newLore = (this.itemMeta.getLore() == null) ? new ArrayList<>() : this.itemMeta.getLore();
        newLore.addAll(CC.translateStrings(lore));
        this.itemMeta.setLore(newLore);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        return addLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(List<String> lore) {
        List<String> toSet = new ArrayList<>();
        lore.forEach(string -> toSet.add(CC.translate(string)));
        this.itemMeta.setLore(toSet);
        return this;
    }

    public ItemBuilder setLore(List<String> lore, Player player) {
        List<String> toSet = new ArrayList<>();
        lore.forEach(string -> toSet.add(CC.translate(player, string)));
        this.itemMeta.setLore(toSet);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        this.itemStack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder setData(int data) {
        this.itemStack.setData(new MaterialData(this.itemStack.getType(), (byte) data));
        return this;
    }

    public ItemBuilder setGlowing() {
        this.itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment) {
        this.itemStack.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder setType(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        this.itemMeta.setLore(new ArrayList());
        return this;
    }

    public ItemBuilder clearEnchantments() {
        this.itemStack.getEnchantments().keySet().forEach(e -> this.itemStack.removeEnchantment(e));
        return this;
    }

    public ItemBuilder setColor(Color color) {
        if (this.itemStack.getType() == Material.LEATHER_BOOTS || this.itemStack
                .getType() == Material.LEATHER_CHESTPLATE || this.itemStack
                .getType() == Material.LEATHER_HELMET || this.itemStack
                .getType() == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) this.itemStack.getItemMeta();
            meta.setColor(color);
            return this;
        }
        throw new IllegalArgumentException("color() only applicable for leather armor.");
    }


    public ItemBuilder setOwner(String owner) {
        SkullMeta meta = (SkullMeta) this.itemMeta;
        meta.setOwner(owner);
        return this;
    }

    public ItemStack get() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

    public static void rename(ItemStack stack, String name) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        stack.setItemMeta(meta);
    }

    public static ItemStack reloreItem(ItemStack stack, String... lore) {
        return reloreItem(ReloreType.OVERWRITE, stack, lore);
    }

    public static ItemStack reloreItem(ReloreType type, ItemStack stack, String... lores) {
        List<String> nLore;
        ItemMeta meta = stack.getItemMeta();

        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new LinkedList<>();
        }

        switch (type) {
            case APPEND:
                lore.addAll(Arrays.asList(lores));
                meta.setLore(CC.translateStrings(lore));
                break;
            case PREPEND:
                nLore = new LinkedList<>(Arrays.asList(lores));
                nLore.addAll(CC.translateStrings(lore));
                meta.setLore(CC.translateStrings(nLore));
                break;
            case OVERWRITE:
                meta.setLore(Arrays.asList(lores));
                break;
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public enum ReloreType {
        OVERWRITE,
        PREPEND,
        APPEND;
    }

    public static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, String name, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, String name, int amount, short damage) {
        ItemStack item = new ItemStack(material, amount, damage);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate(name));
        item.setItemMeta(meta);
        return item;
    }

    public ItemBuilder ownerSkull(String owner) {
        if (this.itemStack.getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {
            SkullMeta meta = (SkullMeta) this.itemStack.getItemMeta();
            meta.setOwner(owner);
            this.itemStack.setItemMeta((ItemMeta) meta);
            return this;
        }

        throw new IllegalArgumentException("setOwner() only applicable for Skull Item");
    }
}