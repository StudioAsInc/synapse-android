#!/bin/bash

# Script to clean up unused drawable files
# This script will delete drawable files that are not referenced in the code

echo "Starting cleanup of unused drawable files..."
echo "Total drawables: $(wc -l < /tmp/all_drawables.txt)"
echo "Used drawables: $(wc -l < /tmp/all_used_drawables.txt)"
echo "Unused drawables: $(wc -l < /tmp/unused_drawables.txt)"
echo ""

# Create backup directory
BACKUP_DIR="unused_drawables_backup_$(date +%Y%m%d_%H%M%S)"
mkdir -p "$BACKUP_DIR"

echo "Creating backup in: $BACKUP_DIR"
echo ""

# Move unused files to backup directory instead of deleting them
while IFS= read -r drawable_name; do
    # Find the actual file (could be .xml or .png)
    if [ -f "app/src/main/res/drawable/${drawable_name}.xml" ]; then
        echo "Moving ${drawable_name}.xml to backup..."
        mv "app/src/main/res/drawable/${drawable_name}.xml" "$BACKUP_DIR/"
    elif [ -f "app/src/main/res/drawable/${drawable_name}.png" ]; then
        echo "Moving ${drawable_name}.png to backup..."
        mv "app/src/main/res/drawable/${drawable_name}.png" "$BACKUP_DIR/"
    else
        echo "Warning: Could not find file for ${drawable_name}"
    fi
done < /tmp/unused_drawables.txt

echo ""
echo "Cleanup completed!"
echo "Unused drawables moved to: $BACKUP_DIR"
echo "You can review them and delete the backup directory if everything looks good."
echo ""
echo "To restore files if needed:"
echo "mv $BACKUP_DIR/* app/src/main/res/drawable/"
echo ""
echo "To permanently delete backup:"
echo "rm -rf $BACKUP_DIR"