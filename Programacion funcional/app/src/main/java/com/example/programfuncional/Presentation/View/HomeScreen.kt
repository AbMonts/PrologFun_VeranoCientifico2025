package com.example.programfuncional.Presentation.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.programfuncional.Navigation.NavRoutes
import com.example.programfuncional.Presentation.ViewModel.ProgresoViewModel
import com.example.programfuncional.Presentation.ViewModel.RutaViewModel
import com.example.programfuncional.R

@Composable
fun HomeScreen(navController: NavHostController, rutaViewModel: RutaViewModel, progresoViewModel: ProgresoViewModel) {

    val rutas by rutaViewModel.rutas.collectAsState()
    val puntos by progresoViewModel.puntos.collectAsState()


    LaunchedEffect(true) {
        progresoViewModel.cargarPuntosTotales()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(40.dp))


        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = Color(0xFF4CAF50),
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Cuánto has avanzado",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(puntos.toString(), color = Color.Black, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Image(
                            painter = painterResource(id = R.mipmap.cristal),
                            contentDescription = "Cristal",
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = 0.0f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = Color.White,
                    trackColor = Color(0xFFB2DFDB)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("0%", color = Color.Black, style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Caja informativa
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = Color.Black,
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 2.dp
        ) {
            Text(
                text = "Aquí encontrarás las opciones de aprendizaje",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Rutas de aprendizaje",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            rutas.forEach { ruta ->
                val progreso by progresoViewModel.getProgresoRutaState(ruta.rutaId).collectAsState()

                LearningPathCard(
                    title = ruta.nombre,
                    progress = progreso,
                    background = Color(0xFFA0C1A7),
                    onClick = {
                        navController.navigate("${NavRoutes.Conceptos}?rutaId=${ruta.rutaId}")
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(navController = navController, currentRoute = NavRoutes.Home)
    }
}

    @Composable
    fun LearningPathCard(
        title: String,
        progress: Float,
        background: Color = Color.White,
        onClick: () -> Unit = {}
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = background),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = Color(0xFF9575CD),
                    trackColor = Color(0xFFEDE7F6)
                )
                Text("${(progress * 100).toInt()}%", fontSize = 12.sp, modifier = Modifier.align(Alignment.End))
            }
        }
    }


@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = currentRoute == NavRoutes.Home,
            onClick = {
                if (currentRoute != NavRoutes.Home) {
                    navController.navigate(NavRoutes.Home) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Menu, contentDescription = "Contenido") },
            selected = currentRoute.startsWith(NavRoutes.Conceptos), // más robusto si incluye ?rutaId=
            onClick = {
                if (!currentRoute.startsWith(NavRoutes.Conceptos)) {
                    navController.navigate("${NavRoutes.Conceptos}?rutaId=1") { // ← Ruta predeterminada
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }
        )

    }
}
