package com.resuadam2.listacompraexamen.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.resuadam2.listacompraexamen.ui.screens.DetalleScreen
import com.resuadam2.listacompraexamen.ui.screens.HomeScreen
import com.resuadam2.listacompraexamen.ui.screens.ListaCompraScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.HOME.name) {
        composable(AppScreens.HOME.name) {
            HomeScreen {
                navController.navigate(AppScreens.LISTA_COMPRA.name)
            }
        }
        composable(AppScreens.LISTA_COMPRA.name) {
            ListaCompraScreen(
                navigateBack = { navController.popBackStack() },
                onDetalleCompra = { name, price ->
                    navController.navigate("${AppScreens.DETALLE_COMPRA.name}/$name-$price")
                }
            )
        }
        composable(AppScreens.DETALLE_COMPRA.name + "/{name}-{price}", arguments = listOf(
            navArgument("name") { type = NavType.StringType },
            navArgument("price") { type = NavType.FloatType }
        )) {
           DetalleScreen(
                producto = it.arguments?.getString("name") ?: "",
                precio = it.arguments?.getFloat("price").toString(),
                navigateBack = { navController.popBackStack() }
           )
        }
    }
}