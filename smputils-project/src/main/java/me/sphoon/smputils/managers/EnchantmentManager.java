package me.sphoon.smputils.managers;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public class EnchantmentManager {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private Map<String, Integer> enchantmentLimits;

    public EnchantmentManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.enchantmentLimits = new HashMap<>();
        loadEnchantmentLimits();
    }

    private void loadEnchantmentLimits() {
        enchantmentLimits.clear();
        
        // Load all enchantment limits from config
        var enchantConfig = configManager.getConfig().getConfigurationSection("enchantments.limits");
        if (enchantConfig != null) {
            for (String key : enchantConfig.getKeys(false)) {
                int level = configManager.getConfig().getInt("enchantments.limits." + key + ".max-level", -1);
                if (level > 0) {
                    enchantmentLimits.put(key.toUpperCase(), level);
                }
            }
        }
    }

    public boolean isEnchantmentAllowed(Enchantment enchantment, int level) {
        String enchantName = enchantment.getKey().getKey().toUpperCase();
        
        if (!enchantmentLimits.containsKey(enchantName)) {
            return true; // No limit set, allow it
        }

        int maxLevel = enchantmentLimits.get(enchantName);
        return level <= maxLevel;
    }

    public int getMaxLevel(Enchantment enchantment) {
        String enchantName = enchantment.getKey().getKey().toUpperCase();
        return enchantmentLimits.getOrDefault(enchantName, enchantment.getMaxLevel());
    }

    public Map<String, Integer> getEnchantmentLimits() {
        return new HashMap<>(enchantmentLimits);
    }

    public void setEnchantmentLimit(String enchantmentName, int level) {
        enchantmentLimits.put(enchantmentName.toUpperCase(), level);
        configManager.setEnchantmentLimit(enchantmentName, level);
    }

    public void removeEnchantmentLimit(String enchantmentName) {
        enchantmentLimits.remove(enchantmentName.toUpperCase());
        configManager.getConfig().set("enchantments.limits." + enchantmentName + ".max-level", null);
        plugin.saveConfig();
    }

    public void reload() {
        configManager.reloadConfig();
        loadEnchantmentLimits();
        plugin.getLogger().info("✓ Enchantment limits reloaded!");
    }
}
