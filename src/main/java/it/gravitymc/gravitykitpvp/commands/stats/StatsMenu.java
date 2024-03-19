package it.gravitymc.gravitykitpvp.commands.stats;

import java.util.List;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import it.gravitymc.gravitykitpvp.utils.item.ItemBuilder;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class StatsMenu
  implements InventoryHolder {
    PlayerData playerData;
    private Inventory inventory;

    public StatsMenu(PlayerData playerData) {
        this.playerData = playerData;
        init(playerData.getPlayer());
    }


    public Inventory getInventory() {
        return this.inventory;
    }

    public void init(Player player) {
        String argument;
        this.playerData = PlayerData.getByUuid(player.getUniqueId());
        Document document = KitPvP.get().getDatabaseManager().getPlayer(this.playerData.getUuid());


        if (this.playerData.getUuid() == player.getUniqueId()) {
            argument = CC.translate("Statistiche");
        } else {
            argument = CC.translate("&a&lStatistiche di " + this.playerData.getName());
        }

        this.inventory = Bukkit.createInventory(this, 45, argument);

        if (document == null) {
            document = new Document();
            document.put("uuid", this.playerData.getUuid().toString());
            document.put("kills", Integer.valueOf(this.playerData.getKills()));
            document.put("deaths", Integer.valueOf(this.playerData.getDeaths()));
            document.put("coins", Double.valueOf(this.playerData.getCoins()));
            document.put("killStreak", Integer.valueOf(this.playerData.getKillStreak()));
            document.put("maxStreak", Integer.valueOf(this.playerData.getMaxKillStreak()));
            document.put("goldenAppleEaten", Integer.valueOf(this.playerData.getGoldenHeadConsumed()));
            document.put("name", (this.playerData.getRealName() != null) ? this.playerData.getRealName() : this.playerData.getName());
            document.put("playerElo", Integer.valueOf(this.playerData.getPlayerBounty()));
            KitPvP.get().getDatabaseManager().replacePlayer(this.playerData, document);
        }


        getInventory().setItem(12, (new ItemBuilder(Material.DIAMOND_SWORD))


                .setLore(
                        List.of("&7Uccisioni: &a" + document
                                .getInteger("kills")))


                .get());


        getInventory().setItem(14, (new ItemBuilder(Material.BONE))


                .setLore(
                        List.of("&7Morti: &a" + document
                                .getInteger("deaths")))


                .get());


        getInventory().setItem(29, (new ItemBuilder(Material.BONE))


                .setLore(
                        List.of("&7Streak: &a" + document
                                .getInteger("killStreak")))


                .get());


        getInventory().setItem(31, (new ItemBuilder(Material.ITEM_FRAME))


                .setLore(
                        List.of("&7Max Streak: &a" + document
                                .getInteger("maxStreak")))


                .get());


        getInventory().setItem(33, (new ItemBuilder(Material.NETHER_STAR))


                .setLore(
                        List.of("&7K/D: &a" + this.playerData
                                .getKdr()))


                .get());
    }
}