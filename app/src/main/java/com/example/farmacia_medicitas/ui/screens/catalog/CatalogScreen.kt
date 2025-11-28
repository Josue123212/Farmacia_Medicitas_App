package com.example.farmacia_medicitas.ui.screens.catalog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.farmacia_medicitas.R
import com.example.farmacia_medicitas.data.model.Product
import com.example.farmacia_medicitas.ui.viewmodel.CatalogState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ButtonDefaults
import androidx.compose.animation.animateContentSize
import com.example.farmacia_medicitas.ui.theme.Success
import com.example.farmacia_medicitas.ui.components.ProductCard
import androidx.compose.ui.tooling.preview.Preview

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun CatalogScreen(
    state: CatalogState,
    onProductClick: (String) -> Unit,
    onCartClick: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRetry: () -> Unit = {},
    onPreviousPage: () -> Unit = {},
    onNextPage: () -> Unit = {}
) {
    val selectedBottomItem = remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.catalog_title_offers)) },
                actions = {
                    Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = stringResource(id = R.string.action_sort), modifier = Modifier.padding(end = 8.dp))
                    Icon(Icons.Filled.FilterList, contentDescription = stringResource(id = R.string.action_filter))
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = stringResource(id = R.string.action_settings),
                        modifier = Modifier.padding(start = 8.dp).clickable { onSettingsClick() }
                    )
                }
            )
        },
        floatingActionButton = {
            val pulse = rememberInfiniteTransition(label = "fab")
            val scale by pulse.animateFloat(
                initialValue = 1f,
                targetValue = 1.06f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "fabScale"
            )
            FloatingActionButton(onClick = onCartClick, modifier = Modifier.scale(scale)) {
                Icon(Icons.Default.ShoppingCart, contentDescription = stringResource(id = R.string.action_cart))
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedBottomItem.intValue == 0,
                    onClick = { selectedBottomItem.intValue = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = stringResource(id = R.string.nav_home)) },
                    label = { Text(stringResource(id = R.string.nav_home)) }
                )
                NavigationBarItem(
                    selected = selectedBottomItem.intValue == 1,
                    onClick = { selectedBottomItem.intValue = 1; onSearchClick() },
                    icon = { Icon(Icons.Default.Search, contentDescription = stringResource(id = R.string.nav_search)) },
                    label = { Text(stringResource(id = R.string.nav_search)) }
                )
                NavigationBarItem(
                    selected = selectedBottomItem.intValue == 2,
                    onClick = { onCartClick() },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = stringResource(id = R.string.action_cart)) },
                    label = { Text(stringResource(id = R.string.action_cart)) }
                )
                NavigationBarItem(
                    selected = selectedBottomItem.intValue == 3,
                    onClick = { selectedBottomItem.intValue = 3; onProfileClick() },
                    icon = { Icon(Icons.Default.Person, contentDescription = stringResource(id = R.string.nav_profile)) },
                    label = { Text(stringResource(id = R.string.nav_profile)) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val uiState = when {
                state.isLoading -> "loading"
                state.error != null -> "error"
                state.products.isEmpty() -> "empty"
                else -> "content"
            }
            // Carrusel de banners en el catálogo (antes estaba en Explore)
            // URLs proporcionadas por el usuario
            val bannerUrls = listOf(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQGKeOfgG707Ajoj8izq71Adv3hiH9rSsdtmw&s",
                "https://farmaciauniversalpe.vtexassets.com/arquivos/ids/156423/00908_1.jpg?v=638417260707800000",
                "https://www.infobae.com/new-resizer/j6JtzYQQtnY2j_DMzFRXRl9xUk0=/arc-anglerfish-arc2-prod-infobae/public/HUQZNKUXJVEG3EGAF32GIOAI4I"
            )
            val pagerState = rememberPagerState(pageCount = { bannerUrls.size })
            // Auto-scroll cada 3 segundos
            LaunchedEffect(Unit) {
                while (true) {
                    delay(3000)
                    val next = (pagerState.currentPage + 1) % pagerState.pageCount
                    pagerState.animateScrollToPage(next)
                }
            }
            HorizontalPager(state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(bottom = 8.dp)) { page ->
                AsyncImage(
                    model = bannerUrls[page],
                    contentDescription = stringResource(id = R.string.banner_content_description, page + 1),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    error = painterResource(id = R.drawable.ic_launcher_background)
                )
            }

            // Encabezado: número de productos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.products_found_count, state.products.size),
                    style = MaterialTheme.typography.titleSmall
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.page_indicator, state.currentPage, state.totalPages),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Button(onClick = onRetry, enabled = !state.isLoading) {
                        Text(text = stringResource(id = R.string.action_reload))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onPreviousPage, enabled = state.hasPrevious) {
                    Text(text = stringResource(id = R.string.action_previous))
                }
                Button(onClick = onNextPage, enabled = state.hasNext) {
                    Text(text = stringResource(id = R.string.action_next))
                }
            }

            Crossfade(targetState = uiState) { s ->
                when (s) {
                    "loading" -> {
                        AnimatedVisibility(visible = true) {
                            Text(
                                text = stringResource(id = R.string.loading_products),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    "error" -> {
                        AnimatedVisibility(visible = true) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = state.error ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Red
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(onClick = onRetry) {
                                    Text(text = stringResource(id = R.string.action_retry))
                                }
                            }
                        }
                    }
                    "empty" -> {
                        AnimatedVisibility(visible = true) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.empty_products),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(onClick = onRetry) {
                                    Text(text = stringResource(id = R.string.action_reload))
                                }
                            }
                        }
                    }
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.products) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = { onProductClick(product.id) },
                                    onAddToCart = { onAddToCart(product) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CatalogScreenPreview() {
    val products = listOf(
        Product(id = "1", name = "Ejemplo 1", description = "Desc", price = 10.0, imageUrl = null, inStock = true),
        Product(id = "2", name = "Ejemplo 2", description = "Desc", price = 20.0, imageUrl = null, inStock = true)
    )
    val state = CatalogState(products = products, isLoading = false, error = null, currentPage = 1, totalPages = 1)
    CatalogScreen(
        state = state,
        onProductClick = {},
        onCartClick = {},
        onAddToCart = {},
        onSearchClick = {},
        onProfileClick = {},
        onSettingsClick = {},
        onRetry = {},
        onPreviousPage = {},
        onNextPage = {}
    )
}

// ProductCard extraído a ui/components/ProductCard.kt
