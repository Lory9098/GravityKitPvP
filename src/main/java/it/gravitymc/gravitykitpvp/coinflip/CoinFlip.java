package it.gravitymc.gravitykitpvp.coinflip;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import it.gravitymc.gravitykitpvp.utils.inventory.InventoryUtil;
import it.gravitymc.gravitykitpvp.utils.item.ItemBuilder;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
public class CoinFlip extends BukkitRunnable {

    private final Player player;
    private final Player target;
    private final int bet;
    private int row = 1;
    private Player current;
    private Gui gui;

    public CoinFlip(Player player, Player target, int bet) {
        this.player = player;
        this.target = target;
        this.bet = bet;
        this.gui = new Gui(5, CC.translate("&e&lCoinFlip"));
        gui.setItem(
                List.of(
                        0, 1, 2, 3, 4, 5, 6, 7, 8,
                        9, 10, 11, 12, 13, 14, 15, 16, 17,
                        18, 19, 20, 21, 22, 23, 24, 25, 26,
                        27, 28, 29, 30, 31, 32, 33, 34, 35,
                        36, 37, 38, 39, 40, 41, 42, 43, 44
                ),
                new GuiItem(
                        new ItemBuilder(Material.STAINED_GLASS)
                                .get(),
                        (e) -> {
                            e.setCancelled(true);
                        }
                )
        );
        current = new Random().nextInt() % 2 == 0 ? player : target;
        gui.setItem(
                22,
                new GuiItem(
                        getPlayerHead(current),
                        (e) -> {
                            e.setCancelled(true);
                        }
                )
        );

        gui.open(player);
        gui.open(target);
        this.runTaskTimer(KitPvP.get(), 0, 20);
    }

    void update() {
        int initialSlot = 9 * (this.row - 1);
        for (int i = initialSlot; i < initialSlot + 9; i++) {
            gui.setItem(
                    i,
                    new GuiItem(
                            new ItemBuilder(Material.STAINED_GLASS)
                                    .setGlowing()
                                    .get(),
                            (e) -> {
                                e.setCancelled(true);
                            }
                    )
            );
        }
        gui.setItem(
                22,
                new GuiItem(
                        getPlayerHead(current == player ? target : player),
                        (e) -> {
                            e.setCancelled(true);
                        }
                )
        );
        gui.update();
        this.row++;
    }

    public ItemStack getPlayerHead(Player player) {
        return new ItemBuilder(Material.SKULL_ITEM)
                .setName("&e" + player.getName())
                .setOwner(player.getName())
                .get();
    }

    @Override
    public void run() {
        if (this.row > 5) {
            this.cancel();
            Player looser = current == player ? target : player;
            current.sendMessage(CC.translate(KitPvP.get().getMessages().getString("CoinFlip.Won").replace("%amount%", String.valueOf(bet))));
            looser.sendMessage(CC.translate(KitPvP.get().getMessages().getString("CoinFlip.Lost").replace("%amount%", String.valueOf(bet))));

            PlayerData winnerPlayer = PlayerData.getByUuid(current.getUniqueId());

            winnerPlayer.setCoins(winnerPlayer.getCoins() + bet);
            winnerPlayer.save(true);

            PlayerData looserPlayer = PlayerData.getByUuid(looser.getUniqueId());
            looserPlayer.setCoins(looserPlayer.getCoins() - bet);
            looserPlayer.save(true);

            target.closeInventory();
            player.closeInventory();
            return;
        }
        update();
    }
}
