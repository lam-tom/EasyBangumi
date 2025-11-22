# Release Guide - How to Create a Release with APK

This guide explains how to create a GitHub Release that automatically builds and attaches an APK file.

## Quick Start

1. **Merge your changes** to the main branch (or the branch you want to release from)
2. **Create a new release** on GitHub
3. **Wait for the APK** to be built and attached automatically

## Detailed Steps

### Step 1: Prepare Your Code

Before creating a release, ensure:
- Your code is merged to the branch you want to release from (usually `main`)
- All tests pass
- The version number is updated (if needed)

To update the version:
1. Open `buildSrc/src/main/java/com/heyanle/buildsrc/Android.kt`
2. Update `versionCode` (increment by 1)
3. Update `versionName` (e.g., "1.2.3")
4. Commit and push these changes

### Step 2: Create a GitHub Release

#### Option A: Using GitHub Web Interface (Recommended)

1. Go to your repository on GitHub: `https://github.com/lam-tom/EasyBangumi`

2. Click on **"Releases"** in the right sidebar (or go to `https://github.com/lam-tom/EasyBangumi/releases`)

3. Click **"Draft a new release"** button

4. Fill in the release form:
   - **Choose a tag**: Type a new tag name (e.g., `v1.2.3`, `1.2.3`, or `2024.11.22`)
     - If the tag doesn't exist, it will be created
     - Recommended format: `v{versionName}` (e.g., `v1.2.3`)
   
   - **Target**: Select the branch to release from (usually `main` or `copilot/add-google-tv-remote-support-again`)
   
   - **Release title**: Give your release a name (e.g., "Version 1.2.3 - TV Navigation Support")
   
   - **Description**: Write release notes describing what's new:
     ```markdown
     ## What's New
     
     - Added Google TV/Android TV D-pad navigation support
     - Visual focus indicators for TV remote control
     - All UI elements now navigable with TV remote
     
     ## Changes
     
     - Updated AndroidManifest.xml for TV support
     - Added TV focus system with visual feedback
     - Enhanced all interactive UI components
     
     ## Installation
     
     Download the APK file below and install it on your device.
     ```
   
   - **Pre-release**: Check this if it's a beta/test version
   
   - **Set as latest release**: Keep this checked for your main releases

5. Click **"Publish release"**

6. **Wait 5-10 minutes** for the GitHub Action to build the APK

7. **Refresh the release page** - the APK file will appear in the "Assets" section

#### Option B: Using GitHub CLI (For Advanced Users)

If you have [GitHub CLI](https://cli.github.com/) installed:

```bash
# Create a release (this will trigger the APK build)
gh release create v1.2.3 \
  --title "Version 1.2.3 - TV Navigation Support" \
  --notes "Added TV navigation support and visual focus indicators"

# Or create a pre-release
gh release create v1.2.4-beta \
  --title "Version 1.2.4 Beta" \
  --notes "Testing TV navigation features" \
  --prerelease
```

### Step 3: Verify the Build

1. Go to the **"Actions"** tab in your repository
2. Look for the workflow run "Build APK on Release"
3. Click on it to see the build progress
4. If the build fails, check the logs for errors

### Step 4: Download the APK

Once the build completes:
1. Go back to your release page
2. Scroll to the **"Assets"** section
3. Download the APK file (named like `easybangumi-1.2.3-debug.apk`)
4. Install it on your Android/TV device

## Workflow Details

The workflow (`.github/workflows/build-apk-on-release.yml`) automatically:

1. **Triggers** when you create or publish a release
2. **Checks out** your code
3. **Sets up** Java 17 and Gradle
4. **Creates** a dummy `google-services.json` if needed (for Firebase)
5. **Builds** a debug APK using `./gradlew assembleDebug`
6. **Renames** the APK to include version number
7. **Uploads** the APK to your release
8. **Saves** the APK as an artifact (available for 30 days)

## Manual Trigger

You can also trigger the build manually without creating a release:

1. Go to **"Actions"** tab
2. Select **"Build APK on Release"** workflow
3. Click **"Run workflow"** button
4. Select the branch to build from
5. Click **"Run workflow"**

The APK will be available as an **Artifact** (not attached to a release):
- Go to the workflow run
- Scroll to "Artifacts" section
- Download the APK

## Troubleshooting

### Build Fails

**Problem**: The GitHub Action fails to build

**Solutions**:
1. Check the Action logs for specific errors
2. Ensure your code compiles locally: `./gradlew assembleDebug`
3. Check that all dependencies are available
4. Verify `google-services.json` isn't required (workflow creates a dummy one)

### APK Not Appearing

**Problem**: Release is created but no APK appears

**Solutions**:
1. Wait a few more minutes (build can take 5-10 minutes)
2. Check the Actions tab to see if the workflow is still running
3. Look at the workflow logs if it failed
4. Refresh the release page

### Wrong Version Number

**Problem**: APK has wrong version number in filename

**Solution**:
1. The version comes from `buildSrc/src/main/java/com/heyanle/buildsrc/Android.kt`
2. Update `versionName` and `versionCode` there
3. Create a new release

## APK Types

This workflow builds a **debug APK**:
- Not signed with release key
- Includes debugging symbols
- Larger file size
- Good for testing

For **release APKs** (smaller, optimized, signed):
- You need to configure signing keys
- Use the existing `release.yml` workflow
- Requires secrets: `SIGNING_KEY`, `KEY_ALIAS`, `KEY_STORE_PWD`, `KEY_PWD`

## Version Naming Best Practices

Recommended tag formats:
- `v1.2.3` - Semantic versioning (recommended)
- `1.2.3` - Simple version number
- `2024.11.22` - Date-based version
- `v1.2.3-beta` - Pre-release version

Match your tag with the `versionName` in Android.kt for consistency.

## Example Release Process

Here's a complete example workflow:

```bash
# 1. Update version in code
# Edit buildSrc/src/main/java/com/heyanle/buildsrc/Android.kt
# Change versionName to "1.3.0" and versionCode to 130

# 2. Commit and push
git add buildSrc/src/main/java/com/heyanle/buildsrc/Android.kt
git commit -m "Bump version to 1.3.0"
git push origin main

# 3. Create release on GitHub
# - Go to https://github.com/lam-tom/EasyBangumi/releases
# - Click "Draft a new release"
# - Tag: v1.3.0
# - Title: "Version 1.3.0 - TV Navigation Support"
# - Description: Release notes
# - Click "Publish release"

# 4. Wait for build (5-10 minutes)
# - Check Actions tab for progress
# - APK will appear in release assets

# 5. Test the APK
# - Download from release
# - Install on device
# - Verify everything works
```

## Summary

**To create a release with automatic APK build:**
1. Go to GitHub → Releases → "Draft a new release"
2. Enter tag (e.g., `v1.2.3`), title, and description
3. Click "Publish release"
4. Wait 5-10 minutes
5. Download APK from release assets

The workflow handles everything automatically! 🚀
