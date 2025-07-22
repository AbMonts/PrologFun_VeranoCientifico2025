package com.example.programfuncional.Domain.repository

import com.example.programfuncional.Data.local.ProgresoDAO
import com.example.programfuncional.Data.local.RutaDAO
import com.example.programfuncional.Data.local.TemaDAO
import com.example.programfuncional.Data.model.Progreso
import com.example.programfuncional.Data.model.RutaAprendizaje
import com.example.programfuncional.Data.model.Tema
import com.example.programfuncional.Data.model.TemaProgreso
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TemaRepository(
    private val temaDao: TemaDAO,
    private val rutaDao: RutaDAO,
    private val progresoDao: ProgresoDAO
)  {

    fun getTemasByRuta(rutaId: Int): Flow<List<Tema>> {
        return temaDao.getTemasByRuta(rutaId)
    }

    suspend fun getTemaPorId(temaId: Int): Tema {
        return temaDao.getTema(temaId)
    }

    fun getRutas(): Flow<List<RutaAprendizaje>> {
        return rutaDao.getAllRutas()
    }

    suspend fun insertarTemas(temas: List<Tema>) {
        temaDao.insertAll(temas)
    }

    suspend fun insertarRutas(rutas: List<RutaAprendizaje>) {
        rutaDao.insertAll(rutas)
    }

    suspend fun temaCompleto(temaId: Int, puntos: Int) {
        val progresoActual = progresoDao.getProgresoSinFlow(temaId)
        if (progresoActual != null) {
            progresoDao.insertProgreso(
                progresoActual.copy(
                    completado = true,
                    porcentaje = 100f,
                    puntos = puntos // <- siempre actualizar puntos
                )
            )
        } else {
            progresoDao.insertProgreso(
                Progreso(
                    temaId = temaId,
                    completado = true,
                    porcentaje = 100f,
                    puntos = puntos
                )
            )
        }
    }
    
    suspend fun guardarProgresoParcial(temaId: Int, porcentaje: Float) {
        progresoDao.getProgresoSinFlow(temaId)?.let {
            val nuevo = it.copy(
                porcentaje = maxOf(porcentaje, it.porcentaje),
                completado = it.completado || porcentaje >= 100f
            )
            progresoDao.insertProgreso(nuevo)
        } ?: progresoDao.insertProgreso(
            Progreso(
                temaId = temaId,
                porcentaje = porcentaje,
                completado = porcentaje >= 100f,
                puntos = 0
            )
        )
    }

}

class ProgresoRepository(
    private val progresoDao: ProgresoDAO,
    private val temaDao: TemaDAO,
    private val rutaDao: RutaDAO
) {

    suspend fun obtenerPuntosTotales(): Int {
        return progresoDao.obtenerPuntosTotales()
    }

    fun getProgresoRuta(rutaId: Int): Flow<Float> = flow {
        val temas = temaDao.getTemasByRutaSinFlow(rutaId)
        val total = temas.size
        val completados = temas.count {
            progresoDao.getProgresoSinFlow(it.temaId)?.completado == true
        }
        val porcentaje = if (total > 0) (completados.toFloat() / total.toFloat()) else 0f
        emit(porcentaje)
    }

    suspend fun getTemasConProgresoPorRuta(rutaId: Int): List<TemaProgreso> {
        val temas = temaDao.getTemasByRutaSinFlow(rutaId)
        return temas.map { tema ->
            val progreso = progresoDao.getProgresoSinFlow(tema.temaId)
            TemaProgreso(tema, progreso)
        }
    }

}

