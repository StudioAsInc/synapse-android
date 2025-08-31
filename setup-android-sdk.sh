#!/bin/bash

# Android SDK Setup Script for Synapse Project
# This script downloads and configures the Android SDK

set -e  # Exit on any error

echo "🚀 Setting up Android SDK for Synapse project..."

# Configuration
SDK_DIR="/opt/android-sdk"
SDK_TOOLS_VERSION="11076708"
SDK_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-${SDK_TOOLS_VERSION}_latest.zip"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if running as root or with sudo
if [[ $EUID -eq 0 ]]; then
    print_warning "Running as root. This is fine for system-wide SDK installation."
else
    print_warning "Not running as root. You may need sudo permissions for /opt/android-sdk"
fi

# Create SDK directory
print_status "Creating SDK directory at $SDK_DIR"
sudo mkdir -p "$SDK_DIR"
sudo chown $USER:$USER "$SDK_DIR"

# Change to SDK directory
cd "$SDK_DIR"

# Download Android SDK command-line tools
print_status "Downloading Android SDK command-line tools..."
if [ -f "commandlinetools-linux-${SDK_TOOLS_VERSION}_latest.zip" ]; then
    print_status "SDK tools already downloaded, skipping download"
else
    wget "$SDK_TOOLS_URL"
fi

# Extract SDK tools
print_status "Extracting SDK tools..."
unzip -q "commandlinetools-linux-${SDK_TOOLS_VERSION}_latest.zip"

# Set up proper directory structure
print_status "Setting up SDK directory structure..."
mkdir -p cmdline-tools/latest
mv cmdline-tools/bin cmdline-tools/lib cmdline-tools/source.properties cmdline-tools/NOTICE.txt cmdline-tools/latest/

# Set environment variables
export ANDROID_HOME="$SDK_DIR"
export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools"

# Accept licenses
print_status "Accepting Android SDK licenses..."
yes | sdkmanager --licenses

# Install required SDK components
print_status "Installing required Android SDK components..."
sdkmanager "platforms;android-36" "platforms;android-32" "build-tools;36.0.0" "build-tools;32.0.0" "platform-tools" "extras;android;m2repository" "extras;google;m2repository"

# Create local.properties in the project root
print_status "Creating local.properties file..."
cd /workspace
echo "sdk.dir=$SDK_DIR" > local.properties

# Set permissions
print_status "Setting proper permissions..."
sudo chown -R $USER:$USER "$SDK_DIR"

print_status "✅ Android SDK setup complete!"
print_status "SDK location: $SDK_DIR"
print_status "local.properties created with SDK path"

# Verify installation
print_status "Verifying installation..."
if [ -f "$SDK_DIR/cmdline-tools/latest/bin/sdkmanager" ]; then
    print_status "✅ SDK Manager found"
else
    print_error "❌ SDK Manager not found"
    exit 1
fi

if [ -d "$SDK_DIR/platforms/android-36" ]; then
    print_status "✅ Android API 36 platform installed"
else
    print_error "❌ Android API 36 platform not found"
    exit 1
fi

print_status "🎉 Android SDK is ready for use!"
print_status "You can now run: ./gradlew build"