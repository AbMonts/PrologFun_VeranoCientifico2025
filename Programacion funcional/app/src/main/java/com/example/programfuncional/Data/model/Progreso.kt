package com.example.programfuncional.Data.model

import androidx.room.Entity


@Entity(primaryKeys = ["temaId"])
data class Progreso(
    val temaId: Int,
    val completado: Boolean = false,
    val puntos: Int = 0,
    val porcentaje: Float = 0f,

)

data class ProgresoRuta(
    val rutaId: Int,
    val temasCompletados: Int,
    val totalTemas: Int,
    val totalPuntos: Int?
    //falta variable de porcentaje en total de dicha ruta
)

