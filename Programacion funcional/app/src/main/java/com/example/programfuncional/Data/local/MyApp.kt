package com.example.programfuncional.Data.local

import android.app.Application
import com.example.programfuncional.Domain.repository.ProgresoRepository
import com.example.programfuncional.Domain.repository.TemaRepository

class MyApp : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var temaRepository: TemaRepository
        private set

    lateinit var progresoRepository: ProgresoRepository
        private set

    override fun onCreate() {
        super.onCreate()

        database = AppDatabase.getDatabase(this)


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
