package com.example.programfuncional.Domain.repository

import com.example.programfuncional.Data.local.ProgresoDAO
import com.example.programfuncional.Data.local.RutaDAO
import com.example.programfuncional.Data.local.TemaDAO
import com.example.programfuncional.Data.model.Progreso
import com.example.programfuncional.Data.model.RutaAprendizaje
import com.example.programfuncional.Data.model.Tema
import kotlinx.coroutines.flow.Flow


class TemaRepository(
    private val temaDao: TemaDAO,
    private val rutaDao: RutaDAO // si decides tenerlo separado
) {
    fun getTemasByRuta(rutaId: Int): Flow<List<Tema>> {
        return temaDao.getTemasByRuta(rutaId)
    }

    suspend fun getTemaPorId(temaId: Int): Tema {
        return temaDao.getTema(temaId)
    }

    suspend fun insertarTemas(temas: List<Tema>) {
        temaDao.insertAll(temas)
    }

    suspend fun insertarTema(tema: Tema) {
        temaDao.insertAll(listOf(tema))
    }

    // Opcional si usas rutas
    suspend fun insertarRutas(rutas: List<RutaAprendizaje>) {
        rutaDao.insertAll(rutas)
    }

    fun getRutas(): Flow<List<RutaAprendizaje>> {
        return rutaDao.getAllRutas()
    }


}


class ProgresoRepository(private val progresoDAO: ProgresoDAO) {
    fun getProgreso(temaId: Int) = progresoDAO.getProgreso(temaId)
    suspend fun temaCompleto(temaId: Int, puntos: Int) {
        val progreso = Progreso(temaId, true, puntos)
        progresoDAO.insertProgreso(progreso)
    }
}
