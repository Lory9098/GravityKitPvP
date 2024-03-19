package it.gravitymc.gravitykitpvp.utils.bar;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.utils.nms.BukkitVersion;
import it.gravitymc.gravitykitpvp.utils.nms.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class NMS_ActionBar {
    private KitPvP plugin;

    public NMS_ActionBar(KitPvP main) {
        this.plugin = main;
    }

    static final HashMap<UUID, Integer> Count = new HashMap<>();

    private static Class<?> chatComponentTextClass;

    private static Constructor<?> chatComponentTextConstructor;

    private static Class<?> chatMessageTypeClass;

    private static Class<?> iChatBaseComponentClass;

    private static Class<?> packetPlayOutChatClass;

    private static Constructor<?> packetPlayOutChatConstructor;
    private static Constructor<?> clientboundSetActionBarTextPacketConstructor;

    static {
        try {
            if (BukkitVersion.getVersionAsInt(2) >= 117) {
                updateToNewClassStructure();
            } else {
                chatComponentTextClass = Reflection.getNMSClass("ChatComponentText");
                chatComponentTextConstructor = chatComponentTextClass.getConstructor(new Class[]{String.class});

                chatMessageTypeClass = Reflection.getNMSClass("ChatMessageType");

                iChatBaseComponentClass = Reflection.getNMSClass("IChatBaseComponent");

                packetPlayOutChatClass = Reflection.getNMSClass("PacketPlayOutChat");
                if (BukkitVersion.matchVersion(Arrays.asList("1.12", "1.13", "1.14", "1.15"), 2)) {
                    packetPlayOutChatConstructor = packetPlayOutChatClass.getConstructor(new Class[]{iChatBaseComponentClass, chatMessageTypeClass});
                } else if (BukkitVersion.isVersion("1.16", 2)) {
                    packetPlayOutChatConstructor = packetPlayOutChatClass.getConstructor(new Class[]{iChatBaseComponentClass, chatMessageTypeClass, UUID.class});
                } else {
                    packetPlayOutChatConstructor = packetPlayOutChatClass.getConstructor(new Class[]{iChatBaseComponentClass, byte.class});
                }

            }
        } catch (NoSuchMethodException | SecurityException ex) {
            System.err.println("Error - Classes not initialized!");
            ex.printStackTrace();
        }
    }

    public void sendActionBar(Player player, String msg) {
        if (msg == null) {
            msg = "";
        }

        try {
            if (BukkitVersion.matchVersion(Arrays.asList("1.12", "1.13", "1.14", "1.15"), 2)) {
                Object ab = chatComponentTextConstructor.newInstance(msg);
                Object acm = chatMessageTypeClass.getField("GAME_INFO").get(null);
                Object abPacket = packetPlayOutChatConstructor.newInstance(ab, acm);
                Reflection.sendPacket(player, abPacket);
            } else if (BukkitVersion.isVersion("1.16", 2)) {
                Object ab = chatComponentTextConstructor.newInstance(msg);
                Object acm = chatMessageTypeClass.getField("GAME_INFO").get(null);
                Object abPacket = packetPlayOutChatConstructor.newInstance(ab, acm, player.getUniqueId());
                Reflection.sendPacket(player, abPacket);
            } else if (BukkitVersion.matchVersion(Arrays.asList("1.17", "1.18"), 2)) {
                Object ab = chatComponentTextConstructor.newInstance(msg);
                Object abPacket = clientboundSetActionBarTextPacketConstructor.newInstance(ab);
                Reflection.sendPacket(player, abPacket);
            } else if (BukkitVersion.isVersion("1.19", 2)) {

                Object abText = iChatBaseComponentClass.getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{\"text\":\"" + msg + "\"}"});
                Object abPacket = clientboundSetActionBarTextPacketConstructor.newInstance(abText);
                Reflection.sendPacket(player, abPacket);
            } else {
                Object ab = chatComponentTextConstructor.newInstance(msg);
                Object abPacket = packetPlayOutChatConstructor.newInstance(ab, (byte) 2);
                Reflection.sendPacket(player, abPacket);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void sendOldActionBar(Player player, String message) {
        if (message == null) {
            message = "";
        }

        try {
            Object ab = iChatBaseComponentClass.getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{\"text\": \"" + message + "\"}"});
            Object Abpacket = packetPlayOutChatConstructor.newInstance(ab, (byte) 2);
            Reflection.sendPacket(player, Abpacket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendActionBar(Player p, String msg, int duration) {
        if (!Count.containsKey(p.getUniqueId())) {
            sendActionBar(p, msg);
        }
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            sendActionBar(p, msg);
            if (!Count.containsKey(p.getUniqueId())) Count.put(p.getUniqueId(), 0);
            int c = Count.get(p.getUniqueId());
            c += 20;
            Count.put(p.getUniqueId(), c);
            if (c < duration - 20) {
                switchActionBar(p, msg, duration);
            } else {
                Count.remove(p.getUniqueId());
            }
        }, 10L);
    }


    public void switchActionBar(final Player p, final String msg, final int duration) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> NMS_ActionBar.this.sendActionBar(p, msg, duration), 10L);
    }

    private static void updateToNewClassStructure() {
        try {
            if (BukkitVersion.getVersionAsInt(2) < 119) {
                chatComponentTextClass = Class.forName("net.minecraft.network.chat.ChatComponentText");
                chatComponentTextConstructor = chatComponentTextClass.getConstructor(String.class);

                packetPlayOutChatClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutChat");
                packetPlayOutChatConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, chatMessageTypeClass, UUID.class);
            }

            chatMessageTypeClass = Class.forName("net.minecraft.network.chat.ChatMessageType");

            iChatBaseComponentClass = Class.forName("net.minecraft.network.chat.IChatBaseComponent");

            Class<?> clientboundSetActionBarTextPacketClass = Class.forName("net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket");
            clientboundSetActionBarTextPacketConstructor = clientboundSetActionBarTextPacketClass.getConstructor(iChatBaseComponentClass);
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}