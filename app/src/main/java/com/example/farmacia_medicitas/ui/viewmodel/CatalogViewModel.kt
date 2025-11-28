package com.example.farmacia_medicitas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmacia_medicitas.core.Constants
import com.example.farmacia_medicitas.core.Result
import com.example.farmacia_medicitas.data.model.Product
import com.example.farmacia_medicitas.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class CatalogState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
    val pageSize: Int = Constants.DEFAULT_PAGE_SIZE
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogState())
    val state: StateFlow<CatalogState> = _state

    init {
        viewModelScope.launch {
            repository.observeCache().collectLatest { cached ->
                _state.value = _state.value.copy(products = cached, isLoading = false, error = null)
            }
        }
        loadProducts(page = 1)
    }

    private fun loadProducts(page: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val res = repository.getProducts(page = page, pageSize = _state.value.pageSize)) {
                is Result.Success -> {
                    val pageData = res.data
                    _state.value = _state.value.copy(
                        products = pageData.products,
                        isLoading = false,
                        error = null,
                        currentPage = pageData.currentPage,
                        totalPages = pageData.totalPages,
                        hasNext = pageData.hasNext,
                        hasPrevious = pageData.hasPrevious,
                        pageSize = pageData.pageSize
                    )
                }
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        products = _state.value.products,
                        isLoading = false,
                        error = res.message
                    )
                }
            }
        }
    }

    fun getProduct(id: String): Product? = _state.value.products.find { it.id == id }

    fun reload() {
        loadProducts(page = _state.value.currentPage)
    }

    fun nextPage() {
        val next = _state.value.currentPage + 1
        if (next <= _state.value.totalPages) {
            loadProducts(page = next)
        }
    }

    fun previousPage() {
        val prev = _state.value.currentPage - 1
        if (prev >= 1) {
            loadProducts(page = prev)
        }
    }
}
