package com.example.programfuncional.Data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class RutaAprendizaje(
    @PrimaryKey val rutaId: Int, //relacion con tema
    val nombre: String, // "Teoría", "Ejercicios", "Quizzes"
    val porcentaje: Float = 0f,
    val totTemas: Int
)
