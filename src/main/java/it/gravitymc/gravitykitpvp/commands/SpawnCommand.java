package it.gravitymc.gravitykitpvp.commands;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.location.LocationUtil;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends Command {
    public SpawnCommand() {
        super("spawn");
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(CC.onlyPlayer);
            return true;
        }
        if (KitPvP.get().getCombatLogManager().combatTagged(player)) {
            player.sendMessage(CC.translate("&cNon puoi eseguire questo comando se sei in combattimento."));
            return true;
        }
        player.teleport(LocationUtil.deserializeLocation(KitPvP.get().getConfig().getString("Spawn")));
        return true;
    }
}