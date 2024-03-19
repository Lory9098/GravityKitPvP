package it.gravitymc.gravitykitpvp.commands.Punch;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunchCMD extends Command {
    public PunchCMD() {
        super("punch");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("This is a player-only command.");
            return true;
        }
        Player player = (Player)sender;
        if (!player.hasPermission("kitpvp.command.punch")) {
            player.sendMessage(CC.noPermission);
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Punch.Usage")));
            return true;
        }
        switch (args[0]) {
            case "horizontal":
                if (args.length < 2) {
                    player.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Punch.horizontal-value-not-defined")));
                    return true;
                }
                if (!isNumeric(args[1])) {
                    player.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Punch.invalid-horizontal-value")));
                    return true;
                }
                KitPvP.get().getConfig().set("Punch.horizontal", Double.valueOf(Double.parseDouble(args[1])));
                KitPvP.get().saveConfig();
                player.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Punch.success-horizontal")));
                return true;
            case "vertical":
                if (args.length < 2) {
                    player.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Punch.vertical-value-not-defined")));
                    return true;
                }
                if (!isNumeric(args[1])) {
                    player.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Punch.invalid-vertical-value")));
                    return true;
                }
                KitPvP.get().getConfig().set("Punch.vertical", Double.valueOf(Double.parseDouble(args[1])));
                KitPvP.get().saveDefaultConfig();
                player.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Punch.success-vertical")));
                return true;
        }
        player.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Punch.command-usage")));
        return true;
    }


    public static boolean isNumeric(String str) {
        if (str == null)
            return false;
        try {
            double d = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
