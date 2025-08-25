#!/usr/bin/env python3
"""
Script to help update Android layout files for dark mode support.
This script replaces hardcoded colors with theme-aware alternatives.
"""

import os
import re
import glob
from pathlib import Path

# Color mapping for common hardcoded colors
COLOR_MAPPINGS = {
    # Background colors
    '#FFFFFF': '?attr/colorSurface',
    '#F5F5F5': '?attr/colorSurfaceVariant',
    
    # Text colors
    '#000000': '?attr/colorOnSurface',
    '#FFFFFF': '?attr/colorOnSurface',  # For text, use onSurface
    
    # Card backgrounds
    'app:cardBackgroundColor="#FFFFFF"': 'app:cardBackgroundColor="?attr/colorSurface"',
    
    # Specific color replacements
    'android:background="#FFFFFF"': 'android:background="?attr/colorSurface"',
    'android:background="#F5F5F5"': 'android:background="?attr/colorSurfaceVariant"',
    'android:textColor="#FFFFFF"': 'android:textColor="?attr/colorOnSurface"',
    'android:textColor="#000000"': 'android:textColor="?attr/colorOnSurface"',
}

def update_layout_file(file_path):
    """Update a single layout file with theme-aware colors."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # Apply color mappings
        for old_color, new_color in COLOR_MAPPINGS.items():
            content = content.replace(old_color, new_color)
        
        # If content changed, write it back
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Updated: {file_path}")
            return True
        else:
            print(f"No changes needed: {file_path}")
            return False
            
    except Exception as e:
        print(f"Error updating {file_path}: {e}")
        return False

def update_drawable_file(file_path):
    """Update a single drawable file with theme-aware colors."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # Replace hardcoded colors in drawable files
        content = re.sub(r'<solid android:color="#FFFFFF"/>', '<solid android:color="?attr/colorSurface"/>', content)
        content = re.sub(r'<solid android:color="#F5F5F5"/>', '<solid android:color="?attr/colorSurfaceVariant"/>', content)
        content = re.sub(r'<solid android:color="#000000"/>', '<solid android:color="?attr/colorOnSurface"/>', content)
        
        # If content changed, write it back
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Updated drawable: {file_path}")
            return True
        else:
            print(f"No changes needed in drawable: {file_path}")
            return False
            
    except Exception as e:
        print(f"Error updating drawable {file_path}: {e}")
        return False

def main():
    """Main function to update all layout and drawable files."""
    app_dir = Path("app/src/main/res")
    
    if not app_dir.exists():
        print("Error: app/src/main/res directory not found!")
        print("Please run this script from the project root directory.")
        return
    
    # Update layout files
    layout_files = glob.glob(str(app_dir / "layout" / "*.xml"))
    print(f"\nFound {len(layout_files)} layout files to process...")
    
    updated_layouts = 0
    for layout_file in layout_files:
        if update_layout_file(layout_file):
            updated_layouts += 1
    
    # Update drawable files
    drawable_files = glob.glob(str(app_dir / "drawable" / "*.xml"))
    print(f"\nFound {len(drawable_files)} drawable files to process...")
    
    updated_drawables = 0
    for drawable_file in drawable_files:
        if update_drawable_file(drawable_file):
            updated_drawables += 1
    
    print(f"\n=== Summary ===")
    print(f"Layout files updated: {updated_layouts}")
    print(f"Drawable files updated: {updated_drawables}")
    print(f"\nNext steps:")
    print(f"1. Build the project to check for any errors")
    print(f"2. Test the app in both light and dark themes")
    print(f"3. Manually review the changes and adjust as needed")
    print(f"4. Check the DARK_MODE_IMPLEMENTATION.md file for detailed guidance")

if __name__ == "__main__":
    main()