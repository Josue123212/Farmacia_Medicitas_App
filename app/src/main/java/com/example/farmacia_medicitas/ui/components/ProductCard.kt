package com.example.farmacia_medicitas.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.farmacia_medicitas.R
import com.example.farmacia_medicitas.data.model.Product
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.platform.LocalContext

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp).animateContentSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.product_image_content_description, product.name),
                modifier = Modifier.height(140.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.name, style = MaterialTheme.typography.titleMedium, maxLines = 2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = stringResource(id = R.string.price_simple, product.price), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            var pressed by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(targetValue = if (pressed) 1.06f else 1f, animationSpec = spring(), label = "addScale")
            val color by animateColorAsState(targetValue = if (pressed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary, label = "addColor")
            Button(onClick = { pressed = !pressed; onAddToCart() }, colors = ButtonDefaults.buttonColors(containerColor = color), modifier = Modifier.scale(scale)) {
                Text(text = stringResource(id = R.string.add_to_cart))
            }
        }
    }
}

@Composable
fun ProductCardPreview() {
    val sample = Product(id = "1", name = "Producto de ejemplo", description = "Descripción", price = 9.99, imageUrl = null, inStock = true)
    ProductCard(product = sample, onClick = {}, onAddToCart = {})
}
