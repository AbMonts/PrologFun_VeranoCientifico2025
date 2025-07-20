package com.example.programfuncional.Presentation.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.programfuncional.Navigation.NavRoutes
import com.example.programfuncional.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.programfuncional.Presentation.ViewModel.ProgresoViewModel
import com.example.programfuncional.Presentation.ViewModel.TemaViewModel

@Composable
fun ConceptosScreen(
    navController: NavController,
    TemaViewModel: TemaViewModel,
    ProgresoViewModel: ProgresoViewModel,
    rutaId: Int
) {
    // Obtener lista de temas por ruta desde el ViewModel
    LaunchedEffect(rutaId) {
        TemaViewModel.cargarTemasPorRuta(rutaId)
    }
    val temas by TemaViewModel.temasPorRuta.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp)
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Progreso con cristal (puedes conectar con progreso real más adelante)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4CAF50))
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Tu progreso en conceptos",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("10", color = Color.Black, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.mipmap.cristal),
                        contentDescription = "Cristal",
                        modifier = Modifier.height(24.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(top = 36.dp)) {
                LinearProgressIndicator(
                    progress = 0.0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color.White,
                    trackColor = Color(0xFFB2DFDB)
                )
                Text("0%", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Text(
                text = "Este apartado de teoría te proporciona una base sólida para entender los fundamentos, facilita la resolución de problemas y permite predecir y comprender mejor diversos fenómenos",
                color = Color.White,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Orden de aprendizaje", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            items(temas, key = { it.temaId }) { tema ->
                ConceptCardConProgreso(
                    title = tema.nombre,
                    progress = 0f,
                    background = Color(0xFFA0C1A7),
                    onClick = {
                        navController.navigate(NavRoutes.tema(tema.temaId))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }



        BottomNavigationBar(
            navController = navController,
            currentRoute = NavRoutes.Conceptos
        )
    }
}


@Composable
fun ConceptCardConProgreso(
    title: String,
    progress: Float,
    background: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = Color(0xFF607D8B),
                trackColor = Color(0xFFECEFF1)
            )
            Text(
                "${(progress * 100).toInt()}%",
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}



