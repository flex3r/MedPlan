package com.flxrs.medplan.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.flxrs.medplan.common.main.*

data class ItemDialogState(
    val isOpen: Boolean = false,
    val text: String = "",
    val title: String = "",
    val selectedItem: MedPlanMainItem? = null,
    val selectedColumn: Int = 0,
)

@Composable
fun MedPlanMainContent(component: MedPlanMain) {
    val model by component.models.subscribeAsState()

    var dialogState by remember { mutableStateOf(ItemDialogState()) }
    fun openDialog(title: String = "Bearbeiten", item: MedPlanMainItem? = null, column: Int = 0) {
        val dialogText = when (column) {
            0    -> item?.name.orEmpty()
            else -> item?.findByColumn(column)?.amount.orEmpty()
        }
        dialogState = ItemDialogState(
            isOpen = true,
            text = dialogText,
            title = title,
            selectedItem = item,
            selectedColumn = column,
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text(text = "MedPlan - ${model.profile}") },
            navigationIcon = {
                IconButton(onClick = component::onBackClicked) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        )

        if (dialogState.isOpen) {
            EditDialog(
                title = dialogState.title,
                initial = dialogState.text,
                onClose = { dialogState = dialogState.copy(isOpen = false) },
                onPositiveResult = { text ->
                    dialogState = dialogState.copy(isOpen = false)
                    val item = dialogState.selectedItem
                    if (item == null) {
                        component.onAddItemClicked(text)
                        return@EditDialog
                    }

                    when (val column = dialogState.selectedColumn) {
                        0    -> component.onItemNameChanged(item.id, text)
                        else -> {
                            val time = UsageTime.values()[column - 1]
                            val usage = Usage(time, text)
                            component.onItemUsageChanged(item.id, usage)
                        }
                    }
                }
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            MedPlanTable(
                data = model.items,
                onClick = { column, item ->
                    openDialog(column = column, item = item)
                },
                onDeleteClick = { component.onItemDeleteClicked(it.id) },
                headerCellContent = { HeaderCell(it) },
                cellContent = { idx, item ->
                    ContentCell(idx, item) {
                        val time = UsageTime.values()[idx - 1]
                        val usage = Usage(time, amount = "")
                        component.onItemUsageChanged(item.id, usage)
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Neues Medikament")
                },
                onClick = { openDialog(title = "Neues Medikament") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                content = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Drucken")
                },
                onClick = component::onPrintClicked
            )
        }
    }
}

@Composable
fun MedPlanTable(
    data: List<MedPlanMainItem>,
    columnCount: Int = UsageTime.values().size + 1,
    modifier: Modifier = Modifier,
    onClick: (column: Int, item: MedPlanMainItem) -> Unit,
    onDeleteClick: (item: MedPlanMainItem) -> Unit,
    headerCellContent: @Composable (index: Int) -> Unit,
    cellContent: @Composable (index: Int, item: MedPlanMainItem) -> Unit,
) {
    Surface(modifier = modifier.padding(16.dp)) {
        Row {
            (0 until columnCount).forEach { columnIndex ->
                Column(modifier = Modifier.weight(1f)) {
                    (0..data.size).forEach { index ->
                        Surface(
                            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = index >= 1) { onClick(columnIndex, data[index - 1]) },
                        ) {
                            when (index) {
                                0    -> headerCellContent(columnIndex)
                                else -> cellContent(columnIndex, data[index - 1])
                            }
                        }
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                (0..data.size).forEach { index ->
                    when (index) {
                        0    -> Text(text = "", modifier = Modifier.padding(16.dp))
                        else -> IconButton(
                            onClick = { onDeleteClick(data[index - 1]) },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderCell(index: Int) {
    val usageTimes = UsageTime.values()
    val value = when (index) {
        0    -> "Medikament"
        else -> usageTimes
            .getOrNull(index = index - 1)
            ?.displayName
            .orEmpty()
    }
    Text(
        text = value,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(16.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textDecoration = TextDecoration.Underline,
    )
}

@Composable
private fun ContentCell(index: Int, item: MedPlanMainItem, onClearClick: () -> Unit) {
    val value = when (index) {
        0    -> item.name
        else -> item.findByColumn(index)?.amount.orEmpty()
    }
    Row(modifier = Modifier.heightIn(min = 48.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = value,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (index > 0 && value.isNotBlank()) {
            IconButton(
                onClick = onClearClick,
                modifier = Modifier.wrapContentWidth(align = Alignment.End, unbounded = true),
                content = { Icon(imageVector = Icons.Default.Clear, contentDescription = null) }
            )
        }
    }

}

private fun MedPlanMainItem.findByColumn(columnIndex: Int): Usage? = UsageTime.values()
    .getOrNull(index = columnIndex - 1)
    ?.let { time ->
        usages.find { it.time == time }
    }
