package me.sphoon.smputils.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionMeta;
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

            // Check if it's a potion
            if (item.getItemMeta() instanceof PotionMeta) {
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                
                // Check custom potions (splash, lingering, etc)
                if (meta.hasCustomEffects()) {
                    for (var effect : meta.getCustomEffects()) {
                        String potionType = effect.getType().getName().toLowerCase();
                        
                        if (!potionManager.isPotionAllowed(potionType)) {
                            event.setCancelled(true);
                            return;
                        }

                        // Check duration limit
                        int duration = effect.getDuration();
                        if (!potionManager.isDurationValid(duration)) {
                            event.setCancelled(true);
                            return;
                        }

                        // Check amplifier (effect level)
                        int amplifier = effect.getAmplifier() + 1; // Convert 0-based to 1-based
                        int maxLevel = potionManager.getPotionLevel(potionType);
                        if (maxLevel > 0 && amplifier > maxLevel) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!(item.getItemMeta() instanceof PotionMeta)) {
            return;
        }

        PotionMeta meta = (PotionMeta) item.getItemMeta();

        // Check custom effects
        if (meta.hasCustomEffects()) {
            for (var effect : meta.getCustomEffects()) {
                String potionType = effect.getType().getName().toLowerCase();

                if (!potionManager.isPotionAllowed(potionType)) {
                    event.setCancelled(true);
                    player.sendMessage(configManager.colorize(
                        "&c✗ This potion type is not allowed!"
                    ));
                    return;
                }

                // Check amplifier
                int amplifier = effect.getAmplifier() + 1;
                int maxLevel = potionManager.getPotionLevel(potionType);
                if (maxLevel > 0 && amplifier > maxLevel) {
                    event.setCancelled(true);
                    player.sendMessage(configManager.colorize(
                        "&c✗ This potion level exceeds the maximum allowed!"
                    ));
                    return;
                }
            }
        }
    }
}
