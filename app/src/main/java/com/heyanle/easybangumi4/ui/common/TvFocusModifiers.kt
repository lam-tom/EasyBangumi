package com.heyanle.easybangumi4.ui.common

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * TV Focus management utilities for D-pad navigation
 * Provides visual feedback and focus handling for TV remote controls
 */

/**
 * Makes a composable focusable with visual feedback for TV remote navigation
 * Adds a border and scale effect when focused
 */
@Composable
fun Modifier.tvFocusable(
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    focusedBorderWidth: Dp = 3.dp,
    focusedScale: Float = 1.05f,
    cornerRadius: Dp = 4.dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
): Modifier = composed {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isFocused) focusedScale else 1f,
        label = "focus_scale"
    )

    this
        .scale(scale)
        .then(
            if (isFocused) {
                Modifier.border(
                    width = focusedBorderWidth,
                    color = focusedBorderColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
            } else {
                Modifier
            }
        )
        .focusable(interactionSource = interactionSource)
}

/**
 * Simplified TV focusable modifier for simple use cases
 * Uses default styling and automatically manages interaction source
 */
fun Modifier.tvSimpleFocusable(): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusedBorderColor = MaterialTheme.colorScheme.primary
    
    this
        .then(
            if (isFocused) {
                Modifier.border(
                    width = 3.dp,
                    color = focusedBorderColor,
                    shape = RoundedCornerShape(4.dp)
                )
            } else {
                Modifier
            }
        )
        .focusable(interactionSource = interactionSource)
}

/**
 * Focus callback modifier that notifies when focus state changes
 */
fun Modifier.onTvFocusChanged(onFocusChanged: (Boolean) -> Unit): Modifier {
    return this.onFocusChanged { focusState ->
        onFocusChanged(focusState.isFocused)
    }
}
