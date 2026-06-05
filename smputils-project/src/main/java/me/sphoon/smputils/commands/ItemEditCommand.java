package me.sphoon.smputils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import me.sphoon.smputils.managers.ConfigManager;

public class ItemEditCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;

    public ItemEditCommand(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("smputils.itemedit.use")) {
            sender.sendMessage(configManager.colorize("&cYou don't have permission!"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.colorize("&cOnly players can use this command!"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(configManager.colorize("&6=== Item Editor ==="));
            sender.sendMessage(configManager.colorize("&e/itemedit name <name> &7- Set item display name"));
            sender.sendMessage(configManager.colorize("&e/itemedit lore add <text> &7- Add lore line"));
            sender.sendMessage(configManager.colorize("&e/itemedit lore remove <line> &7- Remove lore line"));
            sender.sendMessage(configManager.colorize("&e/itemedit lore clear &7- Clear all lore"));
            sender.sendMessage(configManager.colorize("&e/itemedit enchant <enchantment> <level> &7- Add enchantment"));
            sender.sendMessage(configManager.colorize("&e/itemedit enchant remove <enchantment> &7- Remove enchantment"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "name":
                handleName(player, args);
                break;
            case "lore":
                handleLore(player, args);
                break;
            case "enchant":
                handleEnchant(player, args);
                break;
            default:
                player.sendMessage(configManager.colorize("&cUnknown subcommand!"));
        }
        return true;
    }

    private void handleName(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(configManager.colorize("&cUsage: /itemedit name <name>"));
            return;
        }

        if (player.getInventory().getItemInMainHand().getType().isAir()) {
            player.sendMessage(configManager.colorize("&cYou must hold an item!"));
            return;
        }

        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            nameBuilder.append(args[i]).append(" ");
        }
        String name = nameBuilder.toString().trim();

        var itemInHand = player.getInventory().getItemInMainHand();
        var meta = itemInHand.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(configManager.colorize(name));
            itemInHand.setItemMeta(meta);
            player.sendMessage(configManager.colorize("&a✓ Item name set to: " + name));
        }
    }

    private void handleLore(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(configManager.colorize("&cUsage: /itemedit lore <add|remove|clear> [text|line]"));
            return;
        }

        if (player.getInventory().getItemInMainHand().getType().isAir()) {
            player.sendMessage(configManager.colorize("&cYou must hold an item!"));
            return;
        }

        var itemInHand = player.getInventory().getItemInMainHand();
        var meta = itemInHand.getItemMeta();
        if (meta == null) {
            player.sendMessage(configManager.colorize("&cCannot edit lore for this item!"));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "add":
                if (args.length < 3) {
                    player.sendMessage(configManager.colorize("&cUsage: /itemedit lore add <text>"));
                    return;
                }
                StringBuilder loreBuilder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    loreBuilder.append(args[i]).append(" ");
                }
                var lore = meta.getLore();
                if (lore == null) {
                    lore = new java.util.ArrayList<>();
                }
                lore.add(configManager.colorize(loreBuilder.toString().trim()));
                meta.setLore(lore);
                itemInHand.setItemMeta(meta);
                player.sendMessage(configManager.colorize("&a✓ Lore line added!"));
                break;

            case "remove":
                if (args.length < 3) {
                    player.sendMessage(configManager.colorize("&cUsage: /itemedit lore remove <line>"));
                    return;
                }
                try {
                    int lineNum = Integer.parseInt(args[2]) - 1;
                    lore = meta.getLore();
                    if (lore != null && lineNum >= 0 && lineNum < lore.size()) {
                        lore.remove(lineNum);
                        meta.setLore(lore);
                        itemInHand.setItemMeta(meta);
                        player.sendMessage(configManager.colorize("&a✓ Lore line removed!"));
                    } else {
                        player.sendMessage(configManager.colorize("&cInvalid line number!"));
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(configManager.colorize("&cInvalid line number!"));
                }
                break;

            case "clear":
                meta.setLore(new java.util.ArrayList<>());
                itemInHand.setItemMeta(meta);
                player.sendMessage(configManager.colorize("&a✓ All lore cleared!"));
                break;

            default:
                player.sendMessage(configManager.colorize("&cUnknown lore subcommand!"));
        }
    }

    private void handleEnchant(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(configManager.colorize("&cUsage: /itemedit enchant <enchantment> <level> or remove <enchantment>"));
            return;
        }

        if (player.getInventory().getItemInMainHand().getType().isAir()) {
            player.sendMessage(configManager.colorize("&cYou must hold an item!"));
            return;
        }

        var itemInHand = player.getInventory().getItemInMainHand();
        var meta = itemInHand.getItemMeta();
        if (meta == null) {
            player.sendMessage(configManager.colorize("&cCannot enchant this item!"));
            return;
        }

        if (args[1].toLowerCase().equals("remove")) {
            if (args.length < 3) {
                player.sendMessage(configManager.colorize("&cUsage: /itemedit enchant remove <enchantment>"));
                return;
            }
            try {
                var enchant = org.bukkit.enchantments.Enchantment.getByName(args[2].toUpperCase());
                if (enchant != null) {
                    meta.removeEnchant(enchant);
                    itemInHand.setItemMeta(meta);
                    player.sendMessage(configManager.colorize("&a✓ Enchantment removed!"));
                } else {
                    player.sendMessage(configManager.colorize("&cUnknown enchantment!"));
                }
            } catch (Exception e) {
                player.sendMessage(configManager.colorize("&cError removing enchantment!"));
            }
        } else {
            if (args.length < 3) {
                player.sendMessage(configManager.colorize("&cUsage: /itemedit enchant <enchantment> <level>"));
                return;
            }
            try {
                var enchant = org.bukkit.enchantments.Enchantment.getByName(args[1].toUpperCase());
                int level = Integer.parseInt(args[2]);
                if (enchant != null) {
                    meta.addEnchant(enchant, level, true);
                    itemInHand.setItemMeta(meta);
                    player.sendMessage(configManager.colorize("&a✓ Enchantment added: " + args[1] + " " + level));
                } else {
                    player.sendMessage(configManager.colorize("&cUnknown enchantment!"));
                }
            } catch (NumberFormatException e) {
                player.sendMessage(configManager.colorize("&cInvalid enchantment level!"));
            }
        }
    }
}
