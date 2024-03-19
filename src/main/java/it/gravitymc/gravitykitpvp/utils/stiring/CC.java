package it.gravitymc.gravitykitpvp.utils.stiring;

import java.util.ArrayList;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class CC {
    public static String translate(Player player, String input) {
        return PlaceholderAPI.setPlaceholders(player, translate(input));
    }

    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> translateStrings(List<String> untranslated) {
        List<String> translated = new ArrayList<>();
        untranslated.forEach(untranslatedString -> translated.add(ChatColor.translateAlternateColorCodes('&', untranslatedString)));


        return translated;
    }

    public static List<String> translateStrings(Player player, List<String> untranslated) {
        List<String> translated = new ArrayList<>();

        untranslated.forEach(untranslatedString -> {
            translated.add(ChatColor.translateAlternateColorCodes('&', untranslatedString));

            PlaceholderAPI.setPlaceholders(player, translated);
        });
        return translated;
    }

    public static final String onlyPlayer = translate("&cQuesto comando può essere eseguito solo dai giocatori");
    public static final String noPermission = translate("&cNon hai il permesso di eseguire questo comando!");
}