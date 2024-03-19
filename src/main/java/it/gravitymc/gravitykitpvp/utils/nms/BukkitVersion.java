package it.gravitymc.gravitykitpvp.utils.nms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;


public class BukkitVersion {
    public static String getVersion() {
        return Bukkit.getServer().getBukkitVersion();
    }

    public static String getVersion(boolean split, int parts) {
        if (split) {
            String cleanVersion = getVersion().split("-")[0];
            if (parts > 0) {
                StringBuilder output = new StringBuilder();
                String[] strings = cleanVersion.replace(".", "-").split("-");
                for (int i = 0; i < parts; i++) {
                    if (i != 0) {
                        output.append(".");
                    }
                    output.append(strings[i]);
                }
                return output.toString();
            }
            return cleanVersion;
        }
        return getVersion();
    }

    private static List<Integer> getVersionPartsAsInt(int parts) throws IndexOutOfBoundsException {
        String cleanVersion = getVersion().split("-")[0];
        if (parts == 0) {
            return null;
        }
        List<Integer> versions = new ArrayList<>();
        String[] s = cleanVersion.replace(".", "-").split("-");
        for (int i = 0; i < parts; i++) {
            int version = 0;
            version = Integer.parseInt(s[i]);
            versions.add(Integer.valueOf(version));
        }
        return versions;
    }

    public static int getVersionAsInt() {
        String version = getVersion(true, 0).replace(".", "");
        return Integer.parseInt(version);
    }

    public static int getVersionAsInt(int parts) {
        String version = getVersion(true, parts).replace(".", "");
        return Integer.parseInt(version);
    }

    public static boolean isVersion(String version) {
        return version.equalsIgnoreCase(getVersion(true, 0));
    }

    public static boolean isVersion(String version, int parts) {
        return version.equalsIgnoreCase(getVersion(true, parts));
    }

    public static boolean matchVersion(List<String> versions) {
        return versions.contains(getVersion(true, 0));
    }

    public static boolean matchVersion(List<String> versions, int parts) {
        return versions.contains(getVersion(true, parts));
    }

    public static int getMajorVersionAsInt() {
        return ((Integer) ((List<Integer>) Objects.<List<Integer>>requireNonNull(getVersionPartsAsInt(1))).get(0)).intValue();
    }

    public static int getMinorVersionAsInt() {
        return ((Integer) ((List<Integer>) Objects.<List<Integer>>requireNonNull(getVersionPartsAsInt(2))).get(1)).intValue();
    }

    public static int getPatchVersionAsInt() {
        return ((Integer) ((List<Integer>) Objects.<List<Integer>>requireNonNull(getVersionPartsAsInt(3))).get(2)).intValue();
    }

    public static String getEngine() {
        String string = "Unknown Engine!";
        if (Bukkit.getVersion().contains("Paper")) {
            string = "Paper";
        }
        if (Bukkit.getVersion().contains("Spigot")) {
            string = "Spigot";
        }
        if (Bukkit.getVersion().contains("Bukkit")) {
            string = "Bukkit | CraftBukkit";
        }
        return string;
    }
}