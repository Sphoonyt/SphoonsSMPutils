package me.sphoon.smputils.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.PotionManager;

public class PotionListener implements Listener {
    private final PotionManager potionManager;
    private final ConfigManager configManager;

    public PotionListener(PotionManager potionManager, ConfigManager configManager) {
        this.potionManager = potionManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        // Check each brewed potion
        for (int i = 0; i < event.getContents().getContents().length; i++) {
            ItemStack item = event.getContents().getContents()[i];
            
            if (item == null || item.getType().isAir()) {
                continue;
            }

            // Check if it has item meta with potion effects
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasCustomModelData()) {
                // Item has custom data, check if it's a potion
                if (meta.hasCustomModelData() || item.getType().name().contains("POTION")) {
                    // Potion validation logic
                    if (meta.hasAttributeModifiers() || item.getType().name().contains("POTION")) {
                        // Allow for now - basic validation
                        continue;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if it's a potion item
        if (!item.getType().name().contains("POTION")) {
            return;
        }

        // For now, allow potion consumption
        // Additional checks can be added here based on PotionManager
    }
}
