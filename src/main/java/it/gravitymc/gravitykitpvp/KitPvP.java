package it.gravitymc.gravitykitpvp;

import it.gravitymc.gravitykitpvp.ability.manager.AbilityManager;
import it.gravitymc.gravitykitpvp.backend.DatabaseManager;
import it.gravitymc.gravitykitpvp.commands.*;
import it.gravitymc.gravitykitpvp.commands.Punch.PunchCMD;
import it.gravitymc.gravitykitpvp.commands.abilities.AbilitiesCommand;
import it.gravitymc.gravitykitpvp.commands.staff.SetSpawnCommand;
import it.gravitymc.gravitykitpvp.commands.stats.*;
import it.gravitymc.gravitykitpvp.hook.KitPvPPlaceholderHook;
import it.gravitymc.gravitykitpvp.hook.VaultHook;
import it.gravitymc.gravitykitpvp.leaderboard.LeadearboardManager;
import it.gravitymc.gravitykitpvp.listener.AbilitiesListener;
import it.gravitymc.gravitykitpvp.listener.DataListener;
import it.gravitymc.gravitykitpvp.listener.GeneralListener;
import it.gravitymc.gravitykitpvp.logger.CombatListener;
import java.lang.reflect.Method;
import java.util.List;

import it.gravitymc.gravitykitpvp.logger.CombatLogManager;
import it.gravitymc.gravitykitpvp.provider.AdapterManager;
import it.gravitymc.gravitykitpvp.utils.config.ConfigFile;
import it.gravitymc.gravitykitpvp.workLoad.WorkloadThread;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class KitPvP extends JavaPlugin {
    private static KitPvP instance;
    private DatabaseManager databaseManager;
    private AdapterManager adapterManager;
    private CombatLogManager combatLogManager;
    private LeadearboardManager leadearboardManager;
    private ConfigFile messages;

    private ConfigFile abilities;
    private ConfigFile config;
    private ConfigFile scoreboard;
    private VaultHook vaultHook;
    private KitPvPPlaceholderHook kitPvPPlaceholderHook;
    private AbilityManager abilityManager;

    private WorkloadThread workloadThread;

    public void onEnable() {
        instance = this;

        loadConfig();
    }

    private void loadConfig() {
        this.messages = new ConfigFile(this, "messages.yml");
        this.config = new ConfigFile(this, "config.yml");
        this.scoreboard = new ConfigFile(this, "scoreboard.yml");
        this.abilities = new ConfigFile(this, "abilities.yml");
        loadDatabase();
    }

    private void loadDatabase() {
        this.databaseManager = new DatabaseManager();
        loadManager();
    }

    private void loadManager() {
        this.adapterManager = new AdapterManager();
        this.combatLogManager = new CombatLogManager();
        this.leadearboardManager = new LeadearboardManager();
        this.abilityManager = new AbilityManager(this);
        this.workloadThread = new WorkloadThread();
        Bukkit.getScheduler().runTaskTimer(this, this.workloadThread, 0L, 1L);

        loadCommandAndListener();
    }

    private void loadCommandAndListener() {
        List.of(
                new SetSpawnCommand(),
                new MoneyCommand(),
                new SpawnCommand(),
                new StatsCommand(),
                new AbilitiesCommand(),
                new TrashCMD(),
                new StackCMD(),
                new BuildCMD(),
                new PunchCMD(),
                new CoinFlipCMD(),
                new SetStatsCMD(),
                new GmsCMD(),
                new GmcCMD(),
                new GmspCMD()
        ).forEach(command -> getCommandMap().register(getName(), command));

        List.of(
                new CombatListener(),
                new DataListener(),
                new GeneralListener(),
                new AbilitiesListener(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        loadHook();
    }


    private void loadHook() {
        if(Bukkit.getServer().getPluginManager().getPlugin("Vault") != null)
            new VaultHook();


        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.kitPvPPlaceholderHook = new KitPvPPlaceholderHook(this);
            this.kitPvPPlaceholderHook.register();
        }
    }


    public CommandMap getCommandMap() {
        try {
            Object server = Bukkit.getServer();
            Method getCommandMapMethod = server.getClass().getDeclaredMethod("getCommandMap", new Class[0]);
            getCommandMapMethod.setAccessible(true);
            return (CommandMap) getCommandMapMethod.invoke(server, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static KitPvP get() {
        return instance;
    }
}