package com.example.programfuncional.Presentation.View

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.programfuncional.Data.local.AppDatabase
import com.example.programfuncional.R

@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    val context = LocalContext.current

    // Verificar y mostrar Toast solo una vez al entrar a la pantalla
    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        val dbName = db.openHelper.databaseName
        val mensaje = if (db != null) {
            "Base de datos creada: ${dbName ?: "Nombre no disponible"}"
        } else {
            "No se ha creado la base de datos"
        }

        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo + Título
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFA0C1A7)) // Verde pastel
                    .padding(8.dp)
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.bookmark),
                    contentDescription = "Logo",
                    tint = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("EasyLearn", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }

        // Mensaje de bienvenida
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Bienvenido", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "En esta aplicación te enseñará aspectos sobre la programación funcional, lo que necesitas saber para programar",
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }

        // Botón "Empezar"
        Button(
            onClick = onStartClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Empezar", color = Color.White, fontWeight = FontWeight.Bold)
        }

        // Footer
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "By clicking continue, you agree to our ",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Row {
                Text(
                    "Terms of Service",
                    fontSize = 12.sp,
                    color = Color.Blue,
                    modifier = Modifier.clickable { /* Navigate to TOS */ }
                )
                Text(" and ", fontSize = 12.sp, color = Color.Gray)
                Text(
                    "Privacy Policy",
                    fontSize = 12.sp,
                    color = Color.Blue,
                    modifier = Modifier.clickable { /* Navigate to Privacy Policy */ }
                )
            }
        }
    }
}
