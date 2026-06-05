package me.sphoon.smputils.managers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private Map<Material, Integer> itemLimits;

    public ItemManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.itemLimits = new HashMap<>();
        loadItemLimits();
    }

    private void loadItemLimits() {
        itemLimits.clear();
        
        var itemsConfig = configManager.getConfig().getConfigurationSection("items.limits");
        if (itemsConfig != null) {
            for (String key : itemsConfig.getKeys(false)) {
                int limit = configManager.getConfig().getInt("items.limits." + key, -1);
                try {
                    Material material = Material.valueOf(key.toUpperCase());
                    itemLimits.put(material, limit);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Unknown material: " + key);
                }
            }
        }
    }

    public boolean isItemAllowed(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return true;
        }

        if (!itemLimits.containsKey(item.getType())) {
            return true; // No limit set
        }

        int limit = itemLimits.get(item.getType());
        if (limit == 0) {
            return false; // Item is banned
        }

        return item.getAmount() <= limit;
    }

    public int getItemLimit(Material material) {
        return itemLimits.getOrDefault(material, -1);
    }

    public int getItemLimit(ItemStack item) {
        return getItemLimit(item.getType());
    }

    public boolean isItemBanned(Material material) {
        return itemLimits.getOrDefault(material, -1) == 0;
    }

    public void setItemLimit(Material material, int limit) {
        itemLimits.put(material, limit);
        configManager.setItemLimit(material.name(), limit);
    }

    public void removeItemLimit(Material material) {
        itemLimits.remove(material);
        configManager.getConfig().set("items.limits." + material.name(), null);
        plugin.saveConfig();
    }

    public Map<Material, Integer> getItemLimits() {
        return new HashMap<>(itemLimits);
    }

    public void reload() {
        configManager.reloadConfig();
        loadItemLimits();
        plugin.getLogger().info("✓ Item limits reloaded!");
    }
}
