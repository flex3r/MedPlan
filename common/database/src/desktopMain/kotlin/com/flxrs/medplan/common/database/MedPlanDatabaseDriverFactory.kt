package com.flxrs.medplan.common.database

import com.flxrs.medplan.database.MedPlanDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

@Suppress("FunctionName") // FactoryFunction
fun MedPlanDatabaseDriver(): SqlDriver {
    // TODO move to roaming?
    val databasePath = File(System.getProperty("java.io.tmpdir"), "ComposeTodoDatabase.db")
    val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}")
    MedPlanDatabase.Schema.create(driver)

    return driver
}