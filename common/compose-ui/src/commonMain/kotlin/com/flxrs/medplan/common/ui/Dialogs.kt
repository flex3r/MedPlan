package com.flxrs.medplan.common.ui

import androidx.compose.runtime.Composable

@Composable
internal expect fun EditDialog(
    title: String = "Bearbeiten",
    initial: String = "",
    onClose: () -> Unit,
    onPositiveResult: (String) -> Unit
)