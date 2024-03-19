package it.gravitymc.gravitykitpvp.commands.staff;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.location.LocationUtil;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends Command {
    public SetSpawnCommand() {
        super("setspawn");
        setPermission("kitpvp.command.setspawn");
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Player player;
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(CC.noPermission);
            return true;
        }
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(CC.onlyPlayer);
            return true;
        }
        KitPvP.get().getConfig().set("Spawn", LocationUtil.serializeLocation(player.getLocation()));
        KitPvP.get().getConfig().save();
        player.sendMessage(CC.translate("&aIl punto di spawn e' stato impostato."));
        return true;
    }
}