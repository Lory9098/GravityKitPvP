package it.gravitymc.gravitykitpvp.utils.nms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;

public class Versioning {
    public static boolean unsupportedVersion = false;
    public static Pattern Version_Pattern = Pattern.compile("(v|)[0-9][_.][0-9][_.][R0-9]*");
    public static boolean internalDebug = false;
    public static String version;

    public static void ServerVersionHook() {
        Bukkit.getConsoleSender().sendMessage("Â§6Engine: Â§e" + BukkitVersion.getEngine());
        if (getServerVersion().contains("v1_6")) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.6 Support Â§2enabled!");
            return;
        }
        if (getServerVersion().contains("v1_7")) {
            if (getServerVersion().equalsIgnoreCase("v1_7_R4.")) {
                Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft Protocol Hack (1.7 & 1.8) Support Â§2enabled!");
            } else {
                Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.7 Support Â§2enabled!");
            }
            return;
        }
        if (getServerVersion().equalsIgnoreCase("v1_8_R1.")) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.8 Support Â§2enabled!");
            return;
        }
        if (getServerVersion().equalsIgnoreCase("v1_8_R2.")) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.8.3 Support Â§2enabled!");
            return;
        }
        if (getServerVersion().equalsIgnoreCase("v1_8_R3.")) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.8.4-1.8.9 Support Â§2enabled!");
            return;
        }
        if (getServerVersion().equalsIgnoreCase("v1_9_R1.")) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.9 Support Â§2enabled!");
            return;
        }
        if (getServerVersion().equalsIgnoreCase("v1_9_R2.")) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.9 Support Â§2enabled!");
            return;
        }
        if (BukkitVersion.isVersion("1.10", 2)) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.10 Support Â§2enabled!");
            return;
        }
        if (BukkitVersion.isVersion("1.11", 2)) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.11 Support Â§2enabled!");
            return;
        }
        if (BukkitVersion.isVersion("1.12", 2)) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.12 Support Â§2enabled!");
            return;
        }
        if (BukkitVersion.isVersion("1.13", 2)) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.13 Support Â§2enabled!");
            return;
        }
        if (BukkitVersion.isVersion("1.14", 2)) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.14 Support Â§2enabled!");
            return;
        }
        if (BukkitVersion.isVersion("1.15", 2)) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.15 Support Â§2enabled!");
            return;
        }
        if (BukkitVersion.isVersion("1.16", 2)) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.16 Support Â§2enabled!");
            return;
        }
        if (BukkitVersion.isVersion("1.17", 2)) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.17 Support Â§2enabled!");
            return;
        }
        if (BukkitVersion.isVersion("1.18", 2)) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.18 Support Â§2enabled!");
            return;
        }
        if (BukkitVersion.isVersion("1.19", 2)) {
            Bukkit.getServer().getConsoleSender().sendMessage("Â§6Minecraft 1.19 Support Â§2enabled!");
            return;
        }
        unsupportedVersion = true;
    }

    public static int getVersionNumber(String version) {
        return Integer.parseInt(version.replace(".", ""));
    }

    public void Debug() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("TTA Debug Mode started!");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("Engine: " + BukkitVersion.getEngine());
        Bukkit.getConsoleSender().sendMessage("Running version: " + BukkitVersion.getVersion());
        Bukkit.getConsoleSender().sendMessage("Version - Major: " + BukkitVersion.getMajorVersionAsInt() + " - Minor: " +
                BukkitVersion.getMinorVersionAsInt() + " - Patch: " + BukkitVersion.getPatchVersionAsInt());
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("Supported Methods:");
        Bukkit.getConsoleSender().sendMessage("");

        List<String> supMethods = new ArrayList<>();

        supMethods.add("getPing");
        supMethods.add("getTPS");
        supMethods.add("setBossBar");
        supMethods.add("hasBossBar");
        supMethods.add("removeBossBar");


        if (getServerVersion().equalsIgnoreCase("v1_7_R4.")) {
            supMethods.add("sendTitle");
        }
        if (BukkitVersion.isVersion("1.8", 2)) {
            supMethods.add("sendActionBar");
        }
        if (BukkitVersion.matchVersion(Arrays.asList("1.9", "1.10", "1.11", "1.12"), 2)) {
            supMethods.add("sendActionBar");
        }
        if (BukkitVersion.isVersion("1.13", 2)) {
            supMethods.add("sendActionBar");
        }
        if (BukkitVersion.isVersion("1.14", 2)) {
            supMethods.add("sendActionBar");
        }
        if (BukkitVersion.matchVersion(Arrays.asList("1.15", "1.16", "1.17", "1.18", "1.19"), 2)) {
            supMethods.add("sendActionBar");
        }
        if (BukkitVersion.getVersionAsInt(2) > 18) {
            supMethods.add("*Scoreboard Methods*");
            supMethods.add("getPlayer");
            supMethods.add("updateTitle");
            supMethods.add("updateTitleData");
            supMethods.add("updateRow");
            supMethods.add("updateContent");
            supMethods.add("remove");
            supMethods.add("getScoreboards");
            supMethods.add("getScoreboardByPlayer");
        }
        for (String method : supMethods) {
            Bukkit.getConsoleSender().sendMessage(method);
        }
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("TTA Debug Mode finished!");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("");
    }


    public static String getServerVersion() {
        if (Versioning.version != null) {
            return Versioning.version;
        }
        String pkg = Bukkit.getServer().getClass().getPackage().getName();
        String version1 = pkg.substring(pkg.lastIndexOf(".") + 1);
        if (!Version_Pattern.matcher(version1).matches()) {
            version1 = "";
        }
        String version = version1;
        return !version.isEmpty() ? (version + ".") : "";
    }
}