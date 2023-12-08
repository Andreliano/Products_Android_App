package com.example.androidproject.todo.ui.item

import android.util.Log
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidproject.R
import com.example.androidproject.core.Result
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(itemId: String?, onClose: () -> Unit) {
    val itemViewModel = viewModel<ItemViewModel>(factory = ItemViewModel.Factory(itemId))
    val itemUiState = itemViewModel.uiState
    var name by rememberSaveable { mutableStateOf(itemUiState.item.name) }

    var price by rememberSaveable { mutableStateOf(itemUiState.item.price) }

    var amount by rememberSaveable { mutableStateOf(itemUiState.item.amount) }

    var category by rememberSaveable { mutableStateOf(itemUiState.item.category) }

    var isAvailable by rememberSaveable { mutableStateOf(itemUiState.item.isAvailable) }

    var producer by rememberSaveable { mutableStateOf(itemUiState.item.producer) }

    var specifications by rememberSaveable { mutableStateOf(itemUiState.item.specifications) }

    var additionDate by rememberSaveable { mutableStateOf(itemUiState.item.additionDate) }

    var showDatePicker by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    val context = LocalContext.current

    Log.d(
        "ItemScreen",
        "recompose, {name = $name, price = $price, amount = $amount, category = $category, isAvailable = $isAvailable, producer = $producer, specifications = $specifications, additionDate = $additionDate}"
    )

    LaunchedEffect(itemUiState.submitResult) {
        Log.d("ItemScreen", "Submit = ${itemUiState.submitResult}")
        if (itemUiState.submitResult is Result.Success) {
            Log.d("ItemScreen", "Closing screen")
            onClose()
        }
    }

    var textInitialized by remember { mutableStateOf(itemId == null) }
    LaunchedEffect(itemId, itemUiState.loadResult) {
        Log.d("ItemScreen", "Text initialized = ${itemUiState.loadResult}")
        if (textInitialized) {
            return@LaunchedEffect
        }
        if (itemUiState.loadResult !is Result.Loading) {
            name = itemUiState.item.name
            price = itemUiState.item.price
            amount = itemUiState.item.amount
            category = itemUiState.item.category
            isAvailable = itemUiState.item.isAvailable
            producer = itemUiState.item.producer
            specifications = itemUiState.item.specifications
            additionDate = itemUiState.item.additionDate
            textInitialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.item)) },
                actions = {
                    Button(onClick = {
                        Log.d(
                            "ItemScreen",
                            "save item {name = $name, price = $price, amount = $amount, category = $category, isAvailable = $isAvailable, producer = $producer, specifications = $specifications, additionDate = $additionDate}"
                        )
                        itemViewModel.saveOrUpdateItem(
                            name,
                            price,
                            amount,
                            category,
                            isAvailable,
                            producer,
                            specifications,
                            additionDate
                        )
                    }) { Text("Save") }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (itemUiState.loadResult is Result.Loading) {
                CircularProgressIndicator()
                return@Scaffold
            }
            if (itemUiState.submitResult is Result.Loading) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { LinearProgressIndicator() }
            }
            if (itemUiState.loadResult is Result.Error) {
                Text(text = "Failed to load item - ${(itemUiState.loadResult as Result.Error).exception?.message}")
            }
            Row {
                TextField(
                    value = name,
                    onValueChange = { name = it }, label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row {
                TextField(
                    value = price.toString(),
                    onValueChange = {
                        price = it.toDoubleOrNull() ?: price
                    },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row {
                TextField(
                    value = amount.toString(),
                    onValueChange = {
                        amount = it.toIntOrNull() ?: amount
                    },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Row {
                TextField(
                    value = category,
                    onValueChange = {category = it},
                    label = {Text("Category")}
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isAvailable,
                    onCheckedChange = { isAvailable = it }
                )
                Text(text = "Is Available")
            }
            Row {
                TextField(
                    value = producer,
                    onValueChange = {producer = it},
                    label = {Text("Producer")}
                )
            }
            Row {
                TextField(
                    value = specifications,
                    onValueChange = {specifications = it},
                    label = {Text("Specifications")}
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { showDatePicker = true }) {
                    Text("Select Addition Date")
                }

                Text("Selected date: ${additionDate.formatToString()}")
            }
            if (showDatePicker) {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                        additionDate = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }.time
                        showDatePicker = false
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()
            }
            if (itemUiState.submitResult is Result.Error) {
                Text(
                    text = "Failed to submit item - ${(itemUiState.submitResult as Result.Error).exception?.message}",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private fun Date.formatToString(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(this)
}


@Preview
@Composable
fun PreviewItemScreen() {
    ItemScreen(itemId = "0", onClose = {})
}
