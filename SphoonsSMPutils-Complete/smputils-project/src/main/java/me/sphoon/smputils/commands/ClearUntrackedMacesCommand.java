package me.sphoon.smputils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.MaceManager;

public class ClearUntrackedMacesCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final MaceManager maceManager;
    private final ConfigManager configManager;

    public ClearUntrackedMacesCommand(JavaPlugin plugin, MaceManager maceManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.maceManager = maceManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("smputils.mace.clear")) {
            sender.sendMessage(configManager.colorize("&cYou don't have permission!"));
            return true;
        }

        maceManager.clearUntrackedMaces();
        sender.sendMessage(configManager.colorize("&a✓ All untracked maces have been cleared!"));
        return true;
    }
}
