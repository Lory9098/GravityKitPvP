package it.gravitymc.gravitykitpvp.commands;

import it.gravitymc.gravitykitpvp.utils.build.Builders;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCMD extends Command {
    public BuildCMD() {
        super("build");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("kitpvp.commands.build")) {
            player.sendMessage(CC.translate("&cNon hai il permesso per eseguire questo comando."));
            return true;
        }

        if (Builders.isBuilder(player.getUniqueId())) {
            Builders.removePlayer(player.getUniqueId());
            player.sendMessage(CC.translate("&cNon sei più un builder."));
        } else {
            Builders.addPlayer(player.getUniqueId());
            player.sendMessage(CC.translate("&aOra sei un builder."));
        }

        return true;
    }
}
