package com.example.programfuncional.Data.local

import android.app.Application
import com.example.programfuncional.Domain.repository.ProgresoRepository
import com.example.programfuncional.Domain.repository.TemaRepository

class MyApp : Application() {

    // Base de datos y repositorios disponibles globalmente
    lateinit var database: AppDatabase
        private set

    lateinit var temaRepository: TemaRepository
        private set

    lateinit var progresoRepository: ProgresoRepository
        private set

    override fun onCreate() {
        super.onCreate()

        // Inicializar la base de datos
        database = AppDatabase.getDatabase(this)

        // Inicializar los repositorios pasando todos los DAOs necesarios
        temaRepository = TemaRepository(
            temaDao = database.temaDao(),
            rutaDao = database.rutaDao(),
            progresoDao = database.progresoDao()
        )

        progresoRepository = ProgresoRepository(
            progresoDao = database.progresoDao(),
            temaDao = database.temaDao(),
            rutaDao = database.rutaDao()
        )
    }
}
