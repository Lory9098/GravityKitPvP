package it.gravitymc.gravitykitpvp.hook;

import java.util.HashMap;
import java.util.List;

import com.zaxxer.hikari.HikariConfig;
import it.gravitymc.gravitykitpvp.KitPvP;
import it.gravitymc.gravitykitpvp.backend.data.PlayerData;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EnderPearl;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class VaultHook implements Economy {

    private static Economy economy;

    private void setupEconomy() {
        Bukkit.getServicesManager().register(Economy.class, this, KitPvP.get(), ServicePriority.Normal);
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (rsp != null)
            economy = rsp.getProvider();
    }

    public static boolean hasEconomy() {
        return economy != null;
    }

    public boolean isEnabled() {
        return true;
    }


    public String getName() {
        return "KitPvP Economy";
    }


    public boolean hasBankSupport() {
        return false;
    }


    public int fractionalDigits() {
        return -1;
    }


    public String format(double amount) {
        return KitPvP.get().getMessages().getString("Economy.format").replace("%value%", "" + Math.round(amount));
    }


    public String currencyNamePlural() {
        return KitPvP.get().getMessages().getString("Economy.plural-name");
    }


    public String currencyNameSingular() {
        return KitPvP.get().getMessages().getString("Economy.singular-name");
    }


    public boolean hasAccount(String playerName) {
        return true;
    }


    public boolean hasAccount(OfflinePlayer player) {
        return true;
    }


    public boolean hasAccount(String playerName, String worldName) {
        return true;
    }


    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return true;
    }


    public double getBalance(String playerName) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return PlayerData.getByName(playerName).getCoins();
    }


    public double getBalance(OfflinePlayer player) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return PlayerData.getByUuid(player.getUniqueId()).getCoins();
    }


    public double getBalance(String playerName, String world) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return getBalance(playerName);
    }


    public double getBalance(OfflinePlayer player, String world) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return getBalance(player);
    }


    public boolean has(String playerName, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return (getBalance(playerName) >= amount);
    }


    public boolean has(OfflinePlayer player, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return (getBalance(player) >= amount);
    }


    public boolean has(String playerName, String worldName, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return (getBalance(playerName) >= amount);
    }


    public boolean has(OfflinePlayer player, String worldName, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return (getBalance(player) >= amount);
    }


    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        PlayerData playerData = PlayerData.getByName(playerName);
        if (!has(playerName, amount))
            return new EconomyResponse(amount, playerData.getCoins(), EconomyResponse.ResponseType.FAILURE, KitPvP.get().getMessages().getString("Economy.FailureMessage"));
        playerData.setCoins(playerData.getCoins() - amount);
        playerData.save(true);
        return new EconomyResponse(amount, playerData.getCoins(), EconomyResponse.ResponseType.SUCCESS, KitPvP.get().getMessages().getString("Economy.SuccessMessage"));
    }


    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (!has(player, amount))
            return new EconomyResponse(amount, playerData.getCoins(), EconomyResponse.ResponseType.FAILURE, KitPvP.get().getMessages().getString("Economy.FailureMessage"));
        playerData.setCoins(playerData.getCoins() - amount);
        playerData.save(true);
        return new EconomyResponse(amount, playerData.getGoldenHeadConsumed(), EconomyResponse.ResponseType.SUCCESS, KitPvP.get().getMessages().getString("Economy.SuccessMessage"));
    }


    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return withdrawPlayer(playerName, amount);
    }


    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return withdrawPlayer(player, amount);
    }


    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        PlayerData playerData = PlayerData.getByName(playerName);
        playerData.setCoins(playerData.getCoins() + amount);
        return new EconomyResponse(amount, playerData.getCoins(), EconomyResponse.ResponseType.SUCCESS, KitPvP.get().getMessages().getString("Economy.SuccessMessage"));
    }


    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        playerData.setCoins(playerData.getCoins() + amount);
        playerData.save(true);
        return new EconomyResponse(amount, playerData.getCoins(), EconomyResponse.ResponseType.SUCCESS, KitPvP.get().getMessages().getString("Economy.SuccessMessage"));
    }


    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return depositPlayer(playerName, amount);
    }


    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        if (!hasEconomy())
            throw new UnsupportedOperationException("Vault Economy not found, call hasEconomy() to check it first.");
        return depositPlayer(player, amount);
    }


    public EconomyResponse createBank(String name, String player) {
        return null;
    }


    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }


    public EconomyResponse deleteBank(String name) {
        return null;
    }


    public EconomyResponse bankBalance(String name) {
        return null;
    }


    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }


    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }


    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }


    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }


    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }


    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }


    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }


    public List<String> getBanks() {
        return null;
    }


    public boolean createPlayerAccount(String playerName) {
        return true;
    }


    public boolean createPlayerAccount(OfflinePlayer player) {
        return true;
    }


    public boolean createPlayerAccount(String playerName, String worldName) {
        return true;
    }


    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return true;
    }

    public VaultHook() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            setupEconomy();
        }
    }
}