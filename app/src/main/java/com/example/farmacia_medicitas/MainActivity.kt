package com.example.farmacia_medicitas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.farmacia_medicitas.navigation.NavRoutes
import com.example.farmacia_medicitas.ui.screens.cart.CartScreen
import com.example.farmacia_medicitas.ui.screens.catalog.CatalogScreen
import com.example.farmacia_medicitas.ui.screens.checkout.CheckoutScreen
import com.example.farmacia_medicitas.ui.screens.login.LoginScreen
import com.example.farmacia_medicitas.ui.screens.login.RegisterScreen
import com.example.farmacia_medicitas.ui.screens.HomeScreen
import com.example.farmacia_medicitas.ui.screens.product.ProductDetailScreen
import com.example.farmacia_medicitas.ui.theme.FarmaciaMedicitasTheme
import com.example.farmacia_medicitas.ui.viewmodel.CartViewModel
import com.example.farmacia_medicitas.ui.viewmodel.CatalogViewModel
import com.example.farmacia_medicitas.ui.screens.search.SearchScreen
import com.example.farmacia_medicitas.ui.viewmodel.AuthViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import dagger.hilt.android.AndroidEntryPoint
import com.example.farmacia_medicitas.ui.screens.profile.ProfileScreen
import com.example.farmacia_medicitas.core.Notifier
import androidx.compose.ui.platform.LocalContext
import com.example.farmacia_medicitas.ui.screens.webview.WebViewScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val EXTRA_NAV_ROUTE = "nav_route"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val initialRoute = intent?.getStringExtra(EXTRA_NAV_ROUTE)
            FarmaciaMedicitasApp(initialRoute)
        }
    }
}

@Composable
fun FarmaciaMedicitasApp(initialRoute: String?) {
    FarmaciaMedicitasTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()
            val catalogViewModel: CatalogViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val cartViewModel: CartViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            AppNavHost(
                navController = navController,
                catalogViewModel = catalogViewModel,
                cartViewModel = cartViewModel,
                initialRoute = initialRoute
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel,
    cartViewModel: CartViewModel,
    initialRoute: String?
) {
    // Collect catalog state inside this composable's scope
    val catalogState = catalogViewModel.state.collectAsStateWithLifecycle()
    androidx.compose.runtime.LaunchedEffect(initialRoute) {
        when (initialRoute) {
            NavRoutes.Search.route -> navController.navigate(NavRoutes.Search.route)
            NavRoutes.Cart.route -> navController.navigate(NavRoutes.Cart.route)
            NavRoutes.Catalog.route -> navController.navigate(NavRoutes.Catalog.route)
            else -> {}
        }
    }
    NavHost(navController = navController, startDestination = NavRoutes.Home.route) {
        composable(NavRoutes.Home.route) {
            HomeScreen(onStartClick = { navController.navigate(NavRoutes.Catalog.route) })
        }
        composable(NavRoutes.Catalog.route) {
            CatalogScreen(
                state = catalogState.value,
                onProductClick = { productId ->
                    navController.navigate(NavRoutes.ProductDetail.route(productId))
                },
                onCartClick = { navController.navigate(NavRoutes.Cart.route) },
                onAddToCart = { p ->
                    cartViewModel.addItem(p)
                },
                onSearchClick = { navController.navigate(NavRoutes.Search.route) },
                onProfileClick = { navController.navigate(NavRoutes.Profile.route) },
                onSettingsClick = { navController.navigate(NavRoutes.WebView.route) },
                onRetry = { catalogViewModel.reload() },
                onPreviousPage = { catalogViewModel.previousPage() },
                onNextPage = { catalogViewModel.nextPage() }
            )
        }

        composable(NavRoutes.ProductDetail.routeTemplate) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("id") ?: return@composable
            val product = catalogViewModel.getProduct(productId)
            ProductDetailScreen(
                product = product,
                onBack = { navController.popBackStack() },
                onAddToCart = { p ->
                    cartViewModel.addItem(p)
                    navController.navigate(NavRoutes.Cart.route)
                }
            )
        }

        composable(NavRoutes.Cart.route) {
            CartScreen(
                items = cartViewModel.items,
                total = cartViewModel.total,
                onIncrease = { cartViewModel.increase(it) },
                onDecrease = { cartViewModel.decrease(it) },
                onCheckout = { navController.navigate(NavRoutes.Checkout.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Checkout.route) {
            val authVm: AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val authState = authVm.state.collectAsStateWithLifecycle()
            val context = LocalContext.current

            // Asegurar verificación de sesión al entrar a Checkout para evitar mostrar el modal por estado inicial desactualizado
            androidx.compose.runtime.LaunchedEffect(Unit) {
                if (!authState.value.isAuthenticated) {
                    authVm.checkSession()
                }
            }

            when {
                authState.value.isAuthenticated -> {
                    CheckoutScreen(
                        total = cartViewModel.total,
                        onConfirm = {
                            Notifier.notifyCheckoutSuccess(context, cartViewModel.total)
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
                authState.value.isLoading -> {
                    // Mostrar un progreso mientras se valida la sesión
                    androidx.compose.material3.Surface {
                        androidx.compose.material3.CircularProgressIndicator()
                    }
                }
                else -> {
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { navController.popBackStack() },
                        title = { androidx.compose.material3.Text("Necesitas iniciar sesión") },
                        text = { androidx.compose.material3.Text("Para finalizar la compra, inicia sesión o crea una cuenta.") },
                        confirmButton = {
                            androidx.compose.material3.TextButton(onClick = { navController.navigate(NavRoutes.LoginToCheckout.route) }) {
                                androidx.compose.material3.Text("Iniciar sesión")
                            }
                        },
                        dismissButton = {
                            androidx.compose.material3.TextButton(onClick = { navController.navigate(NavRoutes.RegisterToCheckout.route) }) {
                                androidx.compose.material3.Text("Crear cuenta")
                            }
                        }
                    )
                }
            }
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(NavRoutes.Catalog.route) {
                    // Elimina Login del back stack para no volver a la pantalla de login
                    popUpTo(NavRoutes.Login.route) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }

        composable(NavRoutes.LoginToCheckout.route) {
            LoginScreen(onLoginSuccess = {
                // Volver al checkout tras login
                navController.navigate(NavRoutes.Checkout.route) {
                    // Elimina LoginToCheckout del back stack para que "Atrás" no regrese a Login
                    popUpTo(NavRoutes.LoginToCheckout.route) { inclusive = true }
                    // Evita duplicar destino y restaura estado si aplica
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }

        composable(NavRoutes.Register.route) {
            RegisterScreen(
                onRegistered = {
                    navController.navigate(NavRoutes.Catalog.route) {
                        // Elimina Register del back stack para no volver a registro
                        popUpTo(NavRoutes.Register.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.RegisterToCheckout.route) {
            RegisterScreen(
                onRegistered = {
                    // Tras registrarse, pedir inicio de sesión y volver al Checkout al completar
                    navController.navigate(NavRoutes.LoginToCheckout.route) {
                        // Elimina RegisterToCheckout del back stack para que "Atrás" no regrese a Registro
                        popUpTo(NavRoutes.RegisterToCheckout.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Search.route) {
            SearchScreen(
                products = catalogState.value.products,
                onBack = { navController.popBackStack() },
                onProductClick = { productId -> navController.navigate(NavRoutes.ProductDetail.route(productId)) },
                onAddToCart = { p -> cartViewModel.addItem(p) }
            )
        }

        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogin = { navController.navigate(NavRoutes.Login.route) },
                onRegister = { navController.navigate(NavRoutes.Register.route) },
                onLogoutNavigate = {
                    navController.navigate(NavRoutes.Catalog.route) {
                        // Limpia el back stack para evitar volver a pantallas protegidas
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(NavRoutes.WebView.route) {
            WebViewScreen(url = "https://www.android.com/intl/es_es/", onBack = { navController.popBackStack() })
        }
    }
}
