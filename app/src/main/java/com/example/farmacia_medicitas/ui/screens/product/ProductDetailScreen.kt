package com.example.farmacia_medicitas.ui.screens.product

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.farmacia_medicitas.data.model.Product
import com.example.farmacia_medicitas.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProductDetailScreen(
    product: Product?,
    onBack: () -> Unit,
    onAddToCart: (Product) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = product?.name ?: stringResource(id = R.string.app_name)) },
            navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás") }
            }
            , actions = {
                val context = LocalContext.current
                IconButton(onClick = {
                    val text = if (product != null) {
                        "${product.name}\n${product.description}"
                    } else {
                        context.getString(R.string.app_name)
                    }
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, text)
                    }
                    context.startActivity(Intent.createChooser(intent, "Compartir producto"))
                }) {
                    Icon(Icons.Filled.Share, contentDescription = "Compartir")
                }
            }
        )

        if (product == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.product_not_found))
            }
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                // Imagen si existe
                if (product.imageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                            .data(product.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        placeholder = painterResource(id = R.drawable.ic_launcher_background),
                        error = painterResource(id = R.drawable.ic_launcher_background)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Text(text = product.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(id = R.string.price_label, product.price), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { onAddToCart(product) }) {
                    Text(text = stringResource(id = R.string.add_to_cart))
                }
            }
        }
    }
}
