package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.module() {
    // Inicializa la base de datos
    DatabaseFactory.init()

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Servidor y base de datos listos 🚀")
        }
        static("/images"){
            resources("static/images")
        }
    }

    // Incluye el módulo de API REST
    apiModule()
}
