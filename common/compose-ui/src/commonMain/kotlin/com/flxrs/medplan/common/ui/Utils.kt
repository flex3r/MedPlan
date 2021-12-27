package com.flxrs.medplan.common.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*

internal fun Modifier.onKeyUp(key: Key, action: () -> Unit): Modifier =
    onKeyEvent { event ->
        when {
            event.type == KeyEventType.KeyUp && event.key == key -> action().run { true }
            else                                                 -> false
        }
    }