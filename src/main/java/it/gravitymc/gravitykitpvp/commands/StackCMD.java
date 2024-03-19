package it.gravitymc.gravitykitpvp.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StackCMD extends Command {
    public StackCMD() {
        super("stack");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        int numbersOfPearls = 0;
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType().equals(Material.ENDER_PEARL)) {
                numbersOfPearls += player.getInventory().getItem(i).getAmount();
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
        }

        player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, numbersOfPearls));
        return true;
    }
}
