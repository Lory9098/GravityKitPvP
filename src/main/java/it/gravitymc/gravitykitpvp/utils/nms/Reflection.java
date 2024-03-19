package it.gravitymc.gravitykitpvp.utils.nms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Reflection {
    private static Class<?> packetClass;

    static {
        try {
            if (BukkitVersion.getVersionAsInt(2) >= 117) {
                packetClass = Class.forName("net.minecraft.network.protocol.Packet");
            } else {
                packetClass = getNMSClass("Packet");
            }
        } catch (SecurityException | ClassNotFoundException ex) {
            System.err.println("Error - Classes not initialized!");
            ex.printStackTrace();
        }
    }


    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        String className = "net.minecraft.server." + version + "." + name;
        Class<?> c = null;

        try {
            c = Class.forName(className);
        } catch (Exception exception) {
        }

        return c;
    }


    public static Class<?> getCraftClass(String name) {
        String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        String className = "org.bukkit.craftbukkit." + version + "." + name;
        Class<?> c = null;

        try {
            c = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }


    public static void sendPacket(ArrayList<Player> players, Object packet) {
        for (Player p : players)
            sendPacket(p, packet);
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object playerConnection;
            Method sPacket;
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);

            if (BukkitVersion.getVersionAsInt(2) >= 117) {
                playerConnection = handle.getClass().getField("b").get(handle);
            } else {
                playerConnection = handle.getClass().getField("playerConnection").get(handle);
            }

            if (BukkitVersion.getVersionAsInt(2) >= 118) {
                sPacket = playerConnection.getClass().getMethod("a", new Class[]{packetClass});
            } else {
                sPacket = playerConnection.getClass().getMethod("sendPacket", new Class[]{packetClass});
            }
            sPacket.invoke(playerConnection, new Object[]{packet});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static Object getHandle(Entity entity) {
        Object nmsEntity = null;
        Method entityGetHandle = getMethod(entity.getClass(), "getHandle");

        try {
            nmsEntity = entityGetHandle.invoke(entity, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nmsEntity;
    }


    public static Object getHandle(World world) {
        Object nmsWntity = null;
        Method worldGetHandle = getMethod(world.getClass(), "getHandle");

        try {
            nmsWntity = worldGetHandle.invoke(world, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nmsWntity;
    }


    public static Field getField(Class<?> cl, String field) {
        try {
            Field f = cl.getDeclaredField(field);
            return f;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method) && classListsEqual(args, m.getParameterTypes())) {
                return m;
            }
        }
        return null;
    }


    public static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }


    public static boolean classListsEqual(Class<?>[] list1, Class<?>[] list2) {
        if (list1.length != list2.length) {
            return false;
        }

        for (int i = 0; i < list1.length; i++) {
            if (list1[i] != list2[i]) {
                return false;
            }
        }
        return true;
    }
}