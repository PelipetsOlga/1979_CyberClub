#!/bin/bash

# Script to convert icon images to WebP format for all mipmap densities
# Usage: ./convert_icons.sh <source_icon1> <source_icon2>

if [ $# -ne 2 ]; then
    echo "Usage: $0 <source_icon1.png> <source_icon2.png>"
    echo "Example: $0 /path/to/navi_g2.png /path/to/fnatic_liquid.png"
    exit 1
fi

ICON1_SRC="$1"
ICON2_SRC="$2"
BASE_PATH="app/src/main/res"

# Check if source files exist
if [ ! -f "$ICON1_SRC" ]; then
    echo "Error: $ICON1_SRC not found"
    exit 1
fi

if [ ! -f "$ICON2_SRC" ]; then
    echo "Error: $ICON2_SRC not found"
    exit 1
fi

# Density configurations: name, width, height
declare -A DENSITIES=(
    ["mipmap-mdpi"]="48:24"
    ["mipmap-hdpi"]="72:36"
    ["mipmap-xhdpi"]="96:48"
    ["mipmap-xxhdpi"]="144:72"
    ["mipmap-xxxhdpi"]="192:96"
)

# Convert function
convert_icon() {
    local src="$1"
    local dest="$2"
    local width="$3"
    local height="$4"
    
    if command -v magick &> /dev/null; then
        magick "$src" -resize "${width}x${height}" -quality 90 "$dest"
    elif command -v convert &> /dev/null; then
        convert "$src" -resize "${width}x${height}" -quality 90 "$dest"
    elif command -v cwebp &> /dev/null; then
        # First convert to PNG with correct size, then to WebP
        if command -v sips &> /dev/null; then
            sips -z "$height" "$width" "$src" --out /tmp/temp_icon.png
            cwebp -q 90 /tmp/temp_icon.png -o "$dest"
        else
            echo "Error: No image conversion tool found. Please install ImageMagick or libwebp"
            exit 1
        fi
    else
        echo "Error: No image conversion tool found. Please install ImageMagick or libwebp"
        exit 1
    fi
}

# Create directories and convert
for density in "${!DENSITIES[@]}"; do
    IFS=':' read -r width height <<< "${DENSITIES[$density]}"
    mkdir -p "$BASE_PATH/$density"
    
    echo "Converting to $density (${width}x${height})..."
    convert_icon "$ICON1_SRC" "$BASE_PATH/$density/ic_icon1.webp" "$width" "$height"
    convert_icon "$ICON2_SRC" "$BASE_PATH/$density/ic_icon2.webp" "$width" "$height"
done

echo "Conversion complete!"
echo "Files created in:"
for density in "${!DENSITIES[@]}"; do
    echo "  - $BASE_PATH/$density/ic_icon1.webp"
    echo "  - $BASE_PATH/$density/ic_icon2.webp"
done

