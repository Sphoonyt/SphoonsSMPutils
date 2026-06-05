# Quick Start Guide - Sphoon's SMP Utilities

## Step 1: Prepare Your Environment

```bash
# Install Java 17+ if you don't have it
# Install Maven if you don't have it

# Clone or download the project
cd SphoonsSMPutils
```

## Step 2: Build the Plugin

```bash
# Compile and package the plugin
mvn clean package

# The JAR will be created at:
# target/SphoonsSMPutils-1.0.0.jar
```

## Step 3: Install on Your Server

```bash
# Copy the JAR to your server
cp target/SphoonsSMPutils-1.0.0.jar /path/to/server/plugins/

# Restart your server
# Server will auto-generate config.yml in plugins/Sphoon'sSMPutils/
```

## Step 4: Verify Installation

1. Check server console for:
   ```
   ================================================
   Sphoon's SMP Utilities v1.0.0
   Loading unified SMP utility plugin...
   ================================================
   ✓ Enchantments Limiter initialized
   ✓ Items Limiter initialized
   ✓ Potion Limiter initialized
   ✓ MultiMace initialized
   ✓ Instant Restock initialized
   ✓ Commands registered
   ✓ Event listeners registered
   All modules loaded successfully!
   ================================================
   ```

2. In-game, run: `/enchantlimit` to test
   - Should show help menu if working

## Step 5: Configure Your Settings

Edit `plugins/Sphoon'sSMPutils/config.yml`:

```yaml
# Enable/disable modules
enchantments:
  enabled: true

# Set enchantment limits
items:
  limits:
    ender_pearl: 16
    totem_of_undying: 1

# Configure maces
maces:
  allowed-maces: 3

# And more...
```

Then reload: `/enchantlimit reload`

## Common Configuration Tasks

### Limit Enchantments
```yaml
enchantments:
  enabled: true
  limits:
    protection: 4
    sharpness: 5
    unbreaking: 3
    looting: 3
```

### Ban/Limit Items
```yaml
items:
  enabled: true
  limits:
    dragon_egg: 0              # Ban completely
    totem_of_undying: 1        # Allow only 1
    ender_pearl: 16            # Limit to 16
```

### Control Maces
```yaml
maces:
  enabled: true
  allowed-maces: 3            # Max 3 maces per server
  allow-mace-enchanting: true # Can enchant maces?
  prevent-container-storage: true # Can't store in chests
```

### Limit Potions
```yaml
potions:
  enabled: true
  limits:
    strength: 1               # Max Strength I
    speed: 2                  # Max Speed II
    regeneration: 1           # Max Regen I
```

## In-Game Commands

Once installed, use these commands:

### View Help
```
/enchantlimit                # Show enchantment help
/itemcap                     # Show items help
/potionlimit                 # Show potion help
/maces                       # Show mace help
/restock                     # Show restock help
/itemedit                    # Show item edit help
```

### Example Usage
```
# Set Protection limit to 3
/enchantlimit set protection 3

# Ban dragon eggs
/itemcap set dragon_egg 0

# Limit totems to 1
/itemcap set totem_of_undying 1

# Limit strength potion to Level I
/potionlimit set strength 1

# Check mace status
/maces list

# Enable instant restocking
/restock enable

# Edit your held item
/itemedit name &cRare Sword
/itemedit lore add &bThis is a legendary weapon
```

## Troubleshooting

### Plugin not loading?
1. Check server version: must be Paper 1.21+
2. Check Java version: must be Java 17+
3. Check server logs for errors
4. Ensure JAR is in `plugins/` folder

### Config not working?
1. Restart server after editing config.yml
2. Or use `/[command] reload` to hot-reload
3. Check that YAML syntax is correct

### Commands not working?
1. Verify you're OP or have the permission
2. Check that module is enabled in config
3. Run `/command` without arguments to see help

### Items/Enchantments not being limited?
1. Check config.yml has entries for the item/enchantment
2. Use `/itemcap list` or `/enchantlimit list` to verify
3. Reload config: `/itemcap reload`

## File Structure

```
SphoonsSMPutils/
├── pom.xml                           # Maven build config
├── README.md                         # Full documentation
├── QUICKSTART.md                     # This file
├── .gitignore                        # Git ignore file
└── src/
    └── main/
        ├── java/me/sphoon/smputils/
        │   ├── SphoonsSMPutils.java  # Main plugin class
        │   ├── commands/             # Command classes
        │   │   ├── EnchantmentCommand.java
        │   │   ├── ItemCommand.java
        │   │   ├── PotionCommand.java
        │   │   ├── MaceCommand.java
        │   │   ├── GetUntrackedMaceCommand.java
        │   │   ├── ClearUntrackedMacesCommand.java
        │   │   ├── RestockCommand.java
        │   │   └── ItemEditCommand.java
        │   ├── managers/             # Manager classes
        │   │   ├── ConfigManager.java
        │   │   ├── EnchantmentManager.java
        │   │   ├── ItemManager.java
        │   │   ├── PotionManager.java
        │   │   ├── MaceManager.java
        │   │   └── RestockManager.java
        │   └── listeners/            # Event listeners
        │       ├── EnchantmentListener.java
        │       ├── ItemListener.java
        │       ├── PotionListener.java
        │       ├── MaceListener.java
        │       ├── MaceCraftingListener.java
        │       └── RestockListener.java
        └── resources/
            ├── plugin.yml            # Plugin manifest
            └── config.yml            # Default config

target/
└── SphoonsSMPutils-1.0.0.jar        # Compiled plugin
```

## Next Steps

1. **Build the plugin**: `mvn clean package`
2. **Install it**: Copy JAR to plugins folder
3. **Restart server**: Let config generate
4. **Customize config.yml**: Set your limits
5. **Test with commands**: Try `/enchantlimit list`
6. **Configure in-game**: Use commands to manage

## Getting Help

For issues:
1. Check the README.md for full documentation
2. Run commands without arguments to see help
3. Check server logs for error messages
4. Verify config.yml is valid YAML

Enjoy your unified SMP utilities!
