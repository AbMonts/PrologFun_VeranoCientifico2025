package com.example.programfuncional.Data.local


import android.app.Application
import com.example.programfuncional.Domain.repository.ProgresoRepository
import com.example.programfuncional.Domain.repository.TemaRepository

class MyApp : Application() {


    // Base de datos y repositorio disponibles globalmente
    lateinit var database: AppDatabase
        private set

    lateinit var temaRepository: TemaRepository
        private set
    lateinit var progresoRepository: ProgresoRepository
        private set

    override fun onCreate() {
        super.onCreate()
        // ✅ Inicializar primero la base de datos
        database = AppDatabase.getDatabase(this)

        // ✅ Luego ya puedes usar la base de datos
        progresoRepository = ProgresoRepository(database.progresoDao())
        temaRepository = TemaRepository(database.temaDao(), database.rutaDao())
    }
}
