package it.gravitymc.gravitykitpvp.commands.stats;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SetStatsCMD extends Command implements TabCompleter {
    public SetStatsCMD() {
        super("setstat");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!player.hasPermission("kitpvp.commands.setstat")) {
            player.sendMessage(CC.noPermission);
            return true;
        }

        ConfigurationSection section = KitPvP.get().getMessages().getConfigurationSection("SetStat");

        if (args.length != 3) {
            player.sendMessage(CC.translate(section.getString("Usage")));
            return true;
        }

        Player target = KitPvP.get().getServer().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(CC.translate(section.getString("PlayerNotFound")));
            return true;
        }

        String stat = args[1];

        if (!Objects.equals(stat, "kills") && !Objects.equals(stat, "deaths") && !Objects.equals(stat, "streak") && !Objects.equals(stat, "coins") && !Objects.equals(stat, "maxStreak") && !Objects.equals(stat, "goldenHeadConsumed") && !Objects.equals(stat, "elo")) {
            player.sendMessage(CC.translate(section.getString("StatNotFound")));
            return true;
        }

        int value;

        try {
            value = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate(section.getString("InvalidValue")));
            return true;
        }

        PlayerData playerData = PlayerData.getByUuid(target.getUniqueId());

        switch (stat) {
            case "kills":
                playerData.setKills(value);
                break;
            case "deaths":
                playerData.setDeaths(value);
                break;
            case "streak":
                playerData.setKillStreak(value);
                break;
            case "coins":
                playerData.setCoins(value);
                break;
            case "maxStreak":
                playerData.setMaxKillStreak(value);
                break;
            case "goldenHeadConsumed":
                playerData.setGoldenHeadConsumed(value);
                break;
            case "elo":
                playerData.setPlayerBounty(value);
                break;
        }

        player.sendMessage(CC.translate(section.getString("Success").replace("%stat%", stat).replace("%value%", String.valueOf(value)).replace("%player%", target.getName())));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }else if (args.length == 1) {
            return List.of("kills", "deaths", "streak", "coins", "maxStreak", "goldenHeadConsumed", "elo");
        }
        return List.of();
    }
}
