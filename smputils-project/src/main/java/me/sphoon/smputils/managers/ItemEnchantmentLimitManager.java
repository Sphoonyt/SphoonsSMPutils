package me.sphoon.smputils.managers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class ItemEnchantmentLimitManager {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private Map<String, Map<String, Integer>> itemEnchantLimits; // item -> enchantment -> level

    public ItemEnchantmentLimitManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.itemEnchantLimits = new HashMap<>();
        loadItemEnchantLimits();
    }

    private void loadItemEnchantLimits() {
        itemEnchantLimits.clear();
        
        var config = configManager.getConfig();
        var itemEnchants = config.getConfigurationSection("enchantments.item-specific");
        
        if (itemEnchants != null) {
            for (String itemName : itemEnchants.getKeys(false)) {
                Map<String, Integer> enchantLimits = new HashMap<>();
                var enchants = config.getConfigurationSection("enchantments.item-specific." + itemName);
                
                if (enchants != null) {
                    for (String enchantName : enchants.getKeys(false)) {
                        int level = config.getInt("enchantments.item-specific." + itemName + "." + enchantName, -1);
                        if (level > 0) {
                            enchantLimits.put(enchantName.toUpperCase(), level);
                        }
                    }
                }
                
                if (!enchantLimits.isEmpty()) {
                    itemEnchantLimits.put(itemName.toUpperCase(), enchantLimits);
                }
            }
        }
        
        plugin.getLogger().info("✓ Loaded " + itemEnchantLimits.size() + " item-specific enchantment limits");
    }

    /**
     * Check if an item-enchantment combination is allowed at a specific level
     */
    public boolean isItemEnchantmentAllowed(Material material, Enchantment enchantment, int level) {
        String itemName = material.name().toUpperCase();
        String enchantName = enchantment.getKey().getKey().toUpperCase();
        
        // Check if there's a specific limit for this item
        if (itemEnchantLimits.containsKey(itemName)) {
            Map<String, Integer> enchantLimits = itemEnchantLimits.get(itemName);
            
            if (enchantLimits.containsKey(enchantName)) {
                int maxLevel = enchantLimits.get(enchantName);
                return level <= maxLevel;
            }
        }
        
        // If no item-specific limit, allow it (global limit will be checked elsewhere)
        return true;
    }

    /**
     * Get the max level for an enchantment on a specific item
     * Returns -1 if no specific limit is set
     */
    public int getItemEnchantmentLimit(Material material, String enchantmentName) {
        String itemName = material.name().toUpperCase();
        String enchantName = enchantmentName.toUpperCase();
        
        if (itemEnchantLimits.containsKey(itemName)) {
            Map<String, Integer> enchantLimits = itemEnchantLimits.get(itemName);
            return enchantLimits.getOrDefault(enchantName, -1);
        }
        
        return -1;
    }

    /**
     * Get all item-specific enchantment limits
     */
    public Map<String, Map<String, Integer>> getAllItemEnchantLimits() {
        return new HashMap<>(itemEnchantLimits);
    }

    /**
     * Set an enchantment limit for a specific item
     */
    public void setItemEnchantmentLimit(Material material, String enchantmentName, int level) {
        String itemName = material.name().toUpperCase();
        String enchantName = enchantmentName.toUpperCase();
        
        if (!itemEnchantLimits.containsKey(itemName)) {
            itemEnchantLimits.put(itemName, new HashMap<>());
        }
        
        itemEnchantLimits.get(itemName).put(enchantName, level);
        
        // Save to config
        String path = "enchantments.item-specific." + itemName + "." + enchantName;
        configManager.getConfig().set(path, level);
        plugin.saveConfig();
    }

    /**
     * Remove an enchantment limit from a specific item
     */
    public void removeItemEnchantmentLimit(Material material, String enchantmentName) {
        String itemName = material.name().toUpperCase();
        String enchantName = enchantmentName.toUpperCase();
        
        if (itemEnchantLimits.containsKey(itemName)) {
            itemEnchantLimits.get(itemName).remove(enchantName);
            
            // Remove from config
            String path = "enchantments.item-specific." + itemName + "." + enchantName;
            configManager.getConfig().set(path, null);
            plugin.saveConfig();
        }
    }

    /**
     * Remove all enchantment limits from a specific item
     */
    public void removeItemLimits(Material material) {
        String itemName = material.name().toUpperCase();
        itemEnchantLimits.remove(itemName);
        
        String path = "enchantments.item-specific." + itemName;
        configManager.getConfig().set(path, null);
        plugin.saveConfig();
    }

    /**
     * Check if an item has any specific enchantment limits
     */
    public boolean hasItemLimits(Material material) {
        String itemName = material.name().toUpperCase();
        return itemEnchantLimits.containsKey(itemName) && !itemEnchantLimits.get(itemName).isEmpty();
    }

    /**
     * Get all enchantment limits for a specific item
     */
    public Map<String, Integer> getItemEnchantmentLimits(Material material) {
        String itemName = material.name().toUpperCase();
        if (itemEnchantLimits.containsKey(itemName)) {
            return new HashMap<>(itemEnchantLimits.get(itemName));
        }
        return new HashMap<>();
    }

    public void reload() {
        configManager.reloadConfig();
        loadItemEnchantLimits();
        plugin.getLogger().info("✓ Item-specific enchantment limits reloaded!");
    }
}
