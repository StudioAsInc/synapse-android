# Android Project File Refactoring Agent

## 🎯 Purpose

This repository contains a comprehensive AI agent that automatically refactors Android project files to follow standard Android naming conventions. The agent analyzes your entire Android project and intelligently renames files while updating all references throughout the codebase.

## 🚀 Quick Start

### Prerequisites
- Python 3.6+
- Android project with standard structure
- Write permissions to project directory

### Installation
```bash
# Download the agent script
wget https://raw.githubusercontent.com/your-repo/android-refactor-agent/main/android_refactor_agent.py

# Make it executable
chmod +x android_refactor_agent.py
```

### Usage
```bash
# Preview changes (dry run)
python3 android_refactor_agent.py /path/to/android/project

# Execute the refactoring
python3 android_refactor_agent.py /path/to/android/project --execute
```

## 📋 What It Does

The agent automatically applies these Android naming conventions:

### Layout Files (`res/layout/`)
- **Activities**: `activity_` prefix
- **Fragments**: `fragment_` prefix  
- **List Items**: `item_` prefix
- **Dialogs**: `dialog_` prefix
- **Bottom Sheets**: `bottom_sheet_` prefix

### Java/Kotlin Files (`app/src/main/java/`)
- **Activities**: `Activity` suffix
- **Fragments**: `Fragment` suffix
- **Adapters**: `Adapter` suffix
- **Services**: `Service` suffix
- **Receivers**: `Receiver` suffix
- **Providers**: `Provider` suffix
- **ViewModels**: `ViewModel` suffix
- **Utilities**: `Utils` suffix

### Drawable Files (`res/drawable/`)
- **Icons**: `ic_` prefix
- **Backgrounds**: `bg_` prefix
- **Buttons**: `button_` prefix

### Menu Files (`res/menu/`)
- **All Menus**: `menu_` prefix

## 🔍 Example Analysis Results

When run on a real Android project, the agent found:

```
📊 Found 111 layout files
📊 Found 90 Java files  
📊 Found 14 Kotlin files
📊 Found 93 drawable files
📊 Found 3 menu files

📋 Found 33 files that need renaming:
  • Layout files: 13
  • Java/Kotlin files: 3
  • Drawable files: 14
  • Menu files: 3

📋 Found 28 references to update
```

## 🛡️ Safety Features

- **Dry Run Mode**: Preview all changes before executing
- **Backup Creation**: Creates `.backup` files before renaming
- **Comprehensive Reporting**: Detailed report of all changes
- **Error Handling**: Robust error handling with detailed messages

## 📄 Generated Report

The agent creates a detailed report (`refactoring_report.txt`) showing:

- All files renamed with reasons
- All code references updated
- Statistics and breakdown by file type
- Line-by-line changes made

## 🎯 Real Project Example

Here's what the agent found in a real Android project:

### Files Renamed:
- `synapse_dialog.xml` → `dialog_synapse_dialog.xml`
- `UserProfileMoreBottomSheet.java` → `UserProfileMoreBottomSheetFragment.java`
- `rounded_corner_background.xml` → `bg_rounded_corner_background.xml`
- `drawer_menu.xml` → `menu_drawer_menu.xml`

### References Updated:
- `R.layout.synapse_dialog` → `R.layout.dialog_synapse_dialog`
- `PostMoreBottomSheetDialog` → `PostMoreBottomSheetDialogFragment`
- `tools:context=".MainActivity"` → `tools:context=".MainActivity"`

## 📚 Documentation

- **[Complete Documentation](Android_Refactor_Agent_Documentation.md)**: Detailed usage guide
- **[Usage Example Script](run_refactor_example.sh)**: Interactive example
- **[Refactoring Report](refactoring_report.txt)**: Sample output report

## 🔧 Advanced Usage

### Custom Project Structure
The agent automatically detects standard Android project structures. For custom layouts, you may need to modify the detection patterns in the script.

### Batch Processing
```bash
# Process multiple projects
for project in /path/to/projects/*/; do
    python3 android_refactor_agent.py "$project" --execute
done
```

### Integration with CI/CD
```yaml
# GitHub Actions example
- name: Refactor Android Project
  run: |
    python3 android_refactor_agent.py . --execute
    git add .
    git commit -m "Apply Android naming conventions"
```

## ⚠️ Important Notes

1. **Always commit your code** before running the agent
2. **Run dry run first** to preview changes
3. **Test your project** after refactoring
4. **Review the report** carefully
5. **Clean up backup files** once satisfied

## 🤝 Contributing

This agent can be extended to support additional Android naming conventions or file types. The modular design makes it easy to add new patterns and rules.

## 📄 License

This tool is provided as-is for educational and development purposes. Use at your own risk and always backup your projects before running any automated refactoring tools.

## 🆘 Support

If you encounter issues:
1. Check the generated report for error details
2. Verify your project structure matches Android standards
3. Ensure all dependencies are properly installed
4. Review the documentation for troubleshooting tips

---

**Happy Refactoring! 🎉**