package me.sphoon.smputils.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import me.sphoon.smputils.SphoonsSMPutils;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.EnchantmentManager;
import me.sphoon.smputils.managers.ItemEnchantmentLimitManager;

public class EnchantmentCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final EnchantmentManager enchantmentManager;
    private final ItemEnchantmentLimitManager itemEnchantmentLimitManager;
    private final ConfigManager configManager;

    public EnchantmentCommand(JavaPlugin plugin, EnchantmentManager enchantmentManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.enchantmentManager = enchantmentManager;
        this.itemEnchantmentLimitManager = ((SphoonsSMPutils) plugin).getItemEnchantmentLimitManager();
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("smputils.enchant.admin")) {
            sender.sendMessage(configManager.colorize("&cYou don't have permission!"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(configManager.colorize("&6=== Enchantment Limiter ==="));
            sender.sendMessage(configManager.colorize("&e/enchantlimit set <enchantment> <level> &7- Set global enchantment limit"));
            sender.sendMessage(configManager.colorize("&e/enchantlimit item <item> <enchantment> <level> &7- Set item-specific limit"));
            sender.sendMessage(configManager.colorize("&e/enchantlimit remove <enchantment> &7- Remove enchantment limit"));
            sender.sendMessage(configManager.colorize("&e/enchantlimit item-remove <item> <enchantment> &7- Remove item-specific limit"));
            sender.sendMessage(configManager.colorize("&e/enchantlimit list &7- List all enchantment limits"));
            sender.sendMessage(configManager.colorize("&e/enchantlimit item-list <item> &7- List item-specific limits"));
            sender.sendMessage(configManager.colorize("&e/enchantlimit reload &7- Reload configuration"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "set":
                handleSet(sender, args);
                break;
            case "item":
                handleItemSet(sender, args);
                break;
            case "remove":
                handleRemove(sender, args);
                break;
            case "item-remove":
                handleItemRemove(sender, args);
                break;
            case "list":
                handleList(sender);
                break;
            case "item-list":
                handleItemList(sender, args);
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
            sender.sendMessage(configManager.colorize("&cUsage: /enchantlimit set <enchantment> <level>"));
            return;
        }

        String enchantment = args[1];
        try {
            int level = Integer.parseInt(args[2]);
            enchantmentManager.setEnchantmentLimit(enchantment, level);
            sender.sendMessage(configManager.colorize("&a✓ Set global " + enchantment + " limit to level " + level));
        } catch (NumberFormatException e) {
            sender.sendMessage(configManager.colorize("&cInvalid level number!"));
        }
    }

    private void handleItemSet(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(configManager.colorize("&cUsage: /enchantlimit item <item> <enchantment> <level>"));
            sender.sendMessage(configManager.colorize("&7Example: /enchantlimit item diamond_chestplate protection 3"));
            return;
        }

        String itemName = args[1].toUpperCase();
        String enchantment = args[2];
        
        try {
            Material material = Material.valueOf(itemName);
            int level = Integer.parseInt(args[3]);
            
            itemEnchantmentLimitManager.setItemEnchantmentLimit(material, enchantment, level);
            sender.sendMessage(configManager.colorize("&a✓ Set " + itemName + " &a" + enchantment + " &alimit to level " + level));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(configManager.colorize("&cUnknown item: " + itemName));
        } catch (NumberFormatException e) {
            sender.sendMessage(configManager.colorize("&cInvalid level number!"));
        }
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.colorize("&cUsage: /enchantlimit remove <enchantment>"));
            return;
        }

        String enchantment = args[1];
        enchantmentManager.removeEnchantmentLimit(enchantment);
        sender.sendMessage(configManager.colorize("&a✓ Removed global limit for " + enchantment));
    }

    private void handleItemRemove(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(configManager.colorize("&cUsage: /enchantlimit item-remove <item> <enchantment>"));
            return;
        }

        String itemName = args[1].toUpperCase();
        String enchantment = args[2];
        
        try {
            Material material = Material.valueOf(itemName);
            itemEnchantmentLimitManager.removeItemEnchantmentLimit(material, enchantment);
            sender.sendMessage(configManager.colorize("&a✓ Removed " + itemName + " " + enchantment + " limit"));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(configManager.colorize("&cUnknown item: " + itemName));
        }
    }

    private void handleList(CommandSender sender) {
        sender.sendMessage(configManager.colorize("&6=== Global Enchantment Limits ==="));
        var limits = enchantmentManager.getEnchantmentLimits();
        if (limits.isEmpty()) {
            sender.sendMessage(configManager.colorize("&eNo global limits set!"));
        } else {
            limits.forEach((enchantment, level) ->
                sender.sendMessage(configManager.colorize("&e" + enchantment + "&7: &f" + level))
            );
        }
        
        sender.sendMessage(configManager.colorize("&6=== Item-Specific Enchantment Limits ==="));
        var itemLimits = itemEnchantmentLimitManager.getAllItemEnchantLimits();
        if (itemLimits.isEmpty()) {
            sender.sendMessage(configManager.colorize("&eNo item-specific limits set!"));
        } else {
            itemLimits.forEach((item, enchants) -> {
                sender.sendMessage(configManager.colorize("&e" + item + "&7:"));
                enchants.forEach((enchant, level) ->
                    sender.sendMessage(configManager.colorize("  &7→ &e" + enchant + "&7: &f" + level))
                );
            });
        }
    }

    private void handleItemList(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.colorize("&cUsage: /enchantlimit item-list <item>"));
            return;
        }

        String itemName = args[1].toUpperCase();
        
        try {
            Material material = Material.valueOf(itemName);
            sender.sendMessage(configManager.colorize("&6=== " + itemName + " Enchantment Limits ==="));
            
            var limits = itemEnchantmentLimitManager.getItemEnchantmentLimits(material);
            if (limits.isEmpty()) {
                sender.sendMessage(configManager.colorize("&eNo item-specific limits for " + itemName));
            } else {
                limits.forEach((enchantment, level) ->
                    sender.sendMessage(configManager.colorize("&e" + enchantment + "&7: &f" + level))
                );
            }
        } catch (IllegalArgumentException e) {
            sender.sendMessage(configManager.colorize("&cUnknown item: " + itemName));
        }
    }

    private void handleReload(CommandSender sender) {
        enchantmentManager.reload();
        itemEnchantmentLimitManager.reload();
        sender.sendMessage(configManager.colorize("&a✓ Enchantment configuration reloaded!"));
    }
}
