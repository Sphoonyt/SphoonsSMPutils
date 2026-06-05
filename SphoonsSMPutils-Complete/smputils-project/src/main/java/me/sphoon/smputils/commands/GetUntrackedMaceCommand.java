package me.sphoon.smputils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import me.sphoon.smputils.managers.ConfigManager;
import me.sphoon.smputils.managers.MaceManager;

public class GetUntrackedMaceCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final MaceManager maceManager;
    private final ConfigManager configManager;

    public GetUntrackedMaceCommand(JavaPlugin plugin, MaceManager maceManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.maceManager = maceManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("smputils.mace.untracked")) {
            sender.sendMessage(configManager.colorize("&cYou don't have permission!"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.colorize("&cOnly players can use this command!"));
            return true;
        }

        Player player = (Player) sender;
        ItemStack mace = new ItemStack(org.bukkit.Material.MACE);
        ItemMeta meta = mace.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(configManager.colorize("&6Untracked Mace"));
            mace.setItemMeta(meta);
        }

        player.getInventory().addItem(mace);
        player.sendMessage(configManager.colorize("&a✓ You received an untracked mace!"));
        return true;
    }
}
