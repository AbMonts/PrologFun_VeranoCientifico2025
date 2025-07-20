package com.example.programfuncional.Data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Tema(
    @PrimaryKey val temaId: Int,
    val rutaId: Int,
    val nombre: String,
    val informacion: String, // texto largo separado por \n
    val ejemplos: String
)

