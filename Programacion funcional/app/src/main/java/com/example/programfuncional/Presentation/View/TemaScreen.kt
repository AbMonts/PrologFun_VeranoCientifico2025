package com.example.programfuncional.Presentation.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.programfuncional.Presentation.ViewModel.TemaViewModel

@Composable
fun TemaScreen(
    viewModel: TemaViewModel,
    temaId: Int,
    onFinalizar: () -> Unit
) {


    // Cargar el tema una sola vez
    LaunchedEffect(temaId) {
        viewModel.cargarTema(temaId)
    }

    val tema by viewModel.tema.collectAsState()
    val parrafos by viewModel.parrafos.collectAsState()
    val indice by viewModel.indice.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Barra superior con progreso
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4CAF50))
                .padding(12.dp)
        ) {
            val progreso = if (parrafos.isNotEmpty()) (indice + 1).toFloat() / parrafos.size else 0f
            Column(modifier = Modifier.padding(top = 36.dp)) {
                LinearProgressIndicator(
                    progress = progreso,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color.White,
                    trackColor = Color(0xFFB2DFDB)
                )
                Text("${(progreso * 100).toInt()}%", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Contenido
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = tema?.nombre ?: "Cargando...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (parrafos.isNotEmpty()) {
                Text(
                    text = parrafos[indice],
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Button(
                onClick = {
                    if (indice < parrafos.size - 1) {
                        viewModel.siguienteParrafo()
                    } else {
                        viewModel.marcarComoCompletado()
                        onFinalizar()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (indice < parrafos.size - 1) "Siguiente" else "Finalizar",
                    color = Color.White
                )
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

