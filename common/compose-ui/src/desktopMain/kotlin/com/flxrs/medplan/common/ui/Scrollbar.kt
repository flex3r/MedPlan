package com.flxrs.medplan.common.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun Scrollbar(modifier: Modifier, state: ScrollState) {
    VerticalScrollbar(
        modifier = modifier,
        adapter = rememberScrollbarAdapter(state)
    )
}