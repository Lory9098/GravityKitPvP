package it.gravitymc.gravitykitpvp.commands;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import it.gravitymc.gravitykitpvp.coinflip.CoinFlip;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CoinFlipCMD extends Command {
    private ConfigurationSection section = KitPvP.get().getMessages().getConfigurationSection("CoinFlip");
    public CoinFlipCMD() {
        super("coinflip");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if (!(sender instanceof Player player)) return true;

        if (args.length != 1) {
            player.sendMessage(CC.translate(section.getString("Usage")));
            return true;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate(section.getString("InvalidNumber")));
            return true;
        }

        if (amount <= 0) {
            player.sendMessage(CC.translate(section.getString("InvalidAmount")));
            return true;
        }

        PlayerData data = PlayerData.getByUuid(player.getUniqueId());

        if (amount > data.getCoins()) {
            player.sendMessage(CC.translate(section.getString("NotEnoughMoney")));
            return true;
        }

        PaginatedGui gui = Gui.paginated()
                .title(Component.text(CC.translate(section.getString("InventoryName"))))
                .rows(6)
                .pageSize(45)
                .create();

        int i = 0;

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target == player) continue;
            i++;
            int finalAmount = amount;
            ItemStack head = new it.gravitymc.gravitykitpvp.utils.item.ItemBuilder(Material.SKULL_ITEM)
                    .setName(CC.translate(section.getString("HeadName").replace("%player%", target.getName())))
                    .setLore(CC.translateStrings(
                            section.getStringList("HeadLore")
                                    .stream()
                                    .map(s -> s
                                            .replace("%amount%", String.valueOf(finalAmount))
                                            .replace("%player%", target.getName())
                                    )
                                    .toList()
                    ))
                    .ownerSkull(target.getName())
                    .get();
            gui.setItem(i, new GuiItem(
                    head,
                    (e) -> {
                        e.setCancelled(true);
                        Player clicker = (Player) e.getWhoClicked();
                        if (data.getCoins() < finalAmount) {
                            clicker.sendMessage(CC.translate(section.getString("NotEnoughMoney")));
                            return;
                        }

                        PlayerData targetData = PlayerData.getByUuid(player.getUniqueId());

                        if (targetData.getCoins() < finalAmount) {
                            clicker.sendMessage(CC.translate(section.getString("TargetNotEnoughMoney")));
                            return;
                        }

                        data.setCoins(data.getCoins() - finalAmount);
                        new CoinFlip(player, target, finalAmount);
                    }
            ));
        }

        gui.setItem(6, 3, ItemBuilder.from(Material.BOOK).name(Component.text("Previous")).asGuiItem(event -> {
            gui.previous();
            event.setCancelled(true);
        }));
        gui.setItem(6, 7, ItemBuilder.from(Material.BOOKSHELF).name(Component.text("Next")).asGuiItem(event -> {
            gui.next();
            event.setCancelled(true);
        }));

        gui.open(player);

        return true;
    }
}
