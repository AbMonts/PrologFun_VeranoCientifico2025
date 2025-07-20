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
    fun getTemasByRuta(rutaId: Int): Flow<List<Tema>>

    @Query("SELECT * FROM Tema WHERE temaId = :temaId")
    suspend fun getTema(temaId: Int): Tema

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(temas: List<Tema>)


}

@Dao
interface ProgresoDAO {
    @Query("SELECT * FROM Progreso WHERE temaId = :temaId")
    fun getProgreso(temaId: Int): Flow<Progreso>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgreso(progress: Progreso)

    @Query("""
    SELECT 
        t.rutaId AS rutaId,
        COUNT(p.temaId) AS temasCompletados,
        COUNT(t.temaId) AS totalTemas,
        SUM(p.puntos) AS totalPuntos
    FROM Tema t
    LEFT JOIN Progreso p ON t.temaId = p.temaId AND p.completado = 1
    WHERE t.rutaId = :rutaId
""")
    fun getProgresoPorRuta(rutaId: Int): Flow<ProgresoRuta>



}


@Dao
interface RutaDAO {
    @Query("SELECT * FROM RutaAprendizaje")
    fun getAllRutas(): Flow<List<RutaAprendizaje>>

    @Query("SELECT * FROM RutaAprendizaje WHERE rutaId = :rutaId")
    suspend fun getRuta(rutaId: Int): RutaAprendizaje

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rutas: List<RutaAprendizaje>)
}
