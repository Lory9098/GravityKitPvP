package it.gravitymc.gravitykitpvp.commands.stats;

import com.mongodb.client.model.Filters;
import java.util.List;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand extends Command {
    public StatsCommand() {
        super("stats");
        setAliases(List.of("statistiche"));
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(CC.onlyPlayer);
            return true;
        }

        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        if (args.length == 0) {
            player.openInventory((new StatsMenu(data))
                    .getInventory());

            return true;
        }


        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        Document document = (Document) KitPvP.get().getDatabaseManager().getPlayers().find(Filters.eq("name", args[0])).first();
        if (document == null) {
            sender.sendMessage(CC.translate("&cStatistiche non trovate di " + offlinePlayer.getName() + "!"));
            return true;
        }
        PlayerData tData = PlayerData.getByUuid(offlinePlayer.getUniqueId());
        player.openInventory((new StatsMenu(tData))
                .getInventory());

        return true;
    }
}