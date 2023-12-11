package com.example.androidproject.todo.ui.item

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidproject.MyApplication
import com.example.androidproject.core.Result
import com.example.androidproject.core.TAG
import com.example.androidproject.todo.data.Item
import com.example.androidproject.todo.data.ItemRepository
import kotlinx.coroutines.launch
import java.util.Date

data class ItemUiState(
    val itemId: String? = null,
    val item: Item = Item(),
    var loadResult: Result<Item>? = null,
    var submitResult: Result<Item>? = null,
)

class ItemViewModel(private val itemId: String?, private val itemRepository: ItemRepository) :
    ViewModel() {

    var uiState: ItemUiState by mutableStateOf(ItemUiState(loadResult = Result.Loading))
        private set

    init {
        Log.d(TAG, "init")
        if (itemId != null) {
            loadItem()
        } else {
            uiState = uiState.copy(loadResult = Result.Success(Item()))
        }
    }

    fun loadItem() {
        viewModelScope.launch {
            itemRepository.itemStream.collect { items ->
                if (!(uiState.loadResult is Result.Loading)) {
                    return@collect
                }
                val item = items.find { it._id == itemId } ?: Item()
                uiState = uiState.copy(item = item, loadResult = Result.Success(item))
            }
        }
    }


    fun saveOrUpdateItem(
        name: String,
        price: Double,
        amount: Int,
        category: String,
        isAvailable: Boolean,
        producer: String,
        specifications: String,
        additionDate: Date,
        latitude: Double,
        longitude: Double,
        onNewItemSaved: () -> Unit
    ) {
        viewModelScope.launch {
            Log.d(TAG, "saveOrUpdateItem...");
            try {
                uiState = uiState.copy(submitResult = Result.Loading)
                val item = uiState.item.copy(
                    name = name,
                    price = price,
                    amount = amount,
                    category = category,
                    isAvailable = isAvailable,
                    producer = producer,
                    specifications = specifications,
                    additionDate = additionDate,
                    latitude = latitude,
                    longitude = longitude
                )
                val savedItem: Item;
                if (itemId == null) {
                    savedItem = itemRepository.save(item)
                    onNewItemSaved()
                } else {
                    savedItem = itemRepository.update(item)
                }
                Log.d(TAG, "saveOrUpdateItem succeeeded");
                uiState = uiState.copy(submitResult = Result.Success(savedItem))
            } catch (e: Exception) {
                Log.d(TAG, "saveOrUpdateItem failed");
                uiState = uiState.copy(submitResult = Result.Error(e))
            }
        }
    }

    companion object {
        fun Factory(itemId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                ItemViewModel(itemId, app.container.itemRepository)
            }
        }
    }
}
