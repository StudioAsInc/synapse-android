# Android Project File Refactoring Agent

## Overview

The Android Project File Refactoring Agent is a comprehensive Python script that analyzes Android projects and automatically refactors filenames to follow standard Android naming conventions. It handles layout files, Java/Kotlin classes, drawable files, and menu files while updating all references throughout the project.

## Features

- **Comprehensive Analysis**: Recursively scans the entire Android project structure
- **Smart Naming Detection**: Automatically determines the appropriate naming convention based on file content and context
- **Reference Updates**: Finds and updates all references to renamed files across the project
- **Dry Run Mode**: Preview changes before executing them
- **Detailed Reporting**: Generates comprehensive reports of all changes made
- **Backup Creation**: Creates backups of files before renaming
- **Error Handling**: Robust error handling with detailed error messages

## Android Naming Conventions Applied

### Layout Files (`res/layout/`)
- **Activities**: `activity_` prefix (e.g., `main.xml` → `activity_main.xml`)
- **Fragments**: `fragment_` prefix (e.g., `home.xml` → `fragment_home.xml`)
- **List/Adapter Items**: `item_` prefix (e.g., `user_row.xml` → `item_user.xml`)
- **Dialogs**: `dialog_` prefix (e.g., `error.xml` → `dialog_error.xml`)
- **Bottom Sheets**: `bottom_sheet_` prefix (e.g., `settings.xml` → `bottom_sheet_settings.xml`)
- **Include Layouts**: `include_` or `merge_` prefix

### Java/Kotlin Files (`app/src/main/java/`)
- **Activities**: `Activity` suffix (e.g., `Home.java` → `HomeActivity.java`)
- **Fragments**: `Fragment` suffix (e.g., `UserProfile.java` → `UserProfileFragment.java`)
- **Adapters**: `Adapter` suffix (e.g., `UserList.java` → `UserListAdapter.java`)
- **Services**: `Service` suffix (e.g., `Upload.java` → `UploadService.java`)
- **Broadcast Receivers**: `Receiver` suffix (e.g., `Download.java` → `DownloadReceiver.java`)
- **Content Providers**: `Provider` suffix (e.g., `Data.java` → `DataProvider.java`)
- **ViewModels**: `ViewModel` suffix (e.g., `User.java` → `UserViewModel.java`)
- **Utilities**: `Utils` suffix (e.g., `Helper.java` → `HelperUtils.java`)

### Drawable Files (`res/drawable/`)
- **Icons**: `ic_` prefix (e.g., `home.xml` → `ic_home.xml`)
- **Backgrounds**: `bg_` prefix (e.g., `rounded.xml` → `bg_rounded.xml`)
- **Buttons**: `button_` prefix for button-related drawables

### Menu Files (`res/menu/`)
- **All Menus**: `menu_` prefix (e.g., `main.xml` → `menu_main.xml`)

## Usage

### Basic Usage

```bash
# Dry run (preview changes without modifying files)
python3 android_refactor_agent.py /path/to/android/project

# Execute the refactoring
python3 android_refactor_agent.py /path/to/android/project --execute
```

### Command Line Options

- `<project_path>`: Path to the Android project root directory
- `--execute`: Actually perform the renames (default is dry run mode)

### Example Output

```
🚀 Starting Android Project Refactoring Agent
============================================================
🔍 Analyzing Android project structure...
📊 Found 111 layout files
📊 Found 90 Java files
📊 Found 14 Kotlin files
📊 Found 93 drawable files
📊 Found 3 menu files

🔍 Identifying files that need renaming...
📋 Found 33 files that need renaming:
  • Layout files: 13
  • Java/Kotlin files: 3
  • Drawable files: 14
  • Menu files: 3

🔍 Finding references to update...
📋 Found 28 references to update
```

## How It Works

### 1. Project Analysis
The agent scans the entire Android project structure to identify:
- Layout files in `res/layout/`
- Java/Kotlin files in `app/src/main/java/`
- Drawable files in `res/drawable/`
- Menu files in `res/menu/`

### 2. Smart Naming Detection
For each file, the agent:
- Analyzes file content to determine the appropriate Android component type
- Checks for existing naming conventions
- Determines the correct prefix/suffix based on Android standards

### 3. Reference Discovery
The agent searches for references to files that will be renamed:
- `R.layout.*` references in Java/Kotlin code
- `tools:context` attributes in layout XML files
- Class instantiations and imports
- Include tags in layout XML files

### 4. Execution
- Creates backups of files before renaming
- Renames files according to Android conventions
- Updates all references throughout the project
- Generates detailed reports

## Safety Features

### Dry Run Mode
By default, the agent runs in dry run mode, showing you exactly what changes will be made without modifying any files.

### Backup Creation
When executing renames, the agent creates `.backup` files for all modified files.

### Error Handling
The agent includes comprehensive error handling and will continue processing even if individual files fail.

## Report Generation

The agent generates a detailed report (`refactoring_report.txt`) containing:

- **File Renames**: Complete list of all files renamed with reasons
- **Reference Updates**: All code references that were updated
- **Statistics**: Summary of changes by file type
- **Breakdown**: Detailed breakdown of changes per file

### Example Report Section

```
📁 FILE RENAMES:
----------------------------------------
• synapse_dialog.xml → dialog_synapse_dialog.xml
  Type: layout
  Reason: Follow Android layout naming convention

• UserProfileMoreBottomSheet.java → UserProfileMoreBottomSheetFragment.java
  Type: java/kotlin
  Reason: Follow Android class naming convention

🔗 REFERENCE UPDATES:
----------------------------------------
📄 CompleteProfileActivity.java:
  Line 634: R.layout.synapse_dialog → R.layout.dialog_synapse_dialog
  Line 669: R.layout.synapse_dialog → R.layout.dialog_synapse_dialog
```

## Requirements

- Python 3.6+
- Android project with standard structure
- Write permissions to the project directory

## Installation

1. Download the `android_refactor_agent.py` script
2. Make it executable: `chmod +x android_refactor_agent.py`
3. Run with Python 3: `python3 android_refactor_agent.py`

## Best Practices

### Before Running
1. **Commit your code**: Ensure all changes are committed to version control
2. **Test the project**: Make sure your project builds and runs correctly
3. **Run dry run first**: Always preview changes before executing

### After Running
1. **Review the report**: Check the generated report for any unexpected changes
2. **Test the project**: Build and test your project to ensure everything works
3. **Clean up backups**: Remove `.backup` files once you're satisfied with the changes

## Troubleshooting

### Common Issues

**Permission Errors**
- Ensure you have write permissions to the project directory
- Run with appropriate user permissions

**File Not Found Errors**
- Verify the project path is correct
- Ensure the project has a standard Android structure

**Reference Update Failures**
- Check the generated report for any failed reference updates
- Manually update any missed references

### Getting Help

If you encounter issues:
1. Check the generated report for error details
2. Verify your project structure matches Android standards
3. Ensure all dependencies are properly installed

## Example Project Analysis

The agent successfully analyzed a real Android project and found:

- **33 files** that needed renaming
- **28 references** that needed updating
- **13 layout files** requiring proper prefixes
- **3 Java/Kotlin files** requiring proper suffixes
- **14 drawable files** requiring proper prefixes
- **3 menu files** requiring proper prefixes

## Contributing

This agent can be extended to support additional Android naming conventions or file types. The modular design makes it easy to add new patterns and rules.

## License

This tool is provided as-is for educational and development purposes. Use at your own risk and always backup your projects before running any automated refactoring tools.