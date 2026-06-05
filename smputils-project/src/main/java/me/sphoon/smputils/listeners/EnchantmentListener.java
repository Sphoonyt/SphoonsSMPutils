package me.sphoon.smputils.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import me.sphoon.smputils.SphoonsSMPutils;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.EnchantmentManager;
import me.sphoon.smputils.managers.ItemEnchantmentLimitManager;

public class EnchantmentListener implements Listener {
    private final EnchantmentManager enchantmentManager;
    private final ItemEnchantmentLimitManager itemEnchantmentLimitManager;
    private final ConfigManager configManager;

    public EnchantmentListener(EnchantmentManager enchantmentManager, ConfigManager configManager, SphoonsSMPutils plugin) {
        this.enchantmentManager = enchantmentManager;
        this.itemEnchantmentLimitManager = plugin.getItemEnchantmentLimitManager();
        this.configManager = configManager;
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        ItemStack item = event.getItem();
        
        // Check if any enchantments exceed the limit
        for (Enchantment enchantment : event.getEnchantsToAdd().keySet()) {
            int level = event.getEnchantsToAdd().get(enchantment);
            
            // Check item-specific limit first
            if (!itemEnchantmentLimitManager.isItemEnchantmentAllowed(item.getType(), enchantment, level)) {
                int maxLevel = itemEnchantmentLimitManager.getItemEnchantmentLimit(item.getType(), enchantment.getKey().getKey());
                event.setCancelled(true);
                event.getEnchanter().sendMessage(configManager.colorize(
                    "&c✗ " + enchantment.getKey().getKey() + " cannot exceed level " + maxLevel + " on this item"
                ));
                return;
            }
            
            // Check global limit
            if (!enchantmentManager.isEnchantmentAllowed(enchantment, level)) {
                int maxLevel = enchantmentManager.getMaxLevel(enchantment);
                event.setCancelled(true);
                event.getEnchanter().sendMessage(configManager.colorize(
                    "&c✗ " + enchantment.getKey().getKey() + " cannot exceed level " + maxLevel
                ));
                return;
            }
        }
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();
        if (result == null || result.getType().isAir()) {
            return;
        }

        // Check enchantments on crafted items
        var meta = result.getItemMeta();
        if (meta != null && meta.hasEnchants()) {
            for (Enchantment enchantment : meta.getEnchants().keySet()) {
                int level = meta.getEnchantLevel(enchantment);
                
                // Check item-specific limit first
                if (!itemEnchantmentLimitManager.isItemEnchantmentAllowed(result.getType(), enchantment, level)) {
                    event.getInventory().setResult(null);
                    return;
                }
                
                // Check global limit
                if (!enchantmentManager.isEnchantmentAllowed(enchantment, level)) {
                    event.getInventory().setResult(null);
                    return;
                }
            }
        }
    }
}
