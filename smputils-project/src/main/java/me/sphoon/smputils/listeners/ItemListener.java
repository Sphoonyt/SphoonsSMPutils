package me.sphoon.smputils.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.ItemManager;

public class ItemListener implements Listener {
    private final ItemManager itemManager;
    private final ConfigManager configManager;

    public ItemListener(ItemManager itemManager, ConfigManager configManager) {
        this.itemManager = itemManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        if (player.hasPermission("smputils.items.bypass")) {
            return;
        }

        ItemStack item = event.getItem().getItemStack();

        // Check if item is banned
        if (itemManager.isItemBanned(item.getType())) {
            event.setCancelled(true);
            player.sendMessage(configManager.colorize("&c✗ The item " + item.getType().name() + " is banned!"));
            return;
        }

        // Check if pickup would exceed limit
        int limit = itemManager.getItemLimit(item);
        if (limit > 0) {
            int playerAmount = getPlayerItemCount(player, item.getType());
            if (playerAmount + item.getAmount() > limit) {
                event.setCancelled(true);
                player.sendMessage(configManager.colorize(
                    "&c✗ You cannot carry more than " + limit + " " + item.getType().name()
                ));
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (player.hasPermission("smputils.items.bypass")) {
            return;
        }

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType().isAir()) {
            return;
        }

        // Check if item is banned
        if (itemManager.isItemBanned(item.getType())) {
            event.setCancelled(true);
            player.sendMessage(configManager.colorize("&c✗ You cannot interact with banned items!"));
            return;
        }

        // Check item limit on shift-click
        if (event.isShiftClick()) {
            int limit = itemManager.getItemLimit(item);
            if (limit > 0) {
                int playerAmount = getPlayerItemCount(player, item.getType());
                if (playerAmount + item.getAmount() > limit) {
                    event.setCancelled(true);
                    player.sendMessage(configManager.colorize(
                        "&c✗ You cannot carry more than " + limit + " " + item.getType().name()
                    ));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();

        // Check if item is banned
        if (itemManager.isItemBanned(item.getType())) {
            event.setCancelled(true);
            player.sendMessage(configManager.colorize("&c✗ You cannot drop banned items!"));
        }
    }

    private int getPlayerItemCount(Player player, org.bukkit.Material material) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        return count;
    }
}
