package com.example

import org.jetbrains.exposed.dao.id.IntIdTable

object Usuarios : IntIdTable() {
    val nombre = varchar("nombre", 50)
    val correo = varchar("correo", 50).uniqueIndex()
    val tipo = varchar("tipo", 20)
    val userName = varchar("userName", 30).uniqueIndex()
    val contrasena = varchar("contrasena", 50)
    val fotoPerfil = varchar("fotoPerfil", 100).nullable()
}

object Locales : IntIdTable() {
    val nombre = varchar("nombre", 50)
    val ubicacion = varchar("ubicacion", 100)
    val horario = varchar("horario", 50)
    val horarioDespacho = varchar("horarioDespacho", 50).nullable() // Agregado
    val imagenUrl = varchar("imagenUrl", 100)
    val abierto = bool("abierto")
    val telefono = varchar("telefono", 15).nullable()
    val metodosPago = varchar("metodosPago", 100)
}

object Promociones : IntIdTable() {
    val titulo = varchar("titulo", 50)
    val descripcion = varchar("descripcion", 200)
    val precio = decimal("precio", 10, 2)
    val colorHex = varchar("colorHex", 7)
    val local = reference("localId", Locales)
}

object Reservas : IntIdTable() {
    val codigoReserva = varchar("codigoReserva", 20).uniqueIndex() // Nuevo
    val local = reference("localId", Locales)
    val usuario = reference("usuarioId", Usuarios)
    val menu = reference("menuId", Menus).nullable() // Nuevo
    val fecha = varchar("fecha", 20)
    val hora = varchar("hora", 20)
    val estado = varchar("estado", 20)
    val mesa = varchar("mesa", 20)
    val metodoPago = varchar("metodoPago", 30).nullable() // Nuevo
}

object Favoritos : IntIdTable() {
    val usuario = reference("usuarioId", Usuarios)
    val local = reference("localId", Locales)
}

object Historial : IntIdTable() {
    val usuario = reference("usuarioId", Usuarios)
    val local = reference("localId", Locales)
    val fecha = varchar("fecha", 20)
    val detalle = varchar("detalle", 200)
}

object Menus : IntIdTable() {
    val nombre = varchar("nombre", 50)
    val tipo = varchar("tipo", 20)
    val entrada = varchar("entrada", 100).nullable()
    val principal = varchar("principal", 100).nullable()
    val postre = varchar("postre", 100).nullable()
    val bebida = varchar("bebida", 100).nullable()
    val precio = decimal("precio", 10, 2)
    val imagenUrl = varchar("imagenUrl", 100).nullable()
    val fecha = varchar("fecha", 20) // Cambiado de date("fecha") a varchar
    val local = reference("localId", Locales)
}

object TiposLocales : IntIdTable() {
    val nombre = varchar("nombre", 30)
}

object Local_Tipo : IntIdTable() {
    val local = reference("localId", Locales)
    val tipo = reference("tipoId", TiposLocales)
    init {
        uniqueIndex(local, tipo)
    }
}
