# Sphoon's SMP Utilities

A unified, all-in-one Paper plugin combining 6 essential SMP utility plugins into one seamless experience.

## Features

### 📜 Enchantments Limiter
- Control maximum enchantment levels per item
- Prevent overpowered enchantments
- Full command-based management
- Hot-reload configuration

### 📦 Items Limiter
- Set stack limits on specific items
- Ban items completely (set limit to 0)
- Monitor item quantities in player inventories
- Bypass permissions for admins

### 🧪 Potion Limiter
- Control potion effect levels during brewing
- Limit potion duration
- Prevent banned potions
- Effect level restrictions

### ⚒️ MultiMace
- Limit the number of maces on your server
- Track mace crafting
- Prevent container storage
- Optional enchanting restrictions
- Destroy maces when dropped

### 🔄 Instant Restock
- Immediate villager restocking
- Lectern restocking support
- Enable/disable per-block type
- Zero delay trading

### ✏️ Item Editor
- Edit item names, lore, and enchantments
- In-game GUI support
- Command-based editing
- Full item customization

## Installation

1. **Requirements**
   - Paper Server 1.21+
   - Java 17+
   - Maven (for building from source)

2. **Building from Source**
   ```bash
   cd SphoonsSMPutils
   mvn clean package
   ```
   The compiled JAR will be in `target/SphoonsSMPutils-1.0.0.jar`

3. **Installation**
   - Place the JAR in your server's `plugins` folder
   - Restart your server
   - Configuration file will auto-generate in `plugins/Sphoon'sSMPutils/config.yml`

4. **File Structure**
   ```
   plugins/
   └── Sphoon'sSMPutils/
       └── config.yml
   ```

## Commands

### Enchantment Commands
```
/enchantlimit set <enchantment> <level> - Set max enchantment level
/enchantlimit remove <enchantment>      - Remove enchantment limit
/enchantlimit list                      - List all limits
/enchantlimit reload                    - Reload config
```
**Permission**: `smputils.enchant.admin`

### Item Commands
```
/itemcap set <material> <limit>   - Set item limit (0 = ban)
/itemcap remove <material>         - Remove item limit
/itemcap list                      - List all limits
/itemcap reload                    - Reload config
```
**Permission**: `smputils.items.admin`
**Bypass**: `smputils.items.bypass`

### Potion Commands
```
/potionlimit set <potion> <level>  - Set potion level limit
/potionlimit remove <potion>        - Remove potion limit
/potionlimit list                   - List all limits
/potionlimit reload                 - Reload config
```
**Permission**: `smputils.potion.admin`

### Mace Commands
```
/maces list                    - List mace holders
/maces reload                  - Reload config
/getuntrackedmace             - Get a mace (admin only)
/clearuntrackedmaces          - Clear all untracked maces
```
**Permissions**:
- `smputils.mace.admin` - Manage maces
- `smputils.mace.untracked` - Get untracked maces
- `smputils.mace.clear` - Clear untracked maces

### Restock Commands
```
/restock enable   - Enable instant restocking
/restock disable  - Disable instant restocking
/restock status   - Check restocking status
/restock reload   - Reload config
```
**Permission**: `smputils.restock.admin`

### Item Edit Commands
```
/itemedit name <name>                  - Set item display name
/itemedit lore add <text>              - Add lore line
/itemedit lore remove <line>           - Remove lore line
/itemedit lore clear                   - Clear all lore
/itemedit enchant <enchantment> <level> - Add enchantment
/itemedit enchant remove <enchantment> - Remove enchantment
```
**Permission**: `smputils.itemedit.use`

## Configuration

### Default config.yml Structure

```yaml
# Module toggles
enchantments:
  enabled: true
  limits:
    protection: 4
    sharpness: 5
    # ... more enchantments

items:
  enabled: true
  limits:
    ender_pearl: 16
    totem_of_undying: 1
    # ... more items

potions:
  enabled: true
  limits:
    strength: 2
    speed: 2
    # ... more potions

maces:
  enabled: true
  allowed-maces: 3
  allow-mace-enchanting: true
  prevent-container-storage: true

restock:
  enabled: true
  villagers:
    enabled: true
    instant: true
  lecterns:
    enabled: true
    instant: true

itemedit:
  enabled: true
  gui-enabled: true
```

### Editing Configuration

You can:
1. **Edit config.yml directly** and use `/[command] reload`
2. **Use commands** to change settings on-the-fly
3. **Changes persist** automatically to the config file

## Examples

### Enchantment Limits
```
/enchantlimit set protection 3      # Max Protection III
/enchantlimit set sharpness 4       # Max Sharpness IV
/enchantlimit list                  # View all limits
```

### Item Bans
```
/itemcap set dragon_egg 0           # Ban dragon eggs
/itemcap set totem_of_undying 1     # Limit totems to 1
/itemcap set diamond 64             # Limit diamonds to 64
```

### Potion Limits
```
/potionlimit set strength 1         # Max Strength I
/potionlimit set regeneration 1     # Max Regen I
```

### Mace Management
```
/maces list                         # See all mace holders
/maces reload                       # Reload mace config
/getuntrackedmace                  # Give yourself a mace
```

## Permissions Summary

| Permission | Default | Description |
|-----------|---------|-------------|
| `smputils.enchant.admin` | OP | Manage enchantment limits |
| `smputils.items.admin` | OP | Manage item limits |
| `smputils.items.bypass` | false | Bypass item limits |
| `smputils.potion.admin` | OP | Manage potion limits |
| `smputils.mace.admin` | OP | Manage maces |
| `smputils.mace.untracked` | OP | Get untracked maces |
| `smputils.mace.clear` | OP | Clear untracked maces |
| `smputils.restock.admin` | OP | Manage restocking |
| `smputils.itemedit.use` | OP | Use item editor |

## Troubleshooting

### Plugin doesn't load
- Ensure server is Paper 1.21+
- Check that Java 17+ is installed
- Look for errors in server logs

### Config not updating
- Use `/[command] reload` to reload the config
- Check file permissions in the plugin folder
- Ensure config.yml is valid YAML

### Commands not working
- Verify you have the correct permission
- Check that the module is enabled in config.yml
- Use `/[command]` without arguments to see usage

## Support & Contributions

For issues, questions, or feature requests, please contact Sphoon.

## License

This plugin combines features from:
- EnchantmentsLimiter by Lusik21556
- ItemEdit by emanondev
- ItemsLimiter by Lusik21556
- InstaRestock by Gui
- MultiMace by Pyro (BonkMC)
- PotionLimiter by Various Contributors

## Changelog

### v1.0.0 (Initial Release)
- ✓ Unified all 6 utility plugins
- ✓ Full command-based management
- ✓ Hot-reload configuration
- ✓ Comprehensive permission system
- ✓ Item editor with full customization
- ✓ Mace tracking and management
- ✓ Potion and enchantment limiting
