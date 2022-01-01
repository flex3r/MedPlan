package com.flxrs.medplan.common.pdf

import com.flxrs.medplan.common.pdf.PdfCreator.PDF_OFFSET_X
import com.flxrs.medplan.common.pdf.PdfCreator.PDF_OFFSET_Y
import com.flxrs.medplan.common.pdf.PdfCreator.PDF_TITLE_FONT_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD
import org.vandeseer.easytable.TableDrawer
import org.vandeseer.easytable.structure.Row
import org.vandeseer.easytable.structure.Table
import org.vandeseer.easytable.structure.cell.TextCell
import java.awt.Color
import java.awt.Desktop
import java.io.File

internal actual suspend fun createPdfWithTablePlatform(title: String, rowsAndColumns: List<List<String>>) {
    withContext(Dispatchers.Default) {
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
                // Title
                val titleOffsetY = page.mediaBox.upperRightY - PDF_OFFSET_Y
                with(contentStream) {
                    setNonStrokingColor(Color.BLACK)
                    beginText()
                    newLineAtOffset(PDF_OFFSET_X, titleOffsetY)
                    setFont(HELVETICA_BOLD, PDF_TITLE_FONT_SIZE)
                    showText(title)
                    endText()
                }

                // Table
                val tableOffsetY = titleOffsetY - PDF_TITLE_FONT_SIZE
                TableDrawer.builder()
                    .contentStream(contentStream)
                    .startX(PDF_OFFSET_X)
                    .startY(tableOffsetY)
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
}