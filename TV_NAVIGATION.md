# Google TV / Android TV Navigation Guide

## Overview

EasyBangumi now supports full navigation using Google TV/Android TV remote controls. The app has been enhanced with D-pad navigation support and visual focus indicators to provide a seamless TV experience.

## Features

### 1. TV Platform Support

The app is now compatible with Google TV and Android TV devices:

- **Leanback Support**: Declared as optional feature for TV compatibility
- **Touchscreen Optional**: App can be installed on devices without touchscreen
- **TV Launcher**: App appears in the TV launcher interface

### 2. D-pad Navigation

All interactive UI elements are now fully navigable using the TV remote's D-pad:

- **Up/Down/Left/Right**: Navigate between focusable elements
- **Center/Enter**: Activate focused elements (equivalent to tap/click)
- **Back**: Navigate back in the app hierarchy

### 3. Visual Focus Indicators

When navigating with the D-pad, focused elements provide clear visual feedback:

- **Border Highlight**: Focused elements show a colored border (uses primary theme color)
- **Scale Animation**: Some elements slightly enlarge when focused (1.05x scale)
- **Smooth Transitions**: Animated transitions between focus states

## Implementation Details

### TV Focus System

The TV focus system is implemented through reusable Compose modifiers in `TvFocusSupport.kt`:

#### `tvFocusable()`
Complete TV focus support with both border and scale animation:
```kotlin
Modifier.tvFocusable(
    interactionSource = interactionSource,
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    focusedBorderWidth = 3.dp,
    scaleOnFocus = true,
    focusedScale = 1.05f,
    cornerRadius = 4.dp
)
```

#### `tvFocusableBorder()`
TV focus support with only border feedback (no scaling):
```kotlin
Modifier.tvFocusableBorder(
    interactionSource = interactionSource,
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    focusedBorderWidth = 3.dp,
    cornerRadius = 0.dp
)
```

### Components with TV Support

The following UI components have been enhanced with TV focus support:

1. **CartoonCard Components**
   - `CartoonCardWithCover`: Anime/manga cards with cover images
   - `CartoonStarCardWithCover`: Starred/favorite content cards
   - `CartoonCardWithoutCover`: Text-based content cards

2. **Navigation Elements**
   - `NavigationBarItem`: Bottom navigation bar items (Material3 default)
   - `NavigationRailItem`: Side navigation rail items (Material3 default)

3. **Preference Items**
   - `BooleanPreferenceItem`: Toggle settings
   - `EmumPreferenceItem`: Enum selection settings
   - `StringSelectPreferenceItem`: String selection settings
   - `StringEditPreferenceItem`: Text input settings
   - `LongEditPreferenceItem`: Number input settings

4. **Interactive Elements**
   - `Action`: Action buttons in button rows
   - `CombineClickIconButton`: Icon buttons with combined click/long-press

5. **Dialog Elements**
   - Checkbox rows in selection dialogs
   - Tag selection dialogs
   - Source selection dialogs

### Manifest Configuration

The AndroidManifest.xml has been updated with the following declarations:

```xml
<!-- TV support declarations -->
<uses-feature android:name="android.software.leanback" android:required="false" />
<uses-feature android:name="android.hardware.touchscreen" android:required="false" />

<!-- Launcher activity includes LEANBACK_LAUNCHER category -->
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
    <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
</intent-filter>
```

## Usage

### For Users

1. **Installation**: Install the app on your Google TV or Android TV device from the TV app store
2. **Navigation**: Use your TV remote's D-pad to navigate
3. **Selection**: Press the center button or Enter to select items
4. **Back**: Press the Back button to go back

### For Developers

To add TV focus support to new components:

1. Import the TV focus support utilities:
```kotlin
import com.heyanle.easybangumi4.ui.common.tvFocusable
import com.heyanle.easybangumi4.ui.common.tvFocusableBorder
```

2. Create an interaction source:
```kotlin
val interactionSource = remember { MutableInteractionSource() }
```

3. Apply the modifier before clickable:
```kotlin
Modifier
    .tvFocusable(interactionSource = interactionSource)
    .clickable(
        interactionSource = interactionSource,
        indication = null
    ) {
        // Click action
    }
```

## Testing

### Testing on TV Devices

1. Install the app on a Google TV or Android TV device
2. Navigate through the app using only the TV remote
3. Verify visual feedback appears on focused elements
4. Test all interactive elements for proper focus behavior

### Testing on Phone/Tablet

The TV navigation features are backward compatible and won't interfere with touch navigation on phones and tablets.

## Guidelines

### Android TV Design Guidelines

This implementation follows Android TV design guidelines:

- **Focus Indicators**: Clear visual feedback for focused elements
- **D-pad Navigation**: Logical navigation order between elements
- **Accessibility**: All interactive elements are keyboard/D-pad accessible
- **Performance**: Smooth animations and responsive feedback

### Best Practices

1. **Always use InteractionSource**: Reuse the same `MutableInteractionSource` for both focus and click modifiers
2. **Set indication to null**: Use `indication = null` in clickable modifiers when using custom focus indicators
3. **Choose appropriate modifier**: Use `tvFocusable()` for cards/buttons, `tvFocusableBorder()` for list items
4. **Consider corner radius**: Match the corner radius parameter with the component's visual design

## Future Enhancements

Possible future improvements:

- Custom focus animations for specific components
- Focus sound effects
- Optimized grid layouts for TV screens
- TV-specific UI layouts for large screens
- Voice search integration
- Initial focus positioning for better UX

## Troubleshooting

### Focus Not Visible

If focus indicators are not appearing:
1. Verify the component uses `tvFocusable()` or `tvFocusableBorder()`
2. Check that the same `InteractionSource` is used for both focus and click
3. Ensure `indication = null` is set in the clickable modifier

### Navigation Issues

If D-pad navigation isn't working correctly:
1. Verify the component is marked as focusable
2. Check component ordering in the layout
3. Ensure no elements are blocking focus traversal

## References

- [Android TV Design Guidelines](https://developer.android.com/design/ui/tv)
- [TV App Quality Guidelines](https://developer.android.com/docs/quality-guidelines/tv-app-quality)
- [Compose Focus](https://developer.android.com/jetpack/compose/touch-input/focus)
