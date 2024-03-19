package it.gravitymc.gravitykitpvp.provider;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.stiring.CC;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class KitPvPBoard extends BukkitRunnable {
  @Override
  public void run() {
    Bukkit.getServer().getOnlinePlayers().forEach(this::checkScoreboard);
  }

  public void checkScoreboard(Player player) {
    List<String> config = KitPvP.get().getScoreboard().getStringList("Scoreboard.Lines");

    BPlayerBoard bPlayerBoard = Netherboard.instance().getBoard(player);
    if (bPlayerBoard == null)
      bPlayerBoard = Netherboard.instance().createBoard(player, CC.translate(KitPvP.get().getScoreboard().getString("Scoreboard.Title")));

    ZonedDateTime dateTimeInItaly = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String date = dateTimeInItaly.format(formatter);

    int lineCounter = 0;

    List<String> reversedConfig = new ArrayList<>();
    for (int i = config.size() - 1; i >= 0; i--) {
      reversedConfig.add(config.get(i));
    }

    for (String configLine : reversedConfig) {
      String line = configLine;
      if (line.equals("%ct%")) {
        double remainingCt = KitPvP.get().getCombatLogManager().getCombatTime().getRemainingSeconds(player.getUniqueId());
        if (remainingCt != 0.0D) {
          line = KitPvP.get().getScoreboard().getString("CT-style").replace("%ct%", String.valueOf(remainingCt));
        } else {
          line = "         ";
        }
      } else if (line.equalsIgnoreCase("%galaxy%")) {
        String galaxyValue = PlaceholderAPI.setPlaceholders(player, "%kitpvp_galaxy%");
        if (!galaxyValue.equals("Nessuna")) {
          bPlayerBoard.set(" ", lineCounter++);
          line = KitPvP.get().getScoreboard().getString("Galaxy-style").replace("%kitpvp_galaxy%", galaxyValue);
        } else {
          line = "  ";
        }
      }

      line = CC.translate(player, line.replace("%ping%", PlaceholderAPI.setPlaceholders(player, "%player_ping%"))
              .replace("%date%", date));
      bPlayerBoard.set(line, lineCounter++);
    }
  }


}
