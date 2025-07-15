package com.example

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseFactory {
    fun init() {
        val dbFile = File("delipucp.db")
        val dbUrl = "jdbc:sqlite:${dbFile.absolutePath}"

        Database.connect(dbUrl, driver = "org.sqlite.JDBC")

        transaction {
            println("Creando tablas...")
            SchemaUtils.createMissingTablesAndColumns(
                Usuarios,
                Locales,
                Promociones,
                Menus,
                TiposLocales,
                Local_Tipo,
                Reservas,
                Favoritos,
                Historial
            )
            println("Tablas creadas exitosamente.")
        }
    }
}
