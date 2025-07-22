package com.example.programfuncional.Data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.programfuncional.Data.model.Progreso
import com.example.programfuncional.Data.model.ProgresoRuta
import com.example.programfuncional.Data.model.RutaAprendizaje
import com.example.programfuncional.Data.model.Tema
import kotlinx.coroutines.flow.Flow

@Dao
interface TemaDAO {
    @Query("SELECT * FROM Tema WHERE rutaId = :rutaId")
    fun getTemasByRuta(rutaId: Int): Flow<List<Tema>> //los temas de una ruta

    @Query("SELECT * FROM Tema WHERE rutaId = :rutaId")
    suspend fun getTemasByRutaSinFlow(rutaId: Int): List<Tema>

    @Query("SELECT * FROM Tema WHERE temaId = :temaId")
    suspend fun getTemasByRutaSinFlowByTemaId(temaId: Int): List<Tema>

    @Query("SELECT * FROM Tema WHERE temaId = :temaId")
    suspend fun getTema(temaId: Int): Tema //por id

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(temas: List<Tema>)

    @Query("SELECT rutaId FROM Tema WHERE temaId = :temaId LIMIT 1")
    suspend fun getRutaIdByTemaId(temaId: Int): Int?



}

@Dao
interface ProgresoDAO {
    @Query("SELECT * FROM Progreso WHERE temaId = :temaId")
    fun getProgreso(temaId: Int): Flow<Progreso>

    @Query("SELECT COALESCE(SUM(puntos), 0) FROM progreso")
    suspend fun obtenerPuntosTotales(): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgreso(progress: Progreso)

    @Query("""
    SELECT 
        t.rutaId AS rutaId,
        COUNT(p.temaId) AS temasCompletados,
        COUNT(t.temaId) AS totalTemas,
        COALESCE(SUM(p.puntos), 0) AS totalPuntos
    FROM Tema t
    LEFT JOIN Progreso p ON t.temaId = p.temaId AND p.completado = 1
    WHERE t.rutaId = :rutaId
""") //de los temas
    fun getProgresoPorRuta(rutaId: Int): Flow<ProgresoRuta>

    @Query("SELECT * FROM Progreso WHERE temaId = :temaId LIMIT 1")
    suspend fun getProgresoSinFlow(temaId: Int): Progreso?

}


@Dao
interface RutaDAO {
    @Query("SELECT * FROM RutaAprendizaje") //obtiene todas las rutas
    fun getAllRutas(): Flow<List<RutaAprendizaje>>

    @Query("SELECT * FROM RutaAprendizaje WHERE rutaId = :rutaId") //id de una ruta con id
    suspend fun getRuta(rutaId: Int): RutaAprendizaje

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rutas: List<RutaAprendizaje>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRuta(ruta: RutaAprendizaje)

}
