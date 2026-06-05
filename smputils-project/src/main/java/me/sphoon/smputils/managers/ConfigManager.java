package me.sphoon.smputils.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private Map<String, Boolean> moduleStatus;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.moduleStatus = new HashMap<>();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        // Load module status
        moduleStatus.put("enchantments", config.getBoolean("enchantments.enabled", true));
        moduleStatus.put("items", config.getBoolean("items.enabled", true));
        moduleStatus.put("potions", config.getBoolean("potions.enabled", true));
        moduleStatus.put("maces", config.getBoolean("maces.enabled", true));
        moduleStatus.put("restock", config.getBoolean("restock.enabled", true));
        moduleStatus.put("itemedit", config.getBoolean("itemedit.enabled", true));
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    // Module status methods
    public boolean isEnchantmentsEnabled() {
        return moduleStatus.getOrDefault("enchantments", true);
    }

    public boolean isItemsEnabled() {
        return moduleStatus.getOrDefault("items", true);
    }

    public boolean isProtionsEnabled() {
        return moduleStatus.getOrDefault("potions", true);
    }

    public boolean isMacesEnabled() {
        return moduleStatus.getOrDefault("maces", true);
    }

    public boolean isRestockEnabled() {
        return moduleStatus.getOrDefault("restock", true);
    }

    public boolean isItemEditEnabled() {
        return moduleStatus.getOrDefault("itemedit", true);
    }

    // Get enchantment limit
    public int getEnchantmentLimit(String enchantment) {
        return config.getInt("enchantments.limits." + enchantment + ".max-level", -1);
    }

    // Get item limit
    public int getItemLimit(String material) {
        return config.getInt("items.limits." + material, -1);
    }

    // Get potion limit
    public int getPotionLimit(String potion) {
        return config.getInt("potions.limits." + potion + ".max-level", -1);
    }

    // Get max maces allowed
    public int getMaxMaces() {
        return config.getInt("maces.allowed-maces", 3);
    }

    // Get messages
    public String getMessage(String module, String key) {
        String message = config.getString(module + ".messages." + key, "");
        if (message.isEmpty()) {
            return "&cMessage not found: " + key;
        }
        return message;
    }

    // Utility methods
    public String colorize(String text) {
        return text.replace("&", "§");
    }

    public void setModuleEnabled(String module, boolean enabled) {
        moduleStatus.put(module, enabled);
        config.set(module + ".enabled", enabled);
        plugin.saveConfig();
    }

    public void setEnchantmentLimit(String enchantment, int level) {
        config.set("enchantments.limits." + enchantment + ".max-level", level);
        plugin.saveConfig();
    }

    public void setItemLimit(String material, int limit) {
        config.set("items.limits." + material, limit);
        plugin.saveConfig();
    }

    public void setPotionLimit(String potion, int level) {
        config.set("potions.limits." + potion + ".max-level", level);
        plugin.saveConfig();
    }
}
