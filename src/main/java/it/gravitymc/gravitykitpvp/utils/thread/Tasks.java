package it.gravitymc.gravitykitpvp.utils.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import it.gravitymc.gravitykitpvp.KitPvP;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import org.bukkit.plugin.Plugin;

public class Tasks {
    public static ThreadFactory newThreadFactory(String name) {
        return (new ThreadFactoryBuilder()).setNameFormat(name).build();
    }

    public static void run(Callable callable) {
        Objects.requireNonNull(callable);
        KitPvP.get().getServer().getScheduler().runTask((Plugin) KitPvP.get(), callable::call);
    }

    public static void runAsync(Callable callable) {
        Objects.requireNonNull(callable);
        KitPvP.get().getServer().getScheduler().runTaskAsynchronously((Plugin) KitPvP.get(), callable::call);
    }

    public static void runLater(Callable callable, long delay) {
        Objects.requireNonNull(callable);
        KitPvP.get().getServer().getScheduler().runTaskLater((Plugin) KitPvP.get(), callable::call, delay);
    }

    public static void runAsyncLater(Callable callable, long delay) {
        Objects.requireNonNull(callable);
        KitPvP.get().getServer().getScheduler().runTaskLaterAsynchronously((Plugin) KitPvP.get(), callable::call, delay);
    }

    public static void runTimer(Callable callable, long delay, long interval) {
        Objects.requireNonNull(callable);
        KitPvP.get().getServer().getScheduler().runTaskTimer((Plugin) KitPvP.get(), callable::call, delay, interval);
    }

    public static void runAsyncTimer(Callable callable, long delay, long interval) {
        Objects.requireNonNull(callable);
        KitPvP.get().getServer().getScheduler().runTaskTimerAsynchronously((Plugin) KitPvP.get(), callable::call, delay, interval);
    }

    public static interface Callable {
        void call();
    }
}