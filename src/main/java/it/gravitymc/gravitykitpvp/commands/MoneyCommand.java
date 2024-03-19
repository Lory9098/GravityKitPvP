package it.gravitymc.gravitykitpvp.commands;

import java.util.List;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class MoneyCommand extends Command {
    public MoneyCommand() {
        super("money");
        setAliases(List.of("bilancio", "balance"));
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(CC.onlyPlayer);
            return true;
        }
        sender.sendMessage(CC.translate(player, KitPvP.get().getMessages().getString("Economy.Money")
                .replace("{money}", String.valueOf(Math.round(PlayerData.getByUuid(player.getUniqueId()).getCoins())))));
        return true;
    }
}