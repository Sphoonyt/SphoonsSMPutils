# Sphoon's SMP Utilities - Complete Installation Guide

## Table of Contents
1. [System Requirements](#system-requirements)
2. [Downloading the Project](#downloading-the-project)
3. [Setting Up Your Environment](#setting-up-your-environment)
4. [Building from Source](#building-from-source)
5. [Installing on Your Server](#installing-on-your-server)
6. [Post-Installation Setup](#post-installation-setup)
7. [Verification & Testing](#verification--testing)
8. [Troubleshooting](#troubleshooting)

---

## System Requirements

### Server Requirements
- **Minecraft Server**: Paper 1.21+ (not Spigot, CraftBukkit, or Vanilla)
- **Java**: Java 17 or higher
- **Storage**: ~10MB for plugin + config

### Development Requirements (for building)
- **Java Development Kit (JDK)**: Java 17+
- **Maven**: 3.8.0+
- **Git** (optional, for cloning)

### System Compatibility
- ✅ Windows 10/11
- ✅ macOS (Intel & Apple Silicon)
- ✅ Linux (Ubuntu, CentOS, etc.)

---

## Downloading the Project

### Option 1: Using Git (Recommended)
```bash
# Clone the repository
git clone <repository-url> SphoonsSMPutils
cd SphoonsSMPutils
```

### Option 2: Download ZIP
```bash
# Download the ZIP file
# Extract to a folder
cd SphoonsSMPutils
```

### Option 3: Using the Tar Archive
```bash
# Extract the tar.gz file
tar -xzf SphoonsSMPutils-source.tar.gz
cd smputils-project
```

---

## Setting Up Your Environment

### Windows Setup

#### 1. Install Java 17+ (JDK)
```
1. Download from oracle.com or openjdk.java.net
2. Run the installer
3. Follow the installation wizard
4. Verify installation:
   - Open Command Prompt
   - Type: java -version
   - Should show Java 17+
```

#### 2. Install Maven
```
1. Download from maven.apache.org
2. Extract to C:\Program Files\maven
3. Add to PATH:
   - Right-click "This PC" → Properties
   - Click "Advanced system settings"
   - Click "Environment Variables"
   - Add MAVEN_HOME: C:\Program Files\maven
   - Add to Path: C:\Program Files\maven\bin
4. Verify installation:
   - Open Command Prompt
   - Type: mvn -version
   - Should show Maven version
```

### macOS Setup

#### 1. Install Java 17+ (using Homebrew)
```bash
# Install Homebrew if needed
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java
brew install openjdk@17

# Verify
java -version
```

#### 2. Install Maven (using Homebrew)
```bash
# Install Maven
brew install maven

# Verify
mvn -version
```

### Linux Setup (Ubuntu/Debian)

#### 1. Install Java 17+
```bash
# Update package manager
sudo apt update

# Install OpenJDK 17
sudo apt install openjdk-17-jdk

# Verify
java -version
```

#### 2. Install Maven
```bash
# Install Maven
sudo apt install maven

# Verify
mvn -version
```

---

## Building from Source

### Step 1: Navigate to Project Directory
```bash
cd SphoonsSMPutils
# or
cd smputils-project  # if extracted from tar
```

### Step 2: Build the Project
```bash
# Clean build
mvn clean package

# This will:
# 1. Download dependencies
# 2. Compile Java source code
# 3. Run tests (if any)
# 4. Package into JAR file
```

### Step 3: Verify Build Success
```bash
# Check for success message at end of build:
# BUILD SUCCESS

# JAR file location:
# target/SphoonsSMPutils-1.0.0.jar

# List the file:
ls -lh target/SphoonsSMPutils-1.0.0.jar
```

### Build Troubleshooting

#### "mvn: command not found"
- Maven not installed or not in PATH
- Reinstall Maven and add to system PATH

#### "Java not found"
- Java not installed or not in PATH
- Install JDK 17+

#### Build fails with dependency errors
```bash
# Clear Maven cache and retry
mvn clean package -DskipTests
```

#### Build takes too long
- Normal for first build (downloads dependencies)
- Subsequent builds will be faster
- Check internet connection

---

## Installing on Your Server

### Step 1: Locate the Compiled JAR
```bash
# The JAR will be at:
target/SphoonsSMPutils-1.0.0.jar
```

### Step 2: Copy to Server (Windows)
```
1. Open File Explorer
2. Navigate to target folder
3. Find SphoonsSMPutils-1.0.0.jar
4. Copy the file
5. Navigate to your server folder
6. Paste into plugins/ folder
```

### Step 3: Copy to Server (macOS/Linux)
```bash
# Copy directly
cp target/SphoonsSMPutils-1.0.0.jar /path/to/server/plugins/

# Or copy with specific path
cp target/SphoonsSMPutils-1.0.0.jar ~/minecraft-server/plugins/
```

### Step 4: Set File Permissions (Linux/macOS)
```bash
# Make sure file is readable by server
chmod 644 /path/to/plugins/SphoonsSMPutils-1.0.0.jar
```

### Step 5: Restart Your Server
```bash
# Stop the server
# Navigate to server directory
# Run startup script

# Windows:
./run.bat

# macOS/Linux:
./run.sh
```

---

## Post-Installation Setup

### Step 1: Wait for Plugin Load
Server startup will show:
```
[XX:XX:XX INFO]: ================================================
[XX:XX:XX INFO]: Sphoon's SMP Utilities v1.0.0
[XX:XX:XX INFO]: Loading unified SMP utility plugin...
[XX:XX:XX INFO]: ================================================
[XX:XX:XX INFO]: ✓ Enchantments Limiter initialized
[XX:XX:XX INFO]: ✓ Items Limiter initialized
[XX:XX:XX INFO]: ✓ Potion Limiter initialized
[XX:XX:XX INFO]: ✓ MultiMace initialized
[XX:XX:XX INFO]: ✓ Instant Restock initialized
[XX:XX:XX INFO]: ✓ Commands registered
[XX:XX:XX INFO]: ✓ Event listeners registered
[XX:XX:XX INFO]: ✓ All modules loaded successfully!
[XX:XX:XX INFO]: ================================================
```

### Step 2: Locate Config File
```
Server folder structure:
minecraft-server/
└── plugins/
    └── Sphoon'sSMPutils/
        └── config.yml
```

### Step 3: Customize Configuration
Edit `config.yml` to set your limits:

```yaml
# Example: Enable/disable modules
enchantments:
  enabled: true

items:
  enabled: true
  limits:
    ender_pearl: 16
    totem_of_undying: 1

maces:
  allowed-maces: 3

# Save and reload:
# /enchantlimit reload
```

### Step 4: Set Operator Permissions
Give yourself operator status in server to use commands:
```bash
# In server console or using RCON:
op [YourUsername]
```

---

## Verification & Testing

### Test 1: Verify Plugin Loaded
```
/plugins
# Should show: Sphoon'sSMPutils v1.0.0
```

### Test 2: Test Command Help
```
/enchantlimit
# Should show command help
```

### Test 3: List Current Limits
```
/enchantlimit list
# Should show configured enchantment limits
```

### Test 4: Test Item Limiting
```
# Give yourself many items
/give @s ender_pearl 64

# Try to pick up more (if limit is 16)
# Should prevent pickup
```

### Test 5: Test Mace Management
```
/maces list
# Should show mace status
```

### Test 6: Hot Reload
```
# Edit config.yml
# Reload without restart:
/enchantlimit reload

# Should confirm reload
```

---

## Troubleshooting

### Problem: Plugin doesn't appear in /plugins
**Solution:**
1. Check server console for errors
2. Verify JAR is in plugins/ folder
3. Check file permissions
4. Verify server is Paper 1.21+
5. Check Java version is 17+

### Problem: "Unknown command: /enchantlimit"
**Solution:**
1. Make sure you're an OP
2. Verify plugin loaded (check /plugins)
3. Try restarting server
4. Check permissions in server console

### Problem: Config file not generating
**Solution:**
1. Verify plugin is running (check logs)
2. Check file permissions in plugins folder
3. Ensure config.yml isn't corrupted
4. Delete config.yml and restart (regenerates)

### Problem: Commands don't execute changes
**Solution:**
1. Verify you have permission (are OP)
2. Reload config with /[command] reload
3. Check config.yml syntax (valid YAML)
4. Check server logs for errors

### Problem: Build fails with "Maven not found"
**Solution:**
```bash
# Verify Maven installation
mvn -version

# If not found, reinstall Maven
# Then add to PATH

# Windows: Add C:\Program Files\maven\bin to PATH
# macOS/Linux: brew install maven
```

### Problem: Build fails with "Java version"
**Solution:**
```bash
# Check Java version
java -version

# Should be 17 or higher

# If lower, upgrade:
# Download JDK 17+ from oracle.com
# Install and set JAVA_HOME
```

### Problem: JAR file won't compile
**Solution:**
```bash
# Try clean rebuild
mvn clean

# Then build again
mvn package

# Or skip tests
mvn clean package -DskipTests
```

---

## Next Steps After Installation

1. **Configure your limits** in `config.yml`
2. **Restart the server** to apply changes
3. **Test commands** with `/enchantlimit`, `/itemcap`, etc.
4. **Grant permissions** to players as needed
5. **Monitor server logs** for any issues

## Useful Commands

```bash
# View server logs
tail -f server.log          # macOS/Linux
Get-Content server.log -Tail 50  # Windows PowerShell

# Stop server gracefully
stop              # In server console

# Check plugin load status
/plugins          # In-game
```

## Support

If you encounter issues:
1. Check the README.md
2. Check the PROJECT_STRUCTURE.md
3. Review server logs
4. Verify config.yml syntax
5. Test with fresh config.yml

---

## Summary

✅ Install Java 17+
✅ Install Maven
✅ Clone/Download project
✅ Run `mvn clean package`
✅ Copy JAR to plugins/
✅ Restart server
✅ Edit config.yml
✅ Test with /commands
✅ Enjoy!

---

**Congratulations! Your Sphoon's SMP Utilities plugin is installed and ready to use.**
