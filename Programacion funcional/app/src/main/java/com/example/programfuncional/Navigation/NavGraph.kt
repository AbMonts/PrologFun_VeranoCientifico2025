package com.example.programfuncional.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.programfuncional.Presentation.View.ConceptosScreen
import com.example.programfuncional.Presentation.View.HomeScreen
import com.example.programfuncional.Presentation.View.TemaScreen
import com.example.programfuncional.Presentation.View.WelcomeScreen
import com.example.programfuncional.Presentation.ViewModel.ProgresoViewModel
import com.example.programfuncional.Presentation.ViewModel.RutaViewModel
import com.example.programfuncional.Presentation.ViewModel.TemaViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
    temaViewModel: TemaViewModel,
    rutaViewModel: RutaViewModel,
    progresoViewModel: ProgresoViewModel
) {
    NavHost(navController = navController, startDestination = NavRoutes.Welcome) {

        composable(NavRoutes.Welcome) {
            WelcomeScreen(
                onStartClick = {
                    navController.navigate(NavRoutes.Home) {
                        popUpTo(NavRoutes.Welcome) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Home) {
            HomeScreen(
                navController = navController,
                rutaViewModel = rutaViewModel
            )
        }

        composable(
            route = NavRoutes.Conceptos + "?rutaId={rutaId}",
            arguments = listOf(navArgument("rutaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val rutaId = backStackEntry.arguments?.getInt("rutaId") ?: 1
            ConceptosScreen(
                navController = navController,
                TemaViewModel = temaViewModel,
                ProgresoViewModel = progresoViewModel,
                rutaId = rutaId
            )

        }

        composable(
            route = NavRoutes.Tema,
            arguments = listOf(navArgument("temaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val temaId = backStackEntry.arguments?.getInt("temaId") ?: return@composable
            TemaScreen(
                viewModel = temaViewModel,
                temaId = temaId,
                onFinalizar = { navController.popBackStack() }
            )
        }
    }
}
