package com.flxrs.medplan.common.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun Scrollbar(modifier: Modifier, state: ScrollState)