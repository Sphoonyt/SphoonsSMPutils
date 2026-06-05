package me.sphoon.smputils.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.MaceManager;

public class MaceListener implements Listener {
    private final MaceManager maceManager;
    private final ConfigManager configManager;

    public MaceListener(MaceManager maceManager, ConfigManager configManager) {
        this.maceManager = maceManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() != Material.MACE) {
            return;
        }

        // Check if trying to put mace in a container
        if (maceManager.isContainerStorageBlocked()) {
            InventoryType invType = event.getInventory().getType();
            
            if (invType == InventoryType.CHEST || 
                invType == InventoryType.FURNACE || 
                invType == InventoryType.HOPPER ||
                invType == InventoryType.DISPENSER ||
                invType == InventoryType.DROPPER ||
                invType == InventoryType.SHULKER_BOX ||
                invType == InventoryType.BARREL) {
                
                event.setCancelled(true);
                player.sendMessage(configManager.colorize(
                    "&c✗ Maces cannot be stored in containers!"
                ));
            }
        }
    }

    @EventHandler
    public void onPlayerDropMace(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();

        if (item.getType() != Material.MACE) {
            return;
        }

        // Unregister the mace when dropped
        maceManager.unregisterMace(player.getUniqueId());
        
        player.sendMessage(configManager.colorize(
            "&c✗ Your mace has been destroyed due to being dropped!"
        ));
        
        event.getPlayer().getServer().broadcastMessage(configManager.colorize(
            configManager.getMessage("maces", "destroyed-broadcast")
                .replace("%lastHolder%", player.getName())
                .replace("%current%", String.valueOf(maceManager.getMaceCount()))
                .replace("%max%", String.valueOf(maceManager.getMaxMaces()))
        ));
        
        // Remove the item
        event.getItemDrop().remove();
    }
}
