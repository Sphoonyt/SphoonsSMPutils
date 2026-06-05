package me.sphoon.smputils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.MaceManager;

public class MaceCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final MaceManager maceManager;
    private final ConfigManager configManager;

    public MaceCommand(JavaPlugin plugin, MaceManager maceManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.maceManager = maceManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("smputils.mace.admin")) {
            sender.sendMessage(configManager.colorize("&cYou don't have permission!"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(configManager.colorize("&6=== MultiMace ==="));
            sender.sendMessage(configManager.colorize("&e/maces list &7- List all mace holders"));
            sender.sendMessage(configManager.colorize("&e/maces reload &7- Reload configuration"));
            return true;
        }

        switch (args[0].toLowerCase()) {
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

    private void handleList(CommandSender sender) {
        sender.sendMessage(configManager.colorize("&6=== Mace Status ==="));
        sender.sendMessage(configManager.colorize("&eCurrent Maces: &f" + maceManager.getMaceCount() + "&e/&f" + maceManager.getMaxMaces()));
        
        var holders = maceManager.getAllMaceHolders();
        if (holders.isEmpty()) {
            sender.sendMessage(configManager.colorize("&eNo maces currently held!"));
        } else {
            sender.sendMessage(configManager.colorize("&6Mace Holders:"));
            holders.forEach((uuid, name) -> 
                sender.sendMessage(configManager.colorize("&e- " + name))
            );
        }
    }

    private void handleReload(CommandSender sender) {
        maceManager.reload();
        sender.sendMessage(configManager.colorize("&a✓ Mace configuration reloaded!"));
    }
}
