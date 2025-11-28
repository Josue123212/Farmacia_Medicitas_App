package com.example.farmacia_medicitas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmacia_medicitas.data.cart.CartRepository
import com.example.farmacia_medicitas.data.model.CartItem
import com.example.farmacia_medicitas.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repo: CartRepository
) : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val itemsFlow: StateFlow<List<CartItem>> = _items

    val items: List<CartItem> get() = _items.value

    val total: Double
        get() = _items.value.sumOf { it.product.price * it.quantity }

    init {
        viewModelScope.launch {
            repo.items.collectLatest { list ->
                _items.value = list
            }
        }
    }

    fun addItem(product: Product) {
        viewModelScope.launch { repo.add(product) }
    }

    fun increase(item: CartItem) {
        viewModelScope.launch { repo.increase(item.product.id) }
    }

    fun decrease(item: CartItem) {
        viewModelScope.launch { repo.decrease(item.product.id) }
    }
}
