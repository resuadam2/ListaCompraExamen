package com.resuadam2.listacompraexamen.ui.state

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.resuadam2.listacompraexamen.model.Product
import com.resuadam2.listacompraexamen.model.getFakeProduct
import com.resuadam2.listacompraexamen.model.getFakeProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShoppingListViewModel : ViewModel() {
    private val _state = MutableStateFlow(ShoppingListUiState())
    val state = _state.asStateFlow()

    private val _products = mutableStateListOf<Product>()
    val products: Set<Product> get() = _products.toSet()

    init {
        _products.addAll(getFakeProducts(10))
        updateTotalQuantityAndPrice()
    }

    fun changeNewProduct(newProduct: String) {
        _state.value = state.value.copy(newProduct = newProduct)
    }

    fun changeNewPrice(newPrice: String) {
        _state.value = state.value.copy(newPrice = newPrice)
    }

    fun addRandomProduct() {
        var product = getFakeProduct()
        while (_products.map { it.name }.contains(product.name)) {
            product = getFakeProduct()
        }
        _products.add(0,product)
        updateTotalQuantityAndPrice()
    }

    fun addProduct(name: String, price: String) = if (_products.map { it.name }.contains(name)) {
        false
    } else {
        _products.add(0,Product(name, price.toFloat(), 1))
        updateTotalQuantityAndPrice()
        true
    }

    fun increaseQuantity(product: Product) {
        val index = _products.indexOf(product)
        _products[index] = product.copy(quantity = product.quantity + 1)
        updateTotalQuantityAndPrice()
    }

    fun decreaseQuantity(product: Product) {
        val index = _products.indexOf(product)
        if (product.quantity == 1) {
            removeProduct(product)
            updateTotalQuantityAndPrice()
            return
        }
        _products[index] = product.copy(quantity = product.quantity - 1)
        updateTotalQuantityAndPrice()
    }

    private fun removeProduct(product: Product) {
        _products.remove(product)
    }

    private fun updateTotalQuantityAndPrice() {
        _state.value = state.value.copy(totalQuantity = _products.sumOf { it.quantity },
            totalPrice = _products.sumOf { it.totalPrice.toDouble() }.toFloat())
    }
}