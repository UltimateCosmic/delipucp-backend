package com.example

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.math.BigDecimal
import kotlin.text.get
import kotlin.text.insert
import kotlin.text.set


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

            put("/cambiarContrasena/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val params = call.receive<Map<String, String>>()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@put
                }
                val contrasenaActual = params["contrasenaActual"]
                val nuevaContrasena = params["nuevaContrasena"]
                if (contrasenaActual.isNullOrEmpty() || nuevaContrasena.isNullOrEmpty()) {
                    call.respond(mapOf("status" to "Contraseña inválida"))
                    return@put
                }
                val usuario = transaction {
                    Usuarios.select { (Usuarios.id eq id) and (Usuarios.contrasena eq contrasenaActual) }
                        .singleOrNull()
                }
                if (usuario == null) {
                    call.respond(mapOf("status" to "Contraseña actual incorrecta"))
                    return@put
                }
                transaction {
                    Usuarios.update({ Usuarios.id eq id }) {
                        it[contrasena] = nuevaContrasena
                    }
                }
                call.respond(mapOf("status" to "Contraseña actualizada"))
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
                            "precio" to it[Promociones.precio],
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
                            "localId" to it[Reservas.local].value.toString(),
                            "usuarioId" to it[Reservas.usuario].value.toString(),
                            "fecha" to it[Reservas.fecha],
                            "hora" to it[Reservas.hora],
                            "platilloId" to it[Reservas.platillo].value.toString(),
                            "cantidad" to it[Reservas.cantidad].toString(),
                            "estado" to it[Reservas.estado],

                            )
                    }
                }
                call.respond(reservas)
            }

            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    Reservas.insert {
                        it[local] = params["localId"]?.toInt() ?: 1
                        it[usuario] = params["usuarioId"]?.toInt() ?: 1
                        it[fecha] = params["fecha"] ?: ""
                        it[hora] = params["hora"] ?: ""
                        it[platillo] = params["platilloId"]?.toInt() ?: 1
                        it[cantidad] = params["cantidad"]?.toIntOrNull() ?: 1
                        it[estado] = params["estado"] ?: "pendiente"
                    }
                }
                call.respond(mapOf("status" to "Reserva creada"))
            }
        }

        route("/platillos") {
            get {
                val platillos = transaction {
                    Platillos.selectAll().map {
                        mapOf(
                            "id" to it[Platillos.id].toString(),
                            "nombre" to it[Platillos.nombre],
                            "descripcion" to it[Platillos.descripcion],
                            "precio" to it[Platillos.precio].toString(),
                            "localId" to it[Platillos.localId].toString()
                        )
                    }
                }
                call.respond(platillos)
            }

            post {
                val params = call.receive<Map<String, String>>()
                transaction {
                    Reservas.insert {
                        it[local] = params["localId"]?.toInt() ?: 1
                        it[usuario] = params["usuarioId"]?.toInt() ?: 1
                        it[fecha] = params["fecha"] ?: ""
                        it[hora] = params["hora"] ?: ""
                        it[platillo] = params["platilloId"]?.toInt() ?: 1
                        it[cantidad] = params["cantidad"]?.toIntOrNull() ?: 1
                        it[estado] = params["estado"] ?: "pendiente"
                    }
                }
                call.respond(mapOf("status" to "Reserva creada"))
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val params = call.receive<Map<String, String>>()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@put
                }
                transaction {
                    Platillos.update({ Platillos.id eq id }) {
                        it[nombre] = params["nombre"] ?: ""
                        it[descripcion] = params["descripcion"] ?: ""
                        it[precio] = params["precio"]?.toBigDecimalOrNull() ?: 0.toBigDecimal()
                        it[localId] = params["localId"]?.toIntOrNull() ?: 0
                    }
                }
                call.respond(mapOf("status" to "Platillo actualizado"))
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(mapOf("status" to "ID inválido"))
                    return@delete
                }
                transaction {
                    Platillos.deleteWhere { Platillos.id eq id }
                }
                call.respond(mapOf("status" to "Platillo eliminado"))
            }


        }
        route("/localesConPlatillo") {
            get {
                val localesConPlatillo = transaction {
                    (Locales leftJoin Platillos).slice(
                        Locales.id, Locales.nombre, Locales.ubicacion, Locales.horario,
                        Locales.imagenUrl, Locales.abierto, Locales.telefono, Locales.metodosPago,
                        Platillos.nombre
                    ).select { Locales.abierto eq true }.map { row ->
                        val platillo = row[Platillos.nombre] ?: ""
                        mapOf(
                            "id" to row[Locales.id].value.toString(),
                            "nombre" to row[Locales.nombre],
                            "ubicacion" to row[Locales.ubicacion],
                            "horario" to row[Locales.horario],
                            "imagenUrl" to row[Locales.imagenUrl],
                            "abierto" to row[Locales.abierto].toString(),
                            "telefono" to row[Locales.telefono],
                            "metodosPago" to row[Locales.metodosPago],
                            "platillo" to platillo
                        )
                    }
                }
                call.respond(localesConPlatillo)
            }
        }
    }
}
