package com.flxrs.medplan.common.pdf

object PdfCreator {
    suspend fun createPdfWithTable(rowsAndColumns: List<List<String>>) = createPdfWithTablePlatform(rowsAndColumns)
}

internal expect suspend fun createPdfWithTablePlatform(rowsAndColumns: List<List<String>>)