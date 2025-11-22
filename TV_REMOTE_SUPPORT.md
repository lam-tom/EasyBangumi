# TV Remote Control Support Documentation

## Overview
This document describes the Google TV / Android TV remote control support implementation for EasyBangumi.

## Features Implemented

### 1. TV Feature Declarations (AndroidManifest.xml)
- Added `<uses-feature>` declarations for:
  - `android.hardware.touchscreen` (required=false) - Makes touchscreen optional for TV devices
  - `android.software.leanback` (required=false) - Declares TV app compatibility
- Added `android.intent.category.LEANBACK_LAUNCHER` to splash activity intent filter for TV launcher visibility

### 2. Focus Management Utilities (TvFocusModifiers.kt)
Created reusable Compose modifiers for TV remote navigation:

#### `Modifier.tvFocusable()`
- Adds focusability with customizable visual feedback
- Parameters:
  - `focusedBorderColor`: Border color when focused (default: primary theme color)
  - `focusedBorderWidth`: Border width when focused (default: 3.dp)
  - `focusedScale`: Scale factor when focused (default: 1.05f)
  - `cornerRadius`: Border corner radius (default: 4.dp)
  - `interactionSource`: For managing focus state
- Features:
  - Animated border appearance
  - Smooth scale animation
  - Theme-aware styling

#### `Modifier.tvSimpleFocusable()`
- Simplified version with default styling
- Uses primary theme color for border
- Automatically manages interaction source
- Suitable for simple list items and buttons

#### `Modifier.onTvFocusChanged()`
- Callback modifier for custom focus handling
- Provides boolean focus state

### 3. UI Components Updated

#### CartoonCard.kt
- **CartoonCardWithCover**: Added `tvFocusable()` modifier for anime card components
- **CartoonStarCardWithCover**: Added `tvFocusable()` modifier for starred anime cards
- Visual feedback: Border + scale animation when focused via D-pad

#### CombineClickIconButton.kt
- Added `tvFocusable()` modifier to custom icon button component
- Circular focus indicator (20.dp corner radius for round appearance)
- Used throughout the app for various icon actions

#### Preference.kt
- Updated all 5 preference item variants:
  - `BooleanPreferenceItem` - Toggle preferences
  - `EmumPreferenceItem` - Enum selection preferences
  - `TextPreferenceItem` - Text input preferences
  - `LongPreferenceItem` - Number input preferences
  - List selection preferences
- All now support D-pad navigation with focus indicators

#### Action.kt
- Updated `Action` component for action rows
- Added `tvSimpleFocusable()` modifier to action buttons
- Used in various screens for quick actions

#### Home.kt
- Updated source selection list items in bottom sheet
- Added `tvSimpleFocusable()` to source selection items
- Enables D-pad navigation through available anime sources

## D-pad Navigation Support

### Automatic D-pad Handling
The following key events are automatically handled by Compose's focus system:
- **DPAD_UP**: Navigate to previous focusable element (vertical)
- **DPAD_DOWN**: Navigate to next focusable element (vertical)
- **DPAD_LEFT**: Navigate to previous focusable element (horizontal)
- **DPAD_RIGHT**: Navigate to next focusable element (horizontal)
- **DPAD_CENTER** / **ENTER**: Trigger onClick action on focused element

### Back Button Navigation
- Already implemented via `BackHandler` composable in:
  - MainActivity
  - Main.kt navigation screens
  - Individual screen components
- TV remote BACK button works correctly without additional code

## Material 3 Components with Built-in Focus Support

These components already support D-pad navigation without modification:
- `NavigationBarItem` - Bottom navigation tabs
- `NavigationRailItem` - Side navigation (tablet/TV mode)
- `FloatingActionButton` - FAB buttons
- `IconButton` - Standard icon buttons
- `TextButton` - Text buttons
- `Button` - Regular buttons
- `Switch` - Toggle switches
- `RadioButton` - Radio buttons
- `Checkbox` - Checkboxes

## Testing Guide

### On Android TV / Google TV Emulator
1. Create Android TV emulator in Android Studio:
   - Tools > Device Manager > Create Device
   - Select TV category > Choose Android TV device
   - Download appropriate system image
   - Create and start emulator

2. Install app on TV emulator:
   ```bash
   ./gradlew installDebug
   ```

3. Test D-pad navigation:
   - Use emulator's virtual D-pad
   - Or use keyboard: Arrow keys for D-pad, Enter for select, Escape for back

### On Physical Android TV Device
1. Enable Developer Options on TV device
2. Enable USB/Network debugging
3. Connect via ADB:
   ```bash
   adb connect <TV_IP_ADDRESS>:5555
   ./gradlew installDebug
   ```

4. Test with TV remote:
   - Navigate through app using D-pad
   - Verify focus indicators appear (borders, scale effects)
   - Test BACK button navigation
   - Verify all clickable elements are reachable via D-pad

### Key Testing Scenarios

#### Home Screen
- [ ] Navigate between bottom navigation tabs
- [ ] Navigate through anime cards in home feed
- [ ] Focus on search icon
- [ ] Open source selection bottom sheet
- [ ] Navigate through sources list in bottom sheet
- [ ] Select different source with D-pad + Enter

#### Anime Cards
- [ ] Navigate through grid of anime cards
- [ ] Verify focus border appears on each card
- [ ] Verify scale animation works smoothly
- [ ] Select card with Enter key
- [ ] Long press functionality (if applicable)

#### Settings/Preferences
- [ ] Navigate through preference items
- [ ] Toggle boolean preferences with D-pad
- [ ] Open selection dialogs
- [ ] Navigate options in dialogs
- [ ] Confirm/cancel with Enter

#### Video Player (if accessible)
- [ ] Focus on player controls
- [ ] Navigate between control buttons
- [ ] Test play/pause, skip, etc.
- [ ] Test fullscreen toggle
- [ ] Test back navigation from player

#### More/Settings Screen
- [ ] Navigate through menu items
- [ ] Test action buttons
- [ ] Test nested navigation

## Known Limitations

1. **Initial Focus**: First element focus on screen load may need tuning
2. **Custom Gestures**: Swipe gestures may not work well with D-pad (expected)
3. **Text Input**: On-screen keyboard needed for text fields on TV devices
4. **Complex Layouts**: Some complex nested layouts may need focus order adjustment

## Future Enhancements

Potential improvements for better TV experience:
1. **Custom Focus Order**: Use `Modifier.focusOrder()` for complex screens
2. **Focus Restoration**: Save and restore focus position on navigation
3. **TV-Optimized Layouts**: Dedicated TV UI for larger screens
4. **Voice Search**: Integrate voice input for search functionality
5. **Remote Control Shortcuts**: Add shortcuts for common actions (play/pause, etc.)
6. **TV-Specific Gestures**: Map additional remote buttons to app functions

## Code Examples

### Adding Focus to New Components

```kotlin
// Simple focus with default styling
ListItem(
    modifier = Modifier
        .tvSimpleFocusable()
        .clickable { /* action */ },
    headlineContent = { Text("Item") }
)

// Custom focus styling
Card(
    modifier = Modifier
        .tvFocusable(
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            focusedScale = 1.1f,
            cornerRadius = 8.dp
        )
        .clickable { /* action */ }
) {
    // Card content
}

// Focus with callback
Box(
    modifier = Modifier
        .tvFocusable()
        .onTvFocusChanged { isFocused ->
            // Handle focus change
        }
        .clickable { /* action */ }
) {
    // Box content
}
```

### Modifier Order Important!
Always apply focus modifiers BEFORE clickable:
```kotlin
// ✅ CORRECT
Modifier
    .tvFocusable()
    .clickable { }

// ❌ WRONG - Focus won't work properly
Modifier
    .clickable { }
    .tvFocusable()
```

## Technical Details

### Focus System Architecture
- Uses Compose's built-in focus system (`Modifier.focusable()`)
- Focus state tracked via `MutableInteractionSource`
- Visual feedback via `collectIsFocusedAsState()`
- Animations using `animateFloatAsState`
- Theme-aware colors via `MaterialTheme.colorScheme`

### Performance Considerations
- Animations are hardware-accelerated
- Focus indicators only rendered when focused (conditional modifiers)
- No impact on touch-based navigation
- Minimal overhead for non-TV devices

## Troubleshooting

### Focus Not Visible
- Check if theme colors provide sufficient contrast
- Verify focus modifier is applied before clickable modifier
- Ensure component is actually focusable (has clickable or similar)

### Focus Not Moving
- Check focus order in complex layouts
- Verify no modal dialogs blocking focus
- Check if focus is trapped in a sub-component

### Back Button Not Working
- Verify `BackHandler` is implemented in screen
- Check navigation controller setup
- Test with debug logging in BackHandler callback

## References
- [Android TV Input Handling](https://developer.android.com/training/tv/start/navigation)
- [Jetpack Compose Focus](https://developer.android.com/jetpack/compose/touch-input/focus)
- [TV Design Guidelines](https://developer.android.com/design/ui/tv)
