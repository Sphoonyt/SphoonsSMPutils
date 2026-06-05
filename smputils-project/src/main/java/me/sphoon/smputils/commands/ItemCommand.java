package me.sphoon.smputils.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.ItemManager;

public class ItemCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final ItemManager itemManager;
    private final ConfigManager configManager;

    public ItemCommand(JavaPlugin plugin, ItemManager itemManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("smputils.items.admin")) {
            sender.sendMessage(configManager.colorize("&cYou don't have permission!"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(configManager.colorize("&6=== Item Limiter ==="));
            sender.sendMessage(configManager.colorize("&e/itemcap set <material> <limit> &7- Set item limit"));
            sender.sendMessage(configManager.colorize("&e/itemcap remove <material> &7- Remove item limit"));
            sender.sendMessage(configManager.colorize("&e/itemcap list &7- List all item limits"));
            sender.sendMessage(configManager.colorize("&e/itemcap reload &7- Reload configuration"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "set":
                handleSet(sender, args);
                break;
            case "remove":
                handleRemove(sender, args);
                break;
            case "list":
                handleList(sender);
                break;
            case "reload":
                handleReload(sender);
                break;
            default:
                sender.sendMessage(configManager.colorize("&cUnknown subcommand!"));
        }
        return true;
    }

    private void handleSet(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(configManager.colorize("&cUsage: /itemcap set <material> <limit>"));
            return;
        }

        String materialName = args[1].toUpperCase();
        try {
            Material material = Material.valueOf(materialName);
            int limit = Integer.parseInt(args[2]);
            itemManager.setItemLimit(material, limit);
            
            if (limit == 0) {
                sender.sendMessage(configManager.colorize("&a✓ Banned " + materialName));
            } else {
                sender.sendMessage(configManager.colorize("&a✓ Set " + materialName + " limit to " + limit));
            }
        } catch (IllegalArgumentException e) {
            sender.sendMessage(configManager.colorize("&cUnknown material: " + materialName));
        } catch (NumberFormatException e) {
            sender.sendMessage(configManager.colorize("&cInvalid limit number!"));
        }
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.colorize("&cUsage: /itemcap remove <material>"));
            return;
        }

        String materialName = args[1].toUpperCase();
        try {
            Material material = Material.valueOf(materialName);
            itemManager.removeItemLimit(material);
            sender.sendMessage(configManager.colorize("&a✓ Removed limit for " + materialName));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(configManager.colorize("&cUnknown material: " + materialName));
        }
    }

    private void handleList(CommandSender sender) {
        sender.sendMessage(configManager.colorize("&6=== Item Limits ==="));
        var limits = itemManager.getItemLimits();
        if (limits.isEmpty()) {
            sender.sendMessage(configManager.colorize("&eNo limits set!"));
        } else {
            limits.forEach((material, limit) -> {
                if (limit == 0) {
                    sender.sendMessage(configManager.colorize("&e" + material.name() + "&7: &cBANNED"));
                } else {
                    sender.sendMessage(configManager.colorize("&e" + material.name() + "&7: &f" + limit));
                }
            });
        }
    }

    private void handleReload(CommandSender sender) {
        itemManager.reload();
        sender.sendMessage(configManager.colorize("&a✓ Item configuration reloaded!"));
    }
}
