#!/bin/zsh

set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Get the script directory and project root
SCRIPT_DIR="$(cd "$(dirname "${0}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
SOURCE_ICON="$PROJECT_ROOT/app/icon/icon.jpg"
RES_DIR="$PROJECT_ROOT/app/src/main/res"

echo -e "${YELLOW}🎨 Generating app icons from source image...${NC}"

# Check if source icon exists
if [ ! -f "$SOURCE_ICON" ]; then
    echo -e "${RED}❌ Source icon not found: $SOURCE_ICON${NC}"
    exit 1
fi

# Check if ImageMagick is installed
if ! command -v magick &> /dev/null; then
    echo -e "${RED}❌ ImageMagick not found. Please install it: brew install imagemagick${NC}"
    exit 1
fi

echo -e "${YELLOW}📱 Source icon: $SOURCE_ICON${NC}"

# Function to generate adaptive icon foreground
generate_adaptive_foreground() {
    local size=$1
    local density=$2
    local output_dir="$RES_DIR/mipmap-$density"
    local output_file="$output_dir/ic_launcher_foreground.webp"
    
    mkdir -p "$output_dir"
    
    # Calculate the inner size (66.7% of canvas for safe zone)
    local inner_size=$((size * 2 / 3))
    
    # Create transparent canvas and place resized icon in center
    magick "$SOURCE_ICON" \
        -resize "${inner_size}x${inner_size}" \
        -background transparent \
        -gravity center \
        -extent "${size}x${size}" \
        -quality 90 \
        "$output_file"
    
    echo -e "${GREEN}✓ Generated adaptive foreground: mipmap-$density/ic_launcher_foreground.webp (${size}x${size})${NC}"
}

# Function to generate legacy icon
generate_legacy_icon() {
    local size=$1
    local density=$2
    local output_dir="$RES_DIR/mipmap-$density"
    local output_file="$output_dir/ic_launcher.webp"
    
    mkdir -p "$output_dir"
    
    # Generate square icon with rounded corners
    magick "$SOURCE_ICON" \
        -resize "${size}x${size}" \
        -background black \
        -alpha remove \
        \( +clone -alpha extract \
           -draw "fill black polygon 0,0 0,6 6,0 fill white circle 6,6 6,0" \
           \( +clone -flip \) -compose Multiply -composite \
           \( +clone -flop \) -compose Multiply -composite \
        \) -alpha off -compose CopyOpacity -composite \
        -quality 90 \
        "$output_file"
    
    echo -e "${GREEN}✓ Generated legacy icon: mipmap-$density/ic_launcher.webp (${size}x${size})${NC}"
}

# Function to generate round icon
generate_round_icon() {
    local size=$1
    local density=$2
    local output_dir="$RES_DIR/mipmap-$density"
    local output_file="$output_dir/ic_launcher_round.webp"
    
    mkdir -p "$output_dir"
    
    # Generate circular icon
    magick "$SOURCE_ICON" \
        -resize "${size}x${size}" \
        -background black \
        -alpha remove \
        \( +clone -threshold -1 -negate -fill white -draw "circle %[fx:int(w/2)],%[fx:int(h/2)] %[fx:int(w/2)],%[fx:int(h/2-1)]" \) \
        -alpha off -compose CopyOpacity -composite \
        -quality 90 \
        "$output_file"
    
    echo -e "${GREEN}✓ Generated round icon: mipmap-$density/ic_launcher_round.webp (${size}x${size})${NC}"
}

# Generate adaptive icon foregrounds (108dp with 72dp safe area)
echo -e "${YELLOW}📐 Generating adaptive icon foregrounds...${NC}"
generate_adaptive_foreground 108 "mdpi"
generate_adaptive_foreground 162 "hdpi"
generate_adaptive_foreground 216 "xhdpi"
generate_adaptive_foreground 324 "xxhdpi"
generate_adaptive_foreground 432 "xxxhdpi"

# Generate legacy square icons
echo -e "${YELLOW}🔲 Generating legacy square icons...${NC}"
generate_legacy_icon 48 "mdpi"
generate_legacy_icon 72 "hdpi"
generate_legacy_icon 96 "xhdpi"
generate_legacy_icon 144 "xxhdpi"
generate_legacy_icon 192 "xxxhdpi"

# Generate round icons
echo -e "${YELLOW}⭕ Generating round icons...${NC}"
generate_round_icon 48 "mdpi"
generate_round_icon 72 "hdpi"
generate_round_icon 96 "xhdpi"
generate_round_icon 144 "xxhdpi"
generate_round_icon 192 "xxxhdpi"

echo -e "${GREEN}🎉 Icon generation completed successfully!${NC}"
echo -e "${YELLOW}💡 Generated icons:${NC}"
echo -e "   • Adaptive icons with black background (#000000)"
echo -e "   • Legacy square icons with rounded corners"
echo -e "   • Round icons for devices that support them"
echo -e "   • All density buckets: mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi"
echo -e "   • WebP format for optimal compression and modern Android compatibility"