package me.sphoon.smputils.managers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class MaceManager {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private int maxMaces;
    private int currentMaces;
    private Map<UUID, String> maceholders; // UUID -> Player name
    private boolean allowEnchanting;
    private boolean preventContainerStorage;

    public MaceManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.maceholders = new HashMap<>();
        loadMaceConfig();
    }

    private void loadMaceConfig() {
        maxMaces = configManager.getConfig().getInt("maces.allowed-maces", 3);
        allowEnchanting = configManager.getConfig().getBoolean("maces.allow-mace-enchanting", true);
        preventContainerStorage = configManager.getConfig().getBoolean("maces.prevent-container-storage", true);
        currentMaces = 0;
    }

    public boolean canCraftMace() {
        return currentMaces < maxMaces;
    }

    public int getMaceCount() {
        return currentMaces;
    }

    public int getMaxMaces() {
        return maxMaces;
    }

    public void registerMace(UUID holderUUID, String holderName) {
        maceholders.put(holderUUID, holderName);
        currentMaces++;
    }

    public void unregisterMace(UUID holderUUID) {
        if (maceholders.containsKey(holderUUID)) {
            maceholders.remove(holderUUID);
            if (currentMaces > 0) {
                currentMaces--;
            }
        }
    }

    public void destroyMace(UUID lastHolderUUID) {
        unregisterMace(lastHolderUUID);
    }

    public boolean isMaceHolder(UUID playerUUID) {
        return maceholders.containsKey(playerUUID);
    }

    public String getLastMaceHolder() {
        if (maceholders.isEmpty()) {
            return "Unknown";
        }
        return maceholders.values().iterator().next();
    }

    public Map<UUID, String> getAllMaceHolders() {
        return new HashMap<>(maceholders);
    }

    public boolean isEnchantingAllowed() {
        return allowEnchanting;
    }

    public boolean isContainerStorageBlocked() {
        return preventContainerStorage;
    }

    public void setMaxMaces(int max) {
        maxMaces = max;
        configManager.getConfig().set("maces.allowed-maces", max);
        plugin.saveConfig();
    }

    public void clearUntrackedMaces() {
        maceholders.clear();
        currentMaces = 0;
    }

    public void reload() {
        configManager.reloadConfig();
        loadMaceConfig();
        plugin.getLogger().info("✓ Mace configuration reloaded!");
    }
}
