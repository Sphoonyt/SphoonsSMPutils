package me.sphoon.smputils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.PotionManager;

public class PotionCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final PotionManager potionManager;
    private final ConfigManager configManager;

    public PotionCommand(JavaPlugin plugin, PotionManager potionManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.potionManager = potionManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("smputils.potion.admin")) {
            sender.sendMessage(configManager.colorize("&cYou don't have permission!"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(configManager.colorize("&6=== Potion Limiter ==="));
            sender.sendMessage(configManager.colorize("&e/potionlimit set <potion> <level> &7- Set potion limit"));
            sender.sendMessage(configManager.colorize("&e/potionlimit remove <potion> &7- Remove potion limit"));
            sender.sendMessage(configManager.colorize("&e/potionlimit list &7- List all potion limits"));
            sender.sendMessage(configManager.colorize("&e/potionlimit reload &7- Reload configuration"));
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
            sender.sendMessage(configManager.colorize("&cUsage: /potionlimit set <potion> <level>"));
            return;
        }

        String potion = args[1];
        try {
            int level = Integer.parseInt(args[2]);
            potionManager.setPotionLimit(potion, level);
            sender.sendMessage(configManager.colorize("&a✓ Set " + potion + " limit to level " + level));
        } catch (NumberFormatException e) {
            sender.sendMessage(configManager.colorize("&cInvalid level number!"));
        }
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(configManager.colorize("&cUsage: /potionlimit remove <potion>"));
            return;
        }

        String potion = args[1];
        potionManager.removePotionLimit(potion);
        sender.sendMessage(configManager.colorize("&a✓ Removed limit for " + potion));
    }

    private void handleList(CommandSender sender) {
        sender.sendMessage(configManager.colorize("&6=== Potion Limits ==="));
        var limits = potionManager.getPotionLimits();
        if (limits.isEmpty()) {
            sender.sendMessage(configManager.colorize("&eNo limits set!"));
        } else {
            limits.forEach((potion, level) ->
                sender.sendMessage(configManager.colorize("&e" + potion + "&7: &f" + level))
            );
        }
    }

    private void handleReload(CommandSender sender) {
        potionManager.reload();
        sender.sendMessage(configManager.colorize("&a✓ Potion configuration reloaded!"));
    }
}
