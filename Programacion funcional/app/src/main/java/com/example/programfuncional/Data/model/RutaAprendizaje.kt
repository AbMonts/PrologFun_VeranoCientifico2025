package com.example.programfuncional.Data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class RutaAprendizaje(
    @PrimaryKey val rutaId: Int,
    val nombre: String // "Teoría", "Ejercicios", "Quizzes"
)
