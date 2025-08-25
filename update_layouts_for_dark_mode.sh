#!/bin/bash

# Script to help update Android layout files for dark mode support
# This script replaces hardcoded colors with theme-aware alternatives

echo "=== Dark Mode Layout Update Script ==="
echo "This script will update layout and drawable files for dark mode support."
echo ""

# Check if we're in the right directory
if [ ! -d "app/src/main/res" ]; then
    echo "Error: app/src/main/res directory not found!"
    echo "Please run this script from the project root directory."
    exit 1
fi

# Function to update a file
update_file() {
    local file_path="$1"
    local temp_file="${file_path}.tmp"
    
    # Create backup
    cp "$file_path" "${file_path}.backup"
    
    # Replace colors
    sed -i.tmp \
        -e 's/#FFFFFF/\\?attr\/colorSurface/g' \
        -e 's/#F5F5F5/\\?attr\/colorSurfaceVariant/g' \
        -e 's/#000000/\\?attr\/colorOnSurface/g' \
        -e 's/app:cardBackgroundColor="#FFFFFF"/app:cardBackgroundColor="\\?attr\/colorSurface"/g' \
        "$file_path"
    
    # Check if file was modified
    if ! cmp -s "$file_path" "${file_path}.backup"; then
        echo "Updated: $file_path"
        rm "${file_path}.backup"
        return 0
    else
        echo "No changes needed: $file_path"
        rm "${file_path}.backup"
        return 1
    fi
}

# Update layout files
echo "Processing layout files..."
layout_files=$(find app/src/main/res/layout -name "*.xml" -type f)
layout_count=0
updated_layouts=0

for file in $layout_files; do
    layout_count=$((layout_count + 1))
    if update_file "$file"; then
        updated_layouts=$((updated_layouts + 1))
    fi
done

# Update drawable files
echo ""
echo "Processing drawable files..."
drawable_files=$(find app/src/main/res/drawable -name "*.xml" -type f 2>/dev/null)
drawable_count=0
updated_drawables=0

for file in $drawable_files; do
    drawable_count=$((drawable_count + 1))
    if update_file "$file"; then
        updated_drawables=$((updated_drawables + 1))
    fi
done

echo ""
echo "=== Summary ==="
echo "Layout files processed: $layout_count"
echo "Layout files updated: $updated_layouts"
echo "Drawable files processed: $drawable_count"
echo "Drawable files updated: $updated_drawables"
echo ""
echo "Next steps:"
echo "1. Build the project to check for any errors"
echo "2. Test the app in both light and dark themes"
echo "3. Manually review the changes and adjust as needed"
echo "4. Check the DARK_MODE_IMPLEMENTATION.md file for detailed guidance"
echo ""
echo "Note: Backup files have been created with .backup extension"
echo "You can restore them if needed by removing the .tmp extension"