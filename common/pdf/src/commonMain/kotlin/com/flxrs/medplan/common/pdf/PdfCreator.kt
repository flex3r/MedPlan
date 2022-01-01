package com.flxrs.medplan.common.pdf

object PdfCreator {
    suspend fun createPdfWithTable(title: String, rowsAndColumns: List<List<String>>) =
        createPdfWithTablePlatform(title, rowsAndColumns)

    internal const val PDF_OFFSET_X = 20f
    internal const val PDF_OFFSET_Y = 30f
    internal const val PDF_TITLE_FONT_SIZE = 18f
}

internal expect suspend fun createPdfWithTablePlatform(title: String, rowsAndColumns: List<List<String>>)