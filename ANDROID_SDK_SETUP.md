# Android SDK Setup Guide

This guide explains how to set up the Android SDK for the Synapse project.

## Quick Setup

Run the automated setup script:

```bash
./setup-android-sdk.sh
```

This script will:
- Download the Android SDK command-line tools
- Install required SDK components (API 32, 36, build tools)
- Configure the environment
- Create the `local.properties` file

## Manual Setup

If you prefer to set up the SDK manually:

### 1. Download Android SDK

```bash
# Create SDK directory
sudo mkdir -p /opt/android-sdk
sudo chown $USER:$USER /opt/android-sdk

# Download command-line tools
cd /opt/android-sdk
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip

# Extract and set up
unzip commandlinetools-linux-11076708_latest.zip
mkdir -p cmdline-tools/latest
mv cmdline-tools/bin cmdline-tools/lib cmdline-tools/source.properties cmdline-tools/NOTICE.txt cmdline-tools/latest/
```

### 2. Set Environment Variables

```bash
export ANDROID_HOME=/opt/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools
```

### 3. Install SDK Components

```bash
# Accept licenses
yes | sdkmanager --licenses

# Install required components
sdkmanager "platforms;android-36" "platforms;android-32" "build-tools;36.0.0" "build-tools;32.0.0" "platform-tools"
```

### 4. Configure Project

Create `local.properties` in the project root:

```bash
echo "sdk.dir=/opt/android-sdk" > local.properties
```

## Verification

After setup, verify the installation:

```bash
# Check if SDK is accessible
ls $ANDROID_HOME/platforms/

# Try building the project
./gradlew build
```

## Troubleshooting

### Permission Issues
If you encounter permission issues with `/opt/android-sdk`:

```bash
sudo chown -R $USER:$USER /opt/android-sdk
```

### SDK Not Found
If the build still can't find the SDK:

1. Verify `local.properties` exists and contains the correct path
2. Check that the SDK components are installed:
   ```bash
   ls $ANDROID_HOME/platforms/
   ls $ANDROID_HOME/build-tools/
   ```

### Network Issues
If you're behind a proxy or have network issues:

```bash
# Set proxy for sdkmanager
export HTTP_PROXY=http://proxy.company.com:8080
export HTTPS_PROXY=http://proxy.company.com:8080
```

## SDK Components

The following components are installed:
- **Platforms**: Android API 32, 36
- **Build Tools**: 32.0.0, 36.0.0
- **Platform Tools**: Latest version
- **Extras**: Android M2Repository, Google M2Repository

## Alternative SDK Locations

You can install the SDK in other locations by modifying the script or manually:

- `~/android-sdk` (user home directory)
- `/usr/local/android-sdk` (system-wide)
- `./android-sdk` (project-local)

Just update the `local.properties` file accordingly.