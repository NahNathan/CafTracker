package com.nathanrds.caftracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nathanrds.caftracker.di.AppContainer
import com.nathanrds.caftracker.presentation.screen.AddEditProductScreen
import com.nathanrds.caftracker.presentation.screen.AddIntakeScreen
import com.nathanrds.caftracker.presentation.screen.HomeScreen
import com.nathanrds.caftracker.presentation.screen.ProductsScreen
import com.nathanrds.caftracker.presentation.viewmodel.AddEditProductViewModelFactory
import com.nathanrds.caftracker.presentation.viewmodel.ViewModelFactory

@Composable
fun NavGraph(container: AppContainer) {
    val navController = rememberNavController()
    val viewModelFactory = ViewModelFactory(container)

    NavHost(
        navController = navController,
        startDestination = Route.HOME
    ) {
        composable(Route.HOME) {
            val viewModel = androidx.lifecycle.viewmodel.compose.viewModel<com.nathanrds.caftracker.presentation.viewmodel.HomeViewModel>(
                factory = viewModelFactory
            )
            HomeScreen(
                viewModel = viewModel,
                onNavigateToProducts = { navController.navigate(Route.PRODUCTS) },
                onNavigateToAddIntake = { navController.navigate(Route.ADD_INTAKE) }
            )
        }

        composable(Route.PRODUCTS) {
            val viewModel = androidx.lifecycle.viewmodel.compose.viewModel<com.nathanrds.caftracker.presentation.viewmodel.ProductsViewModel>(
                factory = viewModelFactory
            )
            ProductsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddProduct = { navController.navigate(Route.ADD_PRODUCT) },
                onNavigateToEditProduct = { productId ->
                    navController.navigate(Route.editProduct(productId))
                }
            )
        }

        composable(Route.ADD_INTAKE) {
            val viewModel = androidx.lifecycle.viewmodel.compose.viewModel<com.nathanrds.caftracker.presentation.viewmodel.AddIntakeViewModel>(
                factory = viewModelFactory
            )
            AddIntakeScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Route.ADD_PRODUCT) {
            val viewModel = androidx.lifecycle.viewmodel.compose.viewModel<com.nathanrds.caftracker.presentation.viewmodel.AddEditProductViewModel>(
                factory = AddEditProductViewModelFactory(container, null)
            )
            AddEditProductScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Route.EDIT_PRODUCT,
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId")
            val viewModel = androidx.lifecycle.viewmodel.compose.viewModel<com.nathanrds.caftracker.presentation.viewmodel.AddEditProductViewModel>(
                factory = AddEditProductViewModelFactory(container, productId)
            )
            AddEditProductScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}