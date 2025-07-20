package com.example.programfuncional.Navigation

object NavRoutes {
    const val Welcome = "welcome"
    const val Home = "home"
    const val Conceptos = "conceptos"
    const val Teoria = "teoria"

    // Ruta base
    const val TemaBase = "tema"

    // Ruta con parámetro
    const val Tema = "tema/{temaId}"

    // Función para generar ruta con id
    fun tema(temaId: Int) = "tema/$temaId"
}