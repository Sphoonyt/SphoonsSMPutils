# Sphoon's SMP Utilities - Project Structure

## Complete File Overview

```
SphoonsSMPutils/
├── pom.xml                                    # Maven configuration for building
├── README.md                                  # Full documentation
├── QUICKSTART.md                              # Quick setup guide
├── .gitignore                                 # Git ignore file
│
└── src/main/
    ├── java/me/sphoon/smputils/
    │
    ├── SphoonsSMPutils.java                   # Main plugin class
    │   └── Initializes all managers and registers commands/listeners
    │
    ├── commands/                              # Command handlers (8 files)
    │   ├── EnchantmentCommand.java            # /enchantlimit
    │   ├── ItemCommand.java                   # /itemcap
    │   ├── PotionCommand.java                 # /potionlimit
    │   ├── MaceCommand.java                   # /maces
    │   ├── GetUntrackedMaceCommand.java       # /getuntrackedmace
    │   ├── ClearUntrackedMacesCommand.java   # /clearuntrackedmaces
    │   ├── RestockCommand.java                # /restock
    │   └── ItemEditCommand.java               # /itemedit
    │
    ├── managers/                              # Core logic managers (6 files)
    │   ├── ConfigManager.java                 # Config loading/saving
    │   ├── EnchantmentManager.java            # Enchantment limits
    │   ├── ItemManager.java                   # Item limits & bans
    │   ├── PotionManager.java                 # Potion limits & duration
    │   ├── MaceManager.java                   # Mace tracking & limits
    │   └── RestockManager.java                # Instant restock config
    │
    └── listeners/                             # Event handlers (6 files)
        ├── EnchantmentListener.java           # Enchanting/crafting events
        ├── ItemListener.java                  # Item pickup/drop events
        ├── PotionListener.java                # Brewing/consume events
        ├── MaceListener.java                  # Mace container blocking
        ├── MaceCraftingListener.java          # Mace crafting tracking
        └── RestockListener.java               # Lectern/villager events
    
    └── resources/
        ├── plugin.yml                         # Plugin manifest & commands
        └── config.yml                         # Default configuration
```

## File Statistics

- **Total Files**: 25
- **Java Classes**: 21
  - 1 Main plugin class
  - 8 Command classes
  - 6 Manager classes
  - 6 Event listener classes
- **Configuration Files**: 2
- **Documentation Files**: 2
- **Build Files**: 1

## Code Organization

### Main Plugin Class (1 file)
- `SphoonsSMPutils.java` - Entry point, initializes all components

### Commands (8 files)
Each command class handles user input for one module:
- Enchantment limits
- Item capacity
- Potion restrictions
- Mace management (3 commands)
- Instant restocking
- Item editing

### Managers (6 files)
Business logic layers that handle:
- Configuration management
- Enchantment tracking
- Item limit enforcement
- Potion limit enforcement
- Mace quantity tracking
- Restock settings

### Listeners (6 files)
Event handlers that:
- Prevent enchantments over limits
- Block item pickups that exceed limits
- Prevent banned potion brewing
- Track mace crafting
- Prevent container storage
- Handle restocking

## Configuration Files

### plugin.yml
Defines:
- Plugin metadata (name, version, API version)
- 7+ command definitions
- 9 permission nodes
- Default permission levels

### config.yml
Contains:
- Module enable/disable switches
- Enchantment level limits
- Item capacity limits
- Potion effect limits
- Mace settings
- Restock settings
- Item editor settings
- Customizable messages

## Building the Project

```bash
# Requires: Java 17+, Maven 3.8+

mvn clean package
# Output: target/SphoonsSMPutils-1.0.0.jar
```

## Installation

```bash
# 1. Build the JAR
mvn clean package

# 2. Copy to server
cp target/SphoonsSMPutils-1.0.0.jar /path/to/server/plugins/

# 3. Restart server
# Auto-generates: plugins/Sphoon'sSMPutils/config.yml
```

## Dependencies

**Build Dependencies**:
- Paper API 1.21.1 (provided scope)
- Lombok 1.18.30 (optional, for reducing boilerplate)

**Runtime**:
- Paper Server 1.21+ (no external dependencies)

## Class Responsibilities

### SphoonsSMPutils.java
- Plugin initialization
- Manager creation
- Command registration
- Listener registration
- Provides getter methods

### Command Classes
- Parse user input
- Validate permissions
- Call manager methods
- Send user feedback

### Manager Classes
- Load/save configuration
- Maintain state (limits, counts)
- Provide validation methods
- Handle configuration changes

### Listener Classes
- Monitor game events
- Check limits/rules
- Cancel invalid actions
- Send notifications

## Permissions Hierarchy

```
smputils.* (conceptual, not actual)
├── smputils.enchant.admin
├── smputils.items.admin
├── smputils.items.bypass
├── smputils.potion.admin
├── smputils.mace.admin
├── smputils.mace.untracked
├── smputils.mace.clear
├── smputils.restock.admin
└── smputils.itemedit.use
```

## Key Features

### ✅ Implemented
- Full module integration
- 8 unique commands
- 6 event listeners
- Dynamic configuration
- Hot-reload support
- Permission system
- Item banning
- Mace tracking
- Potion brewing control
- Enchantment limiting
- Instant restocking
- Item customization

### 📝 Future Enhancement Ideas
- GUI for management
- Database storage option
- Per-world configuration
- Statistics/analytics
- Admin notifications
- Customizable alerts
- Rollback functionality

## Performance Considerations

- **Managers**: Cached configuration data
- **Listeners**: Minimal event processing
- **Commands**: Single-threaded execution
- **Config**: File-based persistence

## Testing Checklist

- [ ] Plugin loads on startup
- [ ] Config generates correctly
- [ ] Commands work without arguments (show help)
- [ ] Enchantment limits enforced
- [ ] Item bans enforced
- [ ] Potion limits enforced
- [ ] Maces tracked correctly
- [ ] Restock works instantly
- [ ] Item editor functions
- [ ] Hot-reload works
- [ ] Permissions enforced
- [ ] Messages display correctly

## Support & Maintenance

For help:
1. Check README.md
2. Check QUICKSTART.md
3. Run `/command` for help
4. Check server logs
5. Verify config.yml syntax
