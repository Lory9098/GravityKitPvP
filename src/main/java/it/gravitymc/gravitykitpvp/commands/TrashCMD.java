package it.gravitymc.gravitykitpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrashCMD extends Command {
    public TrashCMD() {
        super("trash");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        player.openInventory(Bukkit.createInventory(null, 54, "Trash"));
        return true;
    }
}
