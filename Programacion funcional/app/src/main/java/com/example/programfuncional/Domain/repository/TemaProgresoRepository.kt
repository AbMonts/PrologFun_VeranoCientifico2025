package com.example.programfuncional.Domain.repository

import com.example.programfuncional.Data.local.ProgresoDAO
import com.example.programfuncional.Data.local.RutaDAO
import com.example.programfuncional.Data.local.TemaDAO
import com.example.programfuncional.Data.model.Progreso
import com.example.programfuncional.Data.model.RutaAprendizaje
import com.example.programfuncional.Data.model.Tema
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TemaRepository(
    private val temaDao: TemaDAO,
    private val rutaDao: RutaDAO,
    private val progresoDao: ProgresoDAO
) {

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

    suspend fun insertarTema(tema: Tema) {
        temaDao.insertAll(listOf(tema))
    }

    suspend fun insertarRutas(rutas: List<RutaAprendizaje>) {
        rutaDao.insertAll(rutas)
    }

    suspend fun getRutaPorId(rutaId: Int): RutaAprendizaje {
        return rutaDao.getRuta(rutaId)
    }

    fun getProgreso(temaId: Int): Flow<Progreso> {
        return progresoDao.getProgreso(temaId)
    }

}



class ProgresoRepository(
    private val progresoDao: ProgresoDAO,
    private val temaDao: TemaDAO,
    private val rutaDao: RutaDAO
) {

    fun getProgreso(temaId: Int): Flow<Progreso> {
        return progresoDao.getProgreso(temaId)
    }

    suspend fun temaCompleto(temaId: Int, puntos: Int) {
        val progreso = Progreso(
            temaId = temaId,
            completado = true,
            puntos = puntos,
            porcentaje = 100f
        )
        progresoDao.insertProgreso(progreso)

        // Obtener la ruta asociada al tema
        val temas = temaDao.getTemasByRutaSinFlowByTemaId(temaId)
        val rutaId = temas.firstOrNull()?.rutaId ?: return

        actualizarPorcentajeRuta(rutaId)
    }

    private suspend fun actualizarPorcentajeRuta(rutaId: Int) {
        val temas = temaDao.getTemasByRutaSinFlow(rutaId)
        val totalTemas = temas.size

        val completados = temas.count { tema ->
            val progreso = progresoDao.getProgresoSinFlow(tema.temaId)
            progreso?.completado == true
        }

        val porcentaje = if (totalTemas > 0) (completados * 100f / totalTemas) else 0f

        // Obtener nombre existente
        val rutaExistente = rutaDao.getRuta(rutaId)
        val nuevaRuta = rutaExistente.copy(
            porcentaje = porcentaje,
            totTemas = totalTemas
        )

        rutaDao.insertRuta(nuevaRuta)
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

}

