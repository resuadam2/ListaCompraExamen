package com.resuadam2.listacompraexamen.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.resuadam2.listacompraexamen.ui.state.ShoppingListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaCompraScreen(
    vm: ShoppingListViewModel = viewModel(),
    navigateBack: () -> Unit,
    onDetalleCompra: (String, String) -> Unit
) {
    val state by vm.state.collectAsState()
    val products = vm.products

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text( text = "Shopping List", fontSize = 24.sp)
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navigateBack() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        bottomBar = {
            BottomAppBar (
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ){
                TotalRow(
                    totalQuantity = state.totalQuantity.toString(),
                    totalPrice = state.totalPrice.toString(),
                )
            }
        }
    ) {
        Column (
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            AddProductRow(
                value = state.newProduct,
                changingStringValue = {
                    vm.changeNewProduct(it)
                },
                price = state.newPrice,
                changingPrice = {
                    vm.changeNewPrice(it)
                },
                addProduct = { name, price ->
                   vm.addProduct(name, price)
                },
                addRandomProduct = { vm.addRandomProduct() }
            )
            LazyColumn(
                modifier = Modifier.weight(1f)
            )
            {
                items(products.size) { index ->
                    val product = products.elementAt(index)
                    ShoppingListItem(
                        name = product.name,
                        quantity = product.quantity.toString(),
                        increaseQuantity = { vm.increaseQuantity(product) },
                        decreaseQuantity = { vm.decreaseQuantity(product) },
                        details = { onDetalleCompra(product.name, product.price.toString()) },
                        delete = { vm.removeProduct(product) }
                    )
                }
            }

        }
    }
}

@Composable
fun TotalRow(totalQuantity: String, totalPrice: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ){
        Text("Total", fontSize = 20.sp)
        Text("Cantidad: $totalQuantity", fontSize = 20.sp)
        Text("Precio: $totalPrice", fontSize = 20.sp)
    }
}

@Composable
fun AddProductRow(
    value: String,
    price: String,
    context: Context = LocalContext.current,
    changingPrice: (String) -> Unit,
    changingStringValue: (String) -> Unit,
    addProduct: (String, String) -> Boolean,
    addRandomProduct: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Column {
                TextField(
                    value = value,
                    label = { Text("Nuevo Producto") },
                    onValueChange = { changingStringValue(it) },
                    singleLine = true,
                )
                TextField(
                    value = price,
                    label = { Text("Precio") },
                    onValueChange = { changingPrice(it) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            IconButton(onClick = {
                if (value.isBlank() || price.isBlank()) {
                    Toast.makeText(context, "Product name or price cannot be empty", Toast.LENGTH_SHORT).show()
                    return@IconButton
                }
                if(!addProduct(value, price)) {
                    Toast.makeText(context, "Product already exists", Toast.LENGTH_SHORT).show()
                }
                changingStringValue("")
                changingPrice("")
            } ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add item")
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = {
            addRandomProduct()
        } ) {
            Text("Aleatorio")
            Spacer(modifier = Modifier.size(8.dp))
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add item")
        }
    }
}


@Composable
fun ShoppingListItem(
    name: String,
    quantity: String,
    increaseQuantity: () -> Unit,
    decreaseQuantity: () -> Unit,
    details: () -> Unit,
    delete: () -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    )
    {
        Text(text = name)
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = decreaseQuantity ) {
                Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "Delete item")
            }
            Text(text = quantity,
                fontSize = 20.sp)
            IconButton(onClick = increaseQuantity ) {
                Icon(imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = "Delete item")
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = details) {
                Icon(imageVector = Icons.Filled.Info, contentDescription = "Details")
            }
            IconButton(onClick = delete) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete item")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaCompraScreenPreview() {
    ListaCompraScreen(
        navigateBack = {},
        onDetalleCompra = { _, _ -> }
    )
}