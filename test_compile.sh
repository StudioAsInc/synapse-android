#!/bin/bash

# Test compilation script
echo "Testing compilation of key files..."

# Set Android SDK path
export ANDROID_HOME=/opt/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

# Navigate to workspace
cd /workspace

# Run the build
./gradlew compileDebugJavaWithJavac --console=plain 2>&1 | grep -E "(BUILD|FAILED|error:|Error:)" | tail -50

echo "Done."