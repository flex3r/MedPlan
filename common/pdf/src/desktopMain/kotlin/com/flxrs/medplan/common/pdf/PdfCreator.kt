package com.flxrs.medplan.common.pdf

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.vandeseer.easytable.TableDrawer
import org.vandeseer.easytable.structure.Row
import org.vandeseer.easytable.structure.Table
import org.vandeseer.easytable.structure.cell.TextCell
import java.awt.Desktop
import java.io.File

internal actual suspend fun createPdfWithTablePlatform(rowsAndColumns: List<List<String>>): Unit = withContext(Dispatchers.Default) {
    runCatching {
        val doc = PDDocument()
        val columns = rowsAndColumns.firstOrNull()?.size ?: return@withContext
        val columnsOfWidth = Array(columns) {
            if (it == 0) 250f else 75f
        }.toFloatArray()

        val page = PDPage(PDRectangle(PDRectangle.A4.height, PDRectangle.A4.width)).also {
            doc.addPage(it)
        }

        val initialTable = Table.builder()
            .addColumnsOfWidth(*columnsOfWidth)
            .padding(4f)

        val table = rowsAndColumns.fold(initialTable) { table, rowContent ->
            val row = rowContent.fold(Row.builder()) { row, columnContent ->
                row.add(TextCell.builder().text(columnContent).borderWidth(1f).build())
            }
            table.addRow(row.padding(4f).build())
        }.build()

        PDPageContentStream(doc, page).use { contentStream ->
            TableDrawer.builder()
                .contentStream(contentStream)
                .startX(20f)
                .startY(page.mediaBox.upperRightY - 20f)
                .table(table)
                .build()
                .draw()
        }

        val output = File(System.getProperty("java.io.tmpdir"), "plan.pdf")
        doc.save(output)
        Desktop.getDesktop().open(output)
    }.getOrElse {
        println(it)
    }
}