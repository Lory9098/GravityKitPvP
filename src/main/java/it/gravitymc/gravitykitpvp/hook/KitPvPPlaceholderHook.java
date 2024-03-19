package it.gravitymc.gravitykitpvp.hook;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nettychannell.api.GangsAPI;
import me.nettychannell.api.gang.IGang;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KitPvPPlaceholderHook extends PlaceholderExpansion {
    private final KitPvP instance;

    public KitPvPPlaceholderHook(KitPvP instance) {
        this.instance = instance;
    }


    @NotNull
    public String getIdentifier() {
        return "kitpvp";
    }

    @NotNull
    public String getAuthor() {
        return "NettyChannell";
    }

    @NotNull
    public String getVersion() {
        return KitPvP.get().getDescription().getVersion();
    }

    @Nullable
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());

        IGang gang = GangsAPI.getInstance().getGangManager().getGangByPlayer(player.getUniqueId());

        switch (params) {
            case "kills":
                return String.valueOf(playerData.getKills());
            case "deaths":
                return String.valueOf(playerData.getDeaths());
            case "coins":
                return String.valueOf((int) playerData.getCoins());
            case "killStreak":
                return String.valueOf(playerData.getKillStreak());
            case "maxKillStreak":
                return String.valueOf(playerData.getMaxKillStreak());
            case "goldenHeadEaten":
                return String.valueOf(playerData.getGoldenHeadConsumed());
            case "bounty":
                return playerData.getPlayerBounty() + "";
            case "bounty_complex":
                return playerData.getPlayerBounty() <= 0 ? "" : " &e&l" + playerData.getPlayerBounty() + "$";
            case "galaxy":
                return gang == null ? "Nessuna" : gang.getName();
        }
        throw new IllegalStateException("Unexpected value: " + params);
    }
}