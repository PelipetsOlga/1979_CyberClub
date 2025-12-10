#!/bin/bash

set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Get the script directory and project root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
BUILD_DIR="$PROJECT_ROOT/build"

# Function to extract project info from config.yaml
get_project_info() {
    local app_name=""
    local version="1.0.0"
    local config_file="$PROJECT_ROOT/../back/config.yaml"
    
    # Get app info from config.yaml
    if [ -f "$config_file" ]; then
        app_name=$(grep "^  name:" "$config_file" | sed "s/  name: *//" | tr -d "'\"")
        # Use app_id as fallback if name is not found or is just a number
        if [ -z "$app_name" ] || [[ "$app_name" =~ ^[0-9]+$ ]]; then
            app_name=$(grep "^  app_id:" "$config_file" | sed "s/  app_id: *//" | tr -d "'\"")
        fi
    fi
    
    # Fallback values
    app_name=${app_name:-"android_app"}
    
    # Clean up the app name for display (remove underscores, capitalize)
    display_name=$(echo "$app_name" | sed 's/_/ /g' | sed 's/\b\(.\)/\u\1/g')
    
    echo "$app_name|$version|$display_name"
}

# Extract project information
PROJECT_INFO=$(get_project_info)
IFS='|' read -r APP_NAME VERSION DISPLAY_NAME <<< "$PROJECT_INFO"

echo -e "${BLUE}🚀 Universal Flutter Build Script${NC}"
echo -e "${BLUE}==================================${NC}"
echo -e "${YELLOW}Project: $DISPLAY_NAME${NC}"
echo -e "${YELLOW}Version: $VERSION${NC}"
echo -e "${YELLOW}Project root: $PROJECT_ROOT${NC}"
echo -e "${YELLOW}Build directory: $BUILD_DIR${NC}"

# Change to project directory
cd "$PROJECT_ROOT"

# Create build directory if it doesn't exist
mkdir -p "$BUILD_DIR"

# Clean previous builds
echo -e "${YELLOW}🧹 Cleaning previous builds...${NC}"
./gradlew clean

# Build APK
echo -e "${YELLOW}🔨 Building APK...${NC}"
./gradlew assembleRelease

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ APK build successful${NC}"
else
    echo -e "${RED}❌ APK build failed${NC}"
    exit 1
fi

# Build AAB
echo -e "${YELLOW}🔨 Building AAB...${NC}"
./gradlew bundleRelease

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ AAB build successful${NC}"
else
    echo -e "${RED}❌ AAB build failed${NC}"
    exit 1
fi

# Copy build artifacts to build directory
echo -e "${YELLOW}📦 Copying build artifacts...${NC}"

# Copy APK files
APK_SOURCE="$PROJECT_ROOT/app/build/outputs/apk/release"
if [ -d "$APK_SOURCE" ]; then
    for apk in "$APK_SOURCE"/*.apk; do
        if [ -f "$apk" ]; then
            cp "$apk" "$BUILD_DIR/"
            echo -e "${GREEN}Copied: $(basename "$apk")${NC}"
        fi
    done
fi

# Copy AAB files
AAB_SOURCE="$PROJECT_ROOT/app/build/outputs/bundle/release"
if [ -d "$AAB_SOURCE" ]; then
    for aab in "$AAB_SOURCE"/*.aab; do
        if [ -f "$aab" ]; then
            cp "$aab" "$BUILD_DIR/"
            echo -e "${GREEN}Copied: $(basename "$aab")${NC}"
        fi
    done
fi

echo ""
echo -e "${GREEN}🎉 Build completed successfully!${NC}"
echo -e "${GREEN}📁 Output files in $BUILD_DIR/:${NC}"

# List all APK and AAB files in build directory
if [ -d "$BUILD_DIR" ]; then
    APK_FILES=$(find "$BUILD_DIR" -name "*.apk" 2>/dev/null)
    AAB_FILES=$(find "$BUILD_DIR" -name "*.aab" 2>/dev/null)
    
    if [ -n "$APK_FILES" ]; then
        echo -e "${GREEN}📦 APK files:${NC}"
        for apk in $APK_FILES; do
            SIZE=$(du -h "$apk" | cut -f1)
            echo -e "   $(basename "$apk") (${SIZE})"
        done
    fi
    
    if [ -n "$AAB_FILES" ]; then
        echo -e "${GREEN}📦 AAB files:${NC}"
        for aab in $AAB_FILES; do
            SIZE=$(du -h "$aab" | cut -f1)
            echo -e "   $(basename "$aab") (${SIZE})"
        done
    fi
fi

echo ""
echo -e "${YELLOW}💡 Usage:${NC}"
echo -e "   APK: For direct installation or testing"
echo -e "   AAB: For Google Play Store upload"