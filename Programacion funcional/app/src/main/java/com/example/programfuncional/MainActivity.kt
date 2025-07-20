package com.example.programfuncional

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.programfuncional.Data.local.MyApp
import com.example.programfuncional.Data.model.RutaAprendizaje
import com.example.programfuncional.Presentation.ViewModel.ProgresoViewModel
import com.example.programfuncional.Presentation.ViewModel.RutaViewModel
import com.example.programfuncional.Presentation.ViewModel.TemaViewModel
import com.example.programfuncional.Presentation.ViewModel.TemaViewModelFactory
import com.example.programfuncional.ui.theme.ProgramFuncionalTheme
import kotlinx.coroutines.launch
import com.example.programfuncional.Navigation.NavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as MyApp

        // Crear los ViewModels (sin Hilt por ahora)
        val temaViewModel = ViewModelProvider(this, TemaViewModelFactory(app.temaRepository))[TemaViewModel::class.java]
        val rutaViewModel = RutaViewModel(app.temaRepository)
        val progresoViewModel = ProgresoViewModel(app.progresoRepository)

        // Cargar rutas de ejemplo si no existen (solo la primera vez)
        lifecycleScope.launch {
            app.temaRepository.insertarRutas(
                listOf(
                    RutaAprendizaje(rutaId = 1, nombre = "Teoría"),
                    RutaAprendizaje(rutaId = 2, nombre = "Ejercicios"),
                    RutaAprendizaje(rutaId = 3, nombre = "Quizzes")
                )
            )
        }

        setContent {
            ProgramFuncionalTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    temaViewModel = temaViewModel,
                    rutaViewModel = rutaViewModel,
                    progresoViewModel = progresoViewModel
                )
            }
        }
    }
}
