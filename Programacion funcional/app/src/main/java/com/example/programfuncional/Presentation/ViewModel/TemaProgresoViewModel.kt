package com.example.programfuncional.Presentation.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.programfuncional.Data.model.Progreso
import com.example.programfuncional.Data.model.RutaAprendizaje
import com.example.programfuncional.Data.model.Tema
import com.example.programfuncional.Domain.repository.ProgresoRepository
import com.example.programfuncional.Domain.repository.TemaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class TemaViewModel(private val repository: TemaRepository) : ViewModel() {


    suspend fun obtenerRutaPorId(rutaId: Int): RutaAprendizaje? {
        return repository.getRutaPorId(rutaId)
    }


    private val _tema = MutableStateFlow<Tema?>(null)
    val tema: StateFlow<Tema?> = _tema

    private val _indice = MutableStateFlow(0)
    val indice: StateFlow<Int> = _indice

    val parrafos: StateFlow<List<String>> = _tema.map { tema ->
        tema?.informacion?.split("\n")?.filter { it.isNotBlank() } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private val _temasPorRuta = MutableStateFlow<List<Tema>>(emptyList())
    val temasPorRuta: StateFlow<List<Tema>> = _temasPorRuta

    fun cargarTemasPorRuta(rutaId: Int) {
        viewModelScope.launch {
            repository.getTemasByRuta(rutaId)
                .distinctUntilChanged()
                .collect {
                    _temasPorRuta.value = it
                }

        }
    }

    fun obtenerTemasPorRuta(rutaId: Int): StateFlow<List<Tema>> {
        return repository.getTemasByRuta(rutaId).distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun cargarTema(temaId: Int) {
        viewModelScope.launch {
            _tema.value = repository.getTemaPorId(temaId)
            _indice.value = 0
        }
    }

    fun siguienteParrafo() {
        if (_indice.value < parrafos.value.size - 1) {
            _indice.value++
        }
    }

    fun anteriorParrafo() {
        if (_indice.value > 0) {
            _indice.value--
        }
    }

    fun marcarComoCompletado() {
        println("Tema finalizado")
    }

    private val _temaActual = MutableStateFlow<Tema?>(null)
    val temaActual: StateFlow<Tema?> = _temaActual

    fun cargarTemaPorId(temaId: Int) {
        viewModelScope.launch {
            _temaActual.value = repository.getTemaPorId(temaId)
        }
    }

    // (Opcional) Obtener rutas disponibles si estás manejando esa tabla
    val rutasDisponibles: StateFlow<List<RutaAprendizaje>> =
        repository.getRutas()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    

}


class TemaViewModelFactory(private val repository: TemaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemaViewModel::class.java)) {
            return TemaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class RutaViewModel(private val repository: TemaRepository) : ViewModel() {

    val rutas: StateFlow<List<RutaAprendizaje>> = repository.getRutas()
        .map { it }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}

class ProgresoViewModel(private val repository: ProgresoRepository) : ViewModel() {
    fun getProgreso(temaId: Int): LiveData<Progreso> =
        repository.getProgreso(temaId).asLiveData()

    fun temaCompleto(temaId: Int, puntos: Int) {
        viewModelScope.launch {
            repository.temaCompleto(temaId, puntos)
        }


    }
    fun getProgresoRuta(rutaId: Int): StateFlow<Float> {
        return repository.getProgresoRuta(rutaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)
    }


}
