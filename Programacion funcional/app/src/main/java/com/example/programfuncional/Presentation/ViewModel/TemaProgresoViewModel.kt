package com.example.programfuncional.Presentation.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.programfuncional.Data.model.RutaAprendizaje
import com.example.programfuncional.Data.model.Tema
import com.example.programfuncional.Data.model.TemaProgreso
import com.example.programfuncional.Domain.repository.ProgresoRepository
import com.example.programfuncional.Domain.repository.TemaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class TemaViewModel(private val repository: TemaRepository) : ViewModel() {

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

    fun cargarTema(temaId: Int) {
        viewModelScope.launch {
            _tema.value = repository.getTemaPorId(temaId)
            _indice.value = 0
        }
    }

    fun siguienteParrafo() {
        if (_indice.value < parrafos.value.size - 1) {
            _indice.value++
            guardarProgreso()

        }
    }

    private fun guardarProgreso() {
        val temaId = _tema.value?.temaId ?: return
        val total = parrafos.value.size
        if (total == 0) return

        val progreso = ((_indice.value + 1).toFloat() / total.toFloat()) * 100f

        viewModelScope.launch {
            repository.guardarProgresoParcial(temaId, progreso)
        }
    }

    fun anteriorParrafo() {
        if (_indice.value > 0) {
            _indice.value--
        }
    }

    fun marcarComoCompletado(progresoViewModel: ProgresoViewModel) {
        val tema = _tema.value ?: return
        val puntos = 10

        viewModelScope.launch {
            repository.guardarProgresoParcial(tema.temaId, 100f)
            repository.temaCompleto(tema.temaId, puntos)
            delay(300)
            progresoViewModel.cargarPuntosTotales()
        }
    }

}

class ProgresoViewModel(private val repository: ProgresoRepository) : ViewModel() {

    private val _puntos = MutableStateFlow(0)
    val puntos: StateFlow<Int> = _puntos

    private val _temasConProgreso = MutableStateFlow<List<TemaProgreso>>(emptyList())
    val temasConProgreso: StateFlow<List<TemaProgreso>> = _temasConProgreso

    fun getProgresoRuta(rutaId: Int): StateFlow<Float> {
        return repository.getProgresoRuta(rutaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)
    }

    fun cargarTemasConProgreso(rutaId: Int) {
        viewModelScope.launch {
            val temasProgreso = repository.getTemasConProgresoPorRuta(rutaId)
            _temasConProgreso.value = temasProgreso
        }
    }

    private val progresoPorRuta = mutableMapOf<Int, StateFlow<Float>>()

    fun getProgresoRutaState(rutaId: Int): StateFlow<Float> {
        return progresoPorRuta.getOrPut(rutaId) {
            repository.getProgresoRuta(rutaId)
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)
        }
    }


    fun cargarPuntosTotales() {
        viewModelScope.launch {
            _puntos.value = repository.obtenerPuntosTotales()
        }
    }

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
