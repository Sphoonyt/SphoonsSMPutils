package me.sphoon.smputils;

import org.bukkit.plugin.java.JavaPlugin;
import me.sphoon.smputils.commands.*;
import me.sphoon.smputils.listeners.*;
import me.sphoon.smputils.managers.*;

public class SphoonsSMPutils extends JavaPlugin {

    private ConfigManager configManager;
    private EnchantmentManager enchantmentManager;
    private ItemEnchantmentLimitManager itemEnchantmentLimitManager;
    private ItemManager itemManager;
    private PotionManager potionManager;
    private MaceManager maceManager;
    private RestockManager restockManager;

    @Override
    public void onEnable() {
        getLogger().info("================================================");
        getLogger().info("Sphoon's SMP Utilities v" + getDescription().getVersion());
        getLogger().info("Loading unified SMP utility plugin...");
        getLogger().info("================================================");

        // Initialize configuration
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // Initialize managers
        initializeManagers();

        // Register commands
        registerCommands();

        // Register listeners
        registerListeners();

        getLogger().info("✓ All modules loaded successfully!");
        getLogger().info("================================================");
    }

    private void initializeManagers() {
        enchantmentManager = new EnchantmentManager(this, configManager);
        itemEnchantmentLimitManager = new ItemEnchantmentLimitManager(this, configManager);
        itemManager = new ItemManager(this, configManager);
        potionManager = new PotionManager(this, configManager);
        maceManager = new MaceManager(this, configManager);
        restockManager = new RestockManager(this, configManager);

        getLogger().info("✓ Enchantments Limiter initialized");
        getLogger().info("✓ Item-Specific Enchantments Limiter initialized");
        getLogger().info("✓ Items Limiter initialized");
        getLogger().info("✓ Potion Limiter initialized");
        getLogger().info("✓ MultiMace initialized");
        getLogger().info("✓ Instant Restock initialized");
    }

    private void registerCommands() {
        // Enchantments command
        if (configManager.isEnchantmentsEnabled()) {
            getCommand("enchantlimit").setExecutor(new EnchantmentCommand(this, enchantmentManager, configManager));
        }

        // Items command
        if (configManager.isItemsEnabled()) {
            getCommand("itemcap").setExecutor(new ItemCommand(this, itemManager, configManager));
        }

        // Potion command
        if (configManager.isProtionsEnabled()) {
            getCommand("potionlimit").setExecutor(new PotionCommand(this, potionManager, configManager));
        }

        // Mace commands
        if (configManager.isMacesEnabled()) {
            getCommand("maces").setExecutor(new MaceCommand(this, maceManager, configManager));
            getCommand("getuntrackedmace").setExecutor(new GetUntrackedMaceCommand(this, maceManager, configManager));
            getCommand("clearuntrackedmaces").setExecutor(new ClearUntrackedMacesCommand(this, maceManager, configManager));
        }

        // Restock command
        if (configManager.isRestockEnabled()) {
            getCommand("restock").setExecutor(new RestockCommand(this, restockManager, configManager));
        }

        // ItemEdit command
        if (configManager.isItemEditEnabled()) {
            getCommand("itemedit").setExecutor(new ItemEditCommand(this, configManager));
        }

        getLogger().info("✓ Commands registered");
    }

    private void registerListeners() {
        // Enchantments listeners
        if (configManager.isEnchantmentsEnabled()) {
            getServer().getPluginManager().registerEvents(new EnchantmentListener(enchantmentManager, configManager, this), this);
        }

        // Items listeners
        if (configManager.isItemsEnabled()) {
            getServer().getPluginManager().registerEvents(new ItemListener(itemManager, configManager), this);
        }

        // Potion listeners
        if (configManager.isProtionsEnabled()) {
            getServer().getPluginManager().registerEvents(new PotionListener(potionManager, configManager), this);
        }

        // Mace listeners
        if (configManager.isMacesEnabled()) {
            getServer().getPluginManager().registerEvents(new MaceListener(maceManager, configManager), this);
            getServer().getPluginManager().registerEvents(new MaceCraftingListener(maceManager, configManager), this);
        }

        // Restock listeners
        if (configManager.isRestockEnabled()) {
            getServer().getPluginManager().registerEvents(new RestockListener(restockManager, configManager), this);
        }

        getLogger().info("✓ Event listeners registered");
    }

    @Override
    public void onDisable() {
        getLogger().info("Sphoon's SMP Utilities disabled!");
    }

    // Getter methods
    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EnchantmentManager getEnchantmentManager() {
        return enchantmentManager;
    }

    public ItemEnchantmentLimitManager getItemEnchantmentLimitManager() {
        return itemEnchantmentLimitManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public PotionManager getPotionManager() {
        return potionManager;
    }

    public MaceManager getMaceManager() {
        return maceManager;
    }

    public RestockManager getRestockManager() {
        return restockManager;
    }
}
