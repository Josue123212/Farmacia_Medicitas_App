package com.example.farmacia_medicitas.navigation

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("home")
    data object Catalog : NavRoutes("catalog")
    data object Cart : NavRoutes("cart")
    data object Checkout : NavRoutes("checkout")
    data object Login : NavRoutes("login")
    // Ruta alternativa para login que redirige a checkout tras autenticación
    data object LoginToCheckout : NavRoutes("login_to_checkout")
    data object RegisterToCheckout : NavRoutes("register_to_checkout")
    data object Register : NavRoutes("register")
    data object Search : NavRoutes("search")
    data object Profile : NavRoutes("profile")
    data object WebView : NavRoutes("webview")

    data object ProductDetail : NavRoutes("product/{id}") {
        val routeTemplate: String = route
        fun route(id: String) = "product/$id"
    }
}
