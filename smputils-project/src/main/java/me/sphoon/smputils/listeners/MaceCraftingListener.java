package me.sphoon.smputils.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.MaceManager;

public class MaceCraftingListener implements Listener {
    private final MaceManager maceManager;
    private final ConfigManager configManager;

    public MaceCraftingListener(MaceManager maceManager, ConfigManager configManager) {
        this.maceManager = maceManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onCraftMace(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();

        // Check if crafting a mace
        if (result.getType() != Material.MACE) {
            return;
        }

        Player crafter = (Player) event.getWhoClicked();

        // Check if mace limit is reached
        if (!maceManager.canCraftMace()) {
            event.setCancelled(true);
            crafter.sendMessage(configManager.colorize(
                configManager.getMessage("maces", "limit-reached")
            ));
            return;
        }

        // Register the mace
        maceManager.registerMace(crafter.getUniqueId(), crafter.getName());

        // Broadcast mace craft
        crafter.getServer().broadcastMessage(configManager.colorize(
            configManager.getMessage("maces", "crafted-broadcast")
                .replace("%player%", crafter.getName())
                .replace("%amount%", String.valueOf(event.getRecipe().getResult().getAmount()))
                .replace("%current%", String.valueOf(maceManager.getMaceCount()))
                .replace("%max%", String.valueOf(maceManager.getMaxMaces()))
        ));
    }
}
