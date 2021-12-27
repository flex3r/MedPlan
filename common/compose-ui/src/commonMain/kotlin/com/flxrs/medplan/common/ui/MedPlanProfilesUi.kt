package com.flxrs.medplan.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.flxrs.medplan.common.profiles.MedPlanProfiles

@Composable
fun MedPlanProfilesContent(component: MedPlanProfiles) {
    val model by component.models.subscribeAsState()

    Scaffold(topBar = { TopAppBar(title = { Text(text = "MedPlan") }) }) {
        var dialogOpen by remember { mutableStateOf(false) }
        var dialogText by remember { mutableStateOf("") }

        if (dialogOpen) {
            EditDialog(
                title = "Neuer Plan",
                initial = dialogText,
                onClose = { dialogOpen = false },
                onPositiveResult = { text ->
                    dialogOpen = false
                    component.onAddProfileClicked(text)
                }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.Start
            ) {
                model.profiles.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            content = { Text(text = item.name) },
                            onClick = { component.onProfileClicked(item.profileId) }
                        )
                        IconButton(
                            modifier = Modifier.wrapContentWidth(),
                            content = { Icon(imageVector = Icons.Default.Delete, contentDescription = null) },
                            onClick = { component.onDeleteProfileClicked(item.profileId) }
                        )
                    }
                }

                if (model.profiles.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    content = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Neuer Plan")
                    },
                    onClick = {
                        dialogText = ""
                        dialogOpen = true
                    }
                )
            }
            Scrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                state = scrollState,
            )
        }
    }
}