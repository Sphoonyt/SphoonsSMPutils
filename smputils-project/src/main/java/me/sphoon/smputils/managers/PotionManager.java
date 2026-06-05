package me.sphoon.smputils.managers;

import org.bukkit.potion.PotionType;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public class PotionManager {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private Map<String, Integer> potionLimits;
    private int maxDuration;

    public PotionManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.potionLimits = new HashMap<>();
        loadPotionLimits();
    }

    private void loadPotionLimits() {
        potionLimits.clear();
        
        var potionConfig = configManager.getConfig().getConfigurationSection("potions.limits");
        if (potionConfig != null) {
            for (String key : potionConfig.getKeys(false)) {
                int level = configManager.getConfig().getInt("potions.limits." + key + ".max-level", -1);
                potionLimits.put(key.toLowerCase(), level);
            }
        }

        // Load duration limit
        boolean durationLimitEnabled = configManager.getConfig().getBoolean("potions.duration-limit.enabled", false);
        if (durationLimitEnabled) {
            maxDuration = configManager.getConfig().getInt("potions.duration-limit.max-minutes", 60) * 60 * 20; // Convert to ticks
        } else {
            maxDuration = Integer.MAX_VALUE;
        }
    }

    public boolean isPotionAllowed(String potionType) {
        if (!potionLimits.containsKey(potionType.toLowerCase())) {
            return true; // No limit set
        }

        return potionLimits.get(potionType.toLowerCase()) > 0;
    }

    public int getPotionLevel(String potionType) {
        return potionLimits.getOrDefault(potionType.toLowerCase(), -1);
    }

    public boolean isDurationValid(int duration) {
        return duration <= maxDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setPotionLimit(String potionType, int level) {
        potionLimits.put(potionType.toLowerCase(), level);
        configManager.setPotionLimit(potionType, level);
    }

    public void removePotionLimit(String potionType) {
        potionLimits.remove(potionType.toLowerCase());
        configManager.getConfig().set("potions.limits." + potionType, null);
        plugin.saveConfig();
    }

    public Map<String, Integer> getPotionLimits() {
        return new HashMap<>(potionLimits);
    }

    public void reload() {
        configManager.reloadConfig();
        loadPotionLimits();
        plugin.getLogger().info("✓ Potion limits reloaded!");
    }
}
