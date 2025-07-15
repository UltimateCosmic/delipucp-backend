package com.example

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val usuario: UsuarioResponse? = null
)

@Serializable
data class UsuarioResponse(
    val id: String,
    val nombre: String,
    val correo: String,
    val tipo: String,
    val userName: String,
    val fotoPerfil: String?
)

fun Application.apiModule() {
    routing {
        route("/usuarios") {
            // Endpoint para login
            post("/login") {
                val params = call.receive<Map<String, String>>()
                val correo = params["correo"]
                val contrasena = params["contrasena"]
                val tipo = params["tipo"]
                if (correo == null || contrasena == null || tipo == null) {
                    call.respond(LoginResponse(false, "Faltan parámetros"))
                    return@post
                }
                println("Datos recibidos: correo=$correo, contrasena=$contrasena, tipo=$tipo")
                val usuario = transaction {
                    println("Ejecutando consulta SQL...")
                    Usuarios.select {
                        (Usuarios.correo eq correo) and (Usuarios.contrasena eq contrasena) and (Usuarios.tipo eq tipo)
                    }.singleOrNull().also {
                        println("Resultado de la consulta: $it")
                    }
                }
                if (usuario != null) {
                    val usuarioResponse = UsuarioResponse(
                        id = usuario[Usuarios.id].value.toString(),
                        nombre = usuario[Usuarios.nombre],
                        correo = usuario[Usuarios.correo],
                        tipo = usuario[Usuarios.tipo],
                        userName = usuario[Usuarios.userName],
                        fotoPerfil = usuario[Usuarios.fotoPerfil]
                    )
                    call.respond(LoginResponse(true, "Login exitoso", usuarioResponse))
                } else {
                    call.respond(LoginResponse(false, "Usuario o contraseña incorrectos"))
                }
            }

            get {
                val usuarios = transaction {
                    Usuarios.selectAll().map {
                        mapOf(
                            "id" to it[Usuarios.id].value.toString(),
                            "nombre" to it[Usuarios.nombre],
                            "correo" to it[Usuarios.correo],
                            "tipo" to it[Usuarios.tipo],
                            "userName" to it[Usuarios.userName],
                            "fotoPerfil" to it[Usuarios.fotoPerfil]
                        )
                    }
                }
                call.respond(usuarios)
            }

            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    Usuarios.insert {
                        it[nombre] = params["nombre"] ?: ""
                        it[correo] = params["correo"] ?: ""
                        it[tipo] = params["tipo"] ?: ""
                        it[userName] = params["userName"] ?: ""
                        it[contrasena] = params["contrasena"] ?: ""
                        it[fotoPerfil] = params["fotoPerfil"]
                    }
                }
                call.respond(mapOf("status" to "Usuario creado"))
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val params = call.receive<Map<String, String>>()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@put
                }
                transaction {
                    Usuarios.update({ Usuarios.id eq id }) {
                        it[nombre] = params["nombre"] ?: ""
                        it[correo] = params["correo"] ?: ""
                        it[tipo] = params["tipo"] ?: ""
                        it[userName] = params["userName"] ?: ""
                        it[contrasena] = params["contrasena"] ?: ""
                        it[fotoPerfil] = params["fotoPerfil"]
                    }
                }
                call.respond(mapOf("status" to "Usuario actualizado"))
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@delete
                }
                transaction {
                    Usuarios.deleteWhere { Usuarios.id eq id }
                }
                call.respond(mapOf("status" to "Usuario eliminado"))
            }
        }

        route("/locales") {
            get {
                val locales = transaction {
                    Locales.selectAll().map {
                        mapOf(
                            "id" to it[Locales.id].value.toString(),
                            "nombre" to it[Locales.nombre],
                            "ubicacion" to it[Locales.ubicacion],
                            "horario" to it[Locales.horario],
                            "horarioDespacho" to it[Locales.horarioDespacho],
                            "imagenUrl" to it[Locales.imagenUrl],
                            "abierto" to it[Locales.abierto].toString(),
                            "telefono" to it[Locales.telefono],
                            "metodosPago" to it[Locales.metodosPago]
                        )
                    }
                }
                call.respond(locales)
            }

            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    Locales.insert {
                        it[nombre] = params["nombre"] ?: ""
                        it[ubicacion] = params["ubicacion"] ?: ""
                        it[horario] = params["horario"] ?: ""
                        it[imagenUrl] = params["imagenUrl"] ?: ""
                        it[abierto] = params["abierto"]?.toBoolean() ?: false
                        it[telefono] = params["telefono"]
                        it[metodosPago] = params["metodosPago"] ?: ""
                    }
                }
                call.respond(mapOf("status" to "Local creado"))
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val params = call.receive<Map<String, String>>()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@put
                }
                transaction {
                    Locales.update({ Locales.id eq id }) {
                        it[nombre] = params["nombre"] ?: ""
                        it[ubicacion] = params["ubicacion"] ?: ""
                        it[horario] = params["horario"] ?: ""
                        it[imagenUrl] = params["imagenUrl"] ?: ""
                        it[abierto] = params["abierto"]?.toBoolean() ?: false
                        it[telefono] = params["telefono"]
                        it[metodosPago] = params["metodosPago"] ?: ""
                    }
                }
                call.respond(mapOf("status" to "Local actualizado"))
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@delete
                }
                transaction {
                    Locales.deleteWhere { Locales.id eq id }
                }
                call.respond(mapOf("status" to "Local eliminado"))
            }
        }

        route("/promociones") {
            get {
                val promociones = transaction {
                    Promociones.selectAll().map {
                        mapOf(
                            "id" to it[Promociones.id].value.toString(),
                            "titulo" to it[Promociones.titulo],
                            "descripcion" to it[Promociones.descripcion],
                            "precio" to it[Promociones.precio].toString(), // Convertido a String
                            "colorHex" to it[Promociones.colorHex],
                            "localId" to it[Promociones.local].value.toString()
                        )
                    }
                }
                call.respond(promociones)
            }

            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    Promociones.insert {
                        it[titulo] = params["titulo"] ?: ""
                        it[descripcion] = params["descripcion"] ?: ""
                        it[precio] = params["precio"]?.toBigDecimalOrNull() ?: 0.toBigDecimal()
                        it[colorHex] = params["colorHex"] ?: "#000000"
                        it[local] = params["localId"]?.toInt() ?: 1
                    }
                }
                call.respond(mapOf("status" to "Promoción creada"))
            }
        }

        route("/reservas") {
            get {
                val reservas = transaction {
                    Reservas.selectAll().map {
                        mapOf(
                            "id" to it[Reservas.id].value.toString(),
                            "codigoReserva" to it[Reservas.codigoReserva],
                            "localId" to it[Reservas.local].value.toString(),
                            "usuarioId" to it[Reservas.usuario].value.toString(),
                            "menuId" to it[Reservas.menu]?.value?.toString(),
                            "fecha" to it[Reservas.fecha],
                            "hora" to it[Reservas.hora],
                            "estado" to it[Reservas.estado],
                            "mesa" to it[Reservas.mesa],
                            "metodoPago" to it[Reservas.metodoPago]
                        )
                    }
                }
                call.respond(reservas)
            }

            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    Reservas.insert {
                        it[codigoReserva] = params["codigoReserva"] ?: ""
                        it[local] = params["localId"]?.toInt() ?: 1
                        it[usuario] = params["usuarioId"]?.toInt() ?: 1
                        it[menu] = params["menuId"]?.toIntOrNull()
                        it[fecha] = params["fecha"] ?: ""
                        it[hora] = params["hora"] ?: ""
                        it[estado] = params["estado"] ?: "pendiente"
                        it[mesa] = params["mesa"] ?: ""
                        it[metodoPago] = params["metodoPago"]
                    }
                }
                call.respond(mapOf("status" to "Reserva creada"))
            }
        }

        // --- Menus ---
        route("/menus") {
            get {
                val menus = transaction {
                    Menus.selectAll().map {
                        mapOf(
                            "id" to it[Menus.id].value.toString(),
                            "nombre" to it[Menus.nombre],
                            "tipo" to it[Menus.tipo],
                            "entrada" to it[Menus.entrada],
                            "principal" to it[Menus.principal],
                            "postre" to it[Menus.postre],
                            "bebida" to it[Menus.bebida],
                            "precio" to it[Menus.precio].toString(), // Convertido a String
                            "imagenUrl" to it[Menus.imagenUrl],
                            "fecha" to it[Menus.fecha],
                            "localId" to it[Menus.local].value.toString()
                        )
                    }
                }
                call.respond(menus)
            }
            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    Menus.insert {
                        it[nombre] = params["nombre"] ?: ""
                        it[tipo] = params["tipo"] ?: ""
                        it[entrada] = params["entrada"]
                        it[principal] = params["principal"]
                        it[postre] = params["postre"]
                        it[bebida] = params["bebida"]
                        it[precio] = params["precio"]?.toBigDecimalOrNull() ?: 0.toBigDecimal()
                        it[imagenUrl] = params["imagenUrl"]
                        it[fecha] = params["fecha"] ?: ""
                        it[local] = params["localId"]?.toInt() ?: 1
                    }
                }
                call.respond(mapOf("status" to "Menú creado"))
            }
            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val params = call.receive<Map<String, String>>()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@put
                }
                transaction {
                    Menus.update({ Menus.id eq id }) {
                        it[nombre] = params["nombre"] ?: ""
                        it[tipo] = params["tipo"] ?: ""
                        it[entrada] = params["entrada"]
                        it[principal] = params["principal"]
                        it[postre] = params["postre"]
                        it[bebida] = params["bebida"]
                        it[precio] = params["precio"]?.toBigDecimalOrNull() ?: 0.toBigDecimal()
                        it[imagenUrl] = params["imagenUrl"]
                        it[fecha] = params["fecha"] ?: ""
                        it[local] = params["localId"]?.toInt() ?: 1
                    }
                }
                call.respond(mapOf("status" to "Menú actualizado"))
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@delete
                }
                transaction {
                    Menus.deleteWhere { Menus.id eq id }
                }
                call.respond(mapOf("status" to "Menú eliminado"))
            }
        }

        // --- TiposLocales ---
        route("/tiposlocales") {
            get {
                val tipos = transaction {
                    TiposLocales.selectAll().map {
                        mapOf(
                            "id" to it[TiposLocales.id].value.toString(),
                            "nombre" to it[TiposLocales.nombre]
                        )
                    }
                }
                call.respond(tipos)
            }
            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    TiposLocales.insert {
                        it[nombre] = params["nombre"] ?: ""
                    }
                }
                call.respond(mapOf("status" to "Tipo de local creado"))
            }
            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val params = call.receive<Map<String, String>>()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@put
                }
                transaction {
                    TiposLocales.update({ TiposLocales.id eq id }) {
                        it[nombre] = params["nombre"] ?: ""
                    }
                }
                call.respond(mapOf("status" to "Tipo de local actualizado"))
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@delete
                }
                transaction {
                    TiposLocales.deleteWhere { TiposLocales.id eq id }
                }
                call.respond(mapOf("status" to "Tipo de local eliminado"))
            }
        }

        // --- Local_Tipo ---
        route("/local_tipo") {
            get {
                val localTipos = transaction {
                    Local_Tipo.selectAll().map {
                        mapOf(
                            "id" to it[Local_Tipo.id].value.toString(),
                            "localId" to it[Local_Tipo.local].value.toString(),
                            "tipoId" to it[Local_Tipo.tipo].value.toString()
                        )
                    }
                }
                call.respond(localTipos)
            }
            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    Local_Tipo.insert {
                        it[local] = params["localId"]?.toInt() ?: 1
                        it[tipo] = params["tipoId"]?.toInt() ?: 1
                    }
                }
                call.respond(mapOf("status" to "Relación local-tipo creada"))
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@delete
                }
                transaction {
                    Local_Tipo.deleteWhere { Local_Tipo.id eq id }
                }
                call.respond(mapOf("status" to "Relación local-tipo eliminada"))
            }
        }

        // --- Favoritos ---
        route("/favoritos") {
            get {
                val favoritos = transaction {
                    Favoritos.selectAll().map {
                        mapOf(
                            "id" to it[Favoritos.id].value.toString(),
                            "usuarioId" to it[Favoritos.usuario].value.toString(),
                            "localId" to it[Favoritos.local].value.toString()
                        )
                    }
                }
                call.respond(favoritos)
            }
            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    Favoritos.insert {
                        it[usuario] = params["usuarioId"]?.toInt() ?: 1
                        it[local] = params["localId"]?.toInt() ?: 1
                    }
                }
                call.respond(mapOf("status" to "Favorito agregado"))
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@delete
                }
                transaction {
                    Favoritos.deleteWhere { Favoritos.id eq id }
                }
                call.respond(mapOf("status" to "Favorito eliminado"))
            }
        }

        // --- Historial ---
        route("/historial") {
            get {
                val historial = transaction {
                    Historial.selectAll().map {
                        mapOf(
                            "id" to it[Historial.id].value.toString(),
                            "usuarioId" to it[Historial.usuario].value.toString(),
                            "localId" to it[Historial.local].value.toString(),
                            "fecha" to it[Historial.fecha],
                            "detalle" to it[Historial.detalle]
                        )
                    }
                }
                call.respond(historial)
            }
            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    Historial.insert {
                        it[usuario] = params["usuarioId"]?.toInt() ?: 1
                        it[local] = params["localId"]?.toInt() ?: 1
                        it[fecha] = params["fecha"] ?: ""
                        it[detalle] = params["detalle"] ?: ""
                    }
                }
                call.respond(mapOf("status" to "Historial agregado"))
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@delete
                }
                transaction {
                    Historial.deleteWhere { Historial.id eq id }
                }
                call.respond(mapOf("status" to "Historial eliminado"))
            }
        }
    }
}
