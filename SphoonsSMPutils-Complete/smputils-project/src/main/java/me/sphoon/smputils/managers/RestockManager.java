package me.sphoon.smputils.managers;

import org.bukkit.plugin.java.JavaPlugin;

public class RestockManager {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private boolean villagersEnabled;
    private boolean lecternsEnabled;
    private boolean instantRestock;

    public RestockManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        loadRestockConfig();
    }

    private void loadRestockConfig() {
        villagersEnabled = configManager.getConfig().getBoolean("restock.villagers.enabled", true);
        lecternsEnabled = configManager.getConfig().getBoolean("restock.lecterns.enabled", true);
        instantRestock = configManager.getConfig().getBoolean("restock.villagers.instant", true);
    }

    public boolean isVillagerRestockEnabled() {
        return villagersEnabled;
    }

    public boolean isLecternRestockEnabled() {
        return lecternsEnabled;
    }

    public boolean isInstantRestock() {
        return instantRestock;
    }

    public void setVillagerRestock(boolean enabled) {
        villagersEnabled = enabled;
        configManager.getConfig().set("restock.villagers.enabled", enabled);
        plugin.saveConfig();
    }

    public void setLecternRestock(boolean enabled) {
        lecternsEnabled = enabled;
        configManager.getConfig().set("restock.lecterns.enabled", enabled);
        plugin.saveConfig();
    }

    public void setInstantRestock(boolean enabled) {
        instantRestock = enabled;
        configManager.getConfig().set("restock.villagers.instant", enabled);
        plugin.saveConfig();
    }

    public void reload() {
        configManager.reloadConfig();
        loadRestockConfig();
        plugin.getLogger().info("✓ Restock configuration reloaded!");
    }
}
