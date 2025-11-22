package com.heyanle.easybangumi4.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * TV focus support utilities for D-pad navigation
 * Provides visual feedback when UI elements are focused
 */

/**
 * Adds TV-friendly focus support with visual feedback
 * - Shows a border when focused
 * - Slightly scales up the element when focused
 * - Animates transitions smoothly
 */
@Composable
fun Modifier.tvFocusable(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    focusedBorderWidth: Dp = 3.dp,
    scaleOnFocus: Boolean = true,
    focusedScale: Float = 1.05f,
    cornerRadius: Dp = 4.dp
): Modifier = composed {
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isFocused && scaleOnFocus) focusedScale else 1f,
        label = "focus_scale"
    )
    
    val borderWidth by animateDpAsState(
        targetValue = if (isFocused) focusedBorderWidth else 0.dp,
        label = "focus_border"
    )
    
    this
        .scale(scale)
        .border(
            border = BorderStroke(borderWidth, focusedBorderColor),
            shape = RoundedCornerShape(cornerRadius)
        )
        .focusable(interactionSource = interactionSource)
}

/**
 * Simplified TV focusable modifier without scaling
 * Useful for items where scaling might cause layout issues
 */
@Composable
fun Modifier.tvFocusableBorder(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    focusedBorderWidth: Dp = 3.dp,
    cornerRadius: Dp = 4.dp
): Modifier = composed {
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    val borderWidth by animateDpAsState(
        targetValue = if (isFocused) focusedBorderWidth else 0.dp,
        label = "focus_border"
    )
    
    this
        .border(
            border = BorderStroke(borderWidth, focusedBorderColor),
            shape = RoundedCornerShape(cornerRadius)
        )
        .focusable(interactionSource = interactionSource)
}
