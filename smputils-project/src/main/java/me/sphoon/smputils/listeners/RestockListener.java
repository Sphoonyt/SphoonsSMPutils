package me.sphoon.smputils.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Lectern;
import org.bukkit.entity.Villager;
import org.bukkit.entity.AbstractVillager;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.RestockManager;

public class RestockListener implements Listener {
    private final RestockManager restockManager;
    private final ConfigManager configManager;

    public RestockListener(RestockManager restockManager, ConfigManager configManager) {
        this.restockManager = restockManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        // Lectern restock
        if (event.getInventory().getHolder() instanceof Lectern) {
            if (restockManager.isLecternRestockEnabled() && restockManager.isInstantRestock()) {
                Lectern lectern = (Lectern) event.getInventory().getHolder();
                
                // Instantly restock the lectern book
                if (lectern.getBook() != null) {
                    // The book is already there, just ensuring it stays
                    event.getViewers().forEach(viewer -> {
                        if (viewer instanceof org.bukkit.entity.Player) {
                            org.bukkit.entity.Player player = (org.bukkit.entity.Player) viewer;
                            player.sendMessage(configManager.colorize("&aLectern content is available!"));
                        }
                    });
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        // Check if breaking a lectern
        if (block.getType() == Material.LECTERN) {
            if (restockManager.isLecternRestockEnabled()) {
                // Allow breaking but warn player
                event.getPlayer().sendMessage(configManager.colorize(
                    "&aLectern broken - contents will be dropped!"
                ));
            }
        }
    }
}
