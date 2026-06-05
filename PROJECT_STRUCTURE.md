name: Build Plugin

on:
  push:
    branches: [ main, master, develop ]
  pull_request:
    branches: [ main, master, develop ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    strategy:
      matrix:
        java-version: ['21']
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven
    
    - name: Build with Maven
      run: mvn clean package -DskipTests
    
    - name: Upload artifact
      uses: actions/upload-artifact@v4
      with:
        name: SphoonsSMPutils-build-${{ github.run_number }}
        path: target/SphoonsSMPutils-*.jar
        retention-days: 90
    
    - name: Create Release (on tag)
      if: startsWith(github.ref, 'refs/tags/v')
      uses: softprops/action-gh-release@v2
      with:
        files: target/SphoonsSMPutils-*.jar
        body: |
          # Sphoon's SMP Utilities Release
          
          ## Features
          - Enchantments Limiter (with item-specific limits!)
          - Items Limiter
          - Potion Limiter
          - MultiMace
          - Instant Restock
          - Item Editor
          
          ## Installation
          1. Download the JAR file
          2. Place in your server's `plugins/` folder
          3. Restart server
          4. Configuration auto-generates
          
          ## Requirements
          - Paper 1.21+
          - Java 17+
          
          ## Commands
          - /enchantlimit - Control enchantments (with item-specific limits!)
          - /itemcap - Manage item limits
          - /potionlimit - Restrict potions
          - /maces - Track maces
          - /restock - Manage restocking
          - /itemedit - Edit items
          
          ## Documentation
          See README.md for complete documentation
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
