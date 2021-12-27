package com.flxrs.medplan.common.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.WindowPosition

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun EditDialog(
    title: String,
    initial: String,
    onClose: () -> Unit,
    onPositiveResult: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(initial, TextRange(initial.length))) }
    val focusRequester = remember { FocusRequester() }
    Dialog(
        onCloseRequest = onClose,
        title = title,
        state = DialogState(height = 200.dp, position = WindowPosition(Alignment.Center)),
        icon = null,
    ) {
        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose {}
        }

        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = textFieldValue,
                    maxLines = 1,
                    onValueChange = { textFieldValue = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onKeyUp(key = Key.Enter) { onPositiveResult(textFieldValue.text) }
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    TextButton(
                        onClick = onClose,
                        content = { Text("Abbrechen") },
                    )
                    TextButton(
                        onClick = { onPositiveResult(textFieldValue.text) },
                        content = { Text("Speichern") },
                    )
                }
            }
        }
    }
}