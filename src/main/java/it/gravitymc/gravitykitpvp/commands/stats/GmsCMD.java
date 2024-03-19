package it.gravitymc.gravitykitpvp.commands.stats;

import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmsCMD extends Command {
    public GmsCMD() {
        super("gms");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!player.hasPermission("kitpvp.commands.gms")) {
            player.sendMessage(CC.noPermission);
            return true;
        }

        player.setGameMode(GameMode.SURVIVAL);
        return true;
    }
}
