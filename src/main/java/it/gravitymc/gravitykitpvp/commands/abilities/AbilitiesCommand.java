package it.gravitymc.gravitykitpvp.commands.abilities;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AbilitiesCommand extends Command {
    public AbilitiesCommand() {
        super("ability");
    }


    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("kitpvp.abilityadmin")) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(
                    CC.translate(
                            KitPvP.get().getMessages().getString("Abilities.Usage")));


            return true;
        }

        String abilityName = args[0];

        Player target = (args.length == 1) ? ((sender instanceof Player) ? (Player) sender : null) : KitPvP.get().getServer().getPlayer(args[1]);

        System.out.println(
                KitPvP.get().getAbilityManager().getAbility(abilityName));

        KitPvP.get().getAbilityManager().getAbility(abilityName)
                .ifPresentOrElse(ability -> {
                    if (target == null) {
                        sender.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Abilities.PlayerNotFound").replace("{player}", args[1])));
                        return;
                    }

                    target.getInventory().addItem(ability.getItem());


                    if (target != sender) {
                        sender.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Abilities.SuccessSender").replace("{ability}", abilityName).replace("{player}", target.getName())));
                    }


                    target.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Abilities.Success").replace("{ability}", abilityName)));
                },
                () ->
                        sender.sendMessage(CC.translate(KitPvP.get().getMessages().getString("Abilities.NoAbility").replace("{ability}", abilityName)))
                );


        return true;
    }
}