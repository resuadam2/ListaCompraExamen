package com.resuadam2.listacompraexamen.model

import com.resuadam2.listacompraexamen.data.products

data class Product(
    val name: String,
    val price: Float,
    val quantity: Int,
) {
    val totalPrice: Float
        get() = price * quantity
}

fun getFakeProducts(size: Int = 10): List<Product> {
    return List(size) { getFakeProduct() }
}

fun getFakeProduct(): Product {
    return Product(
        name = products.random(),
        price = (1..10).random().toFloat(),
        quantity = (1..5).random()
    )
}