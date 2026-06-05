package me.sphoon.smputils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.RestockManager;

public class RestockCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final RestockManager restockManager;
    private final ConfigManager configManager;

    public RestockCommand(JavaPlugin plugin, RestockManager restockManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.restockManager = restockManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("smputils.restock.admin")) {
            sender.sendMessage(configManager.colorize("&cYou don't have permission!"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(configManager.colorize("&6=== Instant Restock ==="));
            sender.sendMessage(configManager.colorize("&e/restock enable &7- Enable instant restocking"));
            sender.sendMessage(configManager.colorize("&e/restock disable &7- Disable instant restocking"));
            sender.sendMessage(configManager.colorize("&e/restock status &7- Check restocking status"));
            sender.sendMessage(configManager.colorize("&e/restock reload &7- Reload configuration"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "enable":
                handleEnable(sender);
                break;
            case "disable":
                handleDisable(sender);
                break;
            case "status":
                handleStatus(sender);
                break;
            case "reload":
                handleReload(sender);
                break;
            default:
                sender.sendMessage(configManager.colorize("&cUnknown subcommand!"));
        }
        return true;
    }

    private void handleEnable(CommandSender sender) {
        restockManager.setInstantRestock(true);
        sender.sendMessage(configManager.colorize("&a✓ Instant restocking enabled!"));
    }

    private void handleDisable(CommandSender sender) {
        restockManager.setInstantRestock(false);
        sender.sendMessage(configManager.colorize("&c✓ Instant restocking disabled!"));
    }

    private void handleStatus(CommandSender sender) {
        sender.sendMessage(configManager.colorize("&6=== Restock Status ==="));
        sender.sendMessage(configManager.colorize("&eVillagers: &f" + (restockManager.isVillagerRestockEnabled() ? "&aEnabled" : "&cDisabled")));
        sender.sendMessage(configManager.colorize("&eLecterns: &f" + (restockManager.isLecternRestockEnabled() ? "&aEnabled" : "&cDisabled")));
        sender.sendMessage(configManager.colorize("&eInstant Restock: &f" + (restockManager.isInstantRestock() ? "&aEnabled" : "&cDisabled")));
    }

    private void handleReload(CommandSender sender) {
        restockManager.reload();
        sender.sendMessage(configManager.colorize("&a✓ Restock configuration reloaded!"));
    }
}
