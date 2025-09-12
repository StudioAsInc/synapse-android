#!/bin/bash

# Android Project Refactoring Agent - Usage Example
# This script demonstrates how to use the Android refactoring agent

echo "🚀 Android Project Refactoring Agent - Usage Example"
echo "=================================================="
echo ""

# Check if the agent script exists
if [ ! -f "android_refactor_agent.py" ]; then
    echo "❌ Error: android_refactor_agent.py not found in current directory"
    echo "Please ensure the script is in the same directory as this example"
    exit 1
fi

# Check if Python 3 is available
if ! command -v python3 &> /dev/null; then
    echo "❌ Error: Python 3 is not installed or not in PATH"
    echo "Please install Python 3 to use this agent"
    exit 1
fi

echo "✅ Prerequisites check passed"
echo ""

# Get the current directory (assuming it's an Android project)
PROJECT_PATH="."

echo "📋 Usage Examples:"
echo "=================="
echo ""

echo "1. 🔍 DRY RUN (Preview changes without modifying files):"
echo "   python3 android_refactor_agent.py $PROJECT_PATH"
echo ""

echo "2. 🚀 EXECUTE REFACTORING (Actually rename files):"
echo "   python3 android_refactor_agent.py $PROJECT_PATH --execute"
echo ""

echo "3. 📄 View the generated report:"
echo "   cat refactoring_report.txt"
echo ""

echo "⚠️  IMPORTANT SAFETY NOTES:"
echo "=========================="
echo "• Always run dry run first to preview changes"
echo "• Commit your code to version control before executing"
echo "• Test your project after refactoring"
echo "• Review the generated report carefully"
echo ""

# Ask user if they want to run dry run
read -p "Would you like to run a dry run now? (y/n): " -n 1 -r
echo ""

if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo ""
    echo "🔍 Running dry run analysis..."
    echo "=============================="
    python3 android_refactor_agent.py "$PROJECT_PATH"
    
    echo ""
    echo "📄 Report generated: refactoring_report.txt"
    echo ""
    echo "Next steps:"
    echo "1. Review the report above"
    echo "2. If satisfied with changes, run: python3 android_refactor_agent.py $PROJECT_PATH --execute"
    echo "3. Test your project after refactoring"
else
    echo ""
    echo "ℹ️  To run the agent manually:"
    echo "   python3 android_refactor_agent.py $PROJECT_PATH"
    echo ""
fi

echo ""
echo "🎯 What the agent will do:"
echo "========================="
echo "• Analyze your Android project structure"
echo "• Identify files that don't follow Android naming conventions"
echo "• Rename files with appropriate prefixes/suffixes:"
echo "  - Layout files: activity_, fragment_, item_, dialog_, etc."
echo "  - Java/Kotlin: Activity, Fragment, Adapter, Service, etc."
echo "  - Drawables: ic_, bg_, button_, etc."
echo "  - Menus: menu_ prefix"
echo "• Update all references throughout your project"
echo "• Generate a detailed report of all changes"
echo ""

echo "✨ Happy refactoring!"