package com.example.programfuncional.Presentation.View

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
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
    LaunchedEffect(temaId) {
        viewModel.cargarTema(temaId)
    }

    val tema by viewModel.tema.collectAsState()
    val parrafos by viewModel.parrafos.collectAsState()
    val indice by viewModel.indice.collectAsState()

    val clipboardManager = LocalClipboardManager.current
    var copiado by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Barra superior con progreso
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = Color(0xFF4CAF50),
            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
            shadowElevation = 4.dp
        ) {
            val progreso = if (parrafos.isNotEmpty()) (indice + 1).toFloat() / parrafos.size else 0f

            Column(modifier = Modifier.padding(16.dp)) {
                LinearProgressIndicator(
                    progress = progreso,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = Color.White,
                    trackColor = Color(0xFFB2DFDB)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${(progreso * 100).toInt()}%",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Contenido principal
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .weight(1f)
        ) {
            Text(
                text = tema?.nombre ?: "Cargando...",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (parrafos.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF5F5F5),
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = parrafos[indice],
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Ejemplo al final
            tema?.ejemplos?.takeIf { it.isNotBlank() }?.let { ejemploTexto ->
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Ejemplo:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF2E7D32)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFEEEEEE),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = ejemploTexto,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(ejemploTexto))
                        copiado = true
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (copiado) "Copiado" else "Copiar",
                        color = Color.White
                    )
                }
            }
        }

        // Botón inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text(
                    text = if (indice < parrafos.size - 1) "Siguiente" else "Finalizar",
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

