package com.example.androidproject.todo.ui.items

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidproject.MyApplication
import com.example.androidproject.core.TAG
import com.example.androidproject.todo.data.Item
import com.example.androidproject.todo.data.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemsViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    private val _loading = MutableStateFlow(false)

    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    val uiState: Flow<List<Item>> = itemRepository.itemStream

    init {
        Log.d(TAG, "init")
        loadItems()
    }

    private fun loadItems() {
        Log.d(TAG, "loadItems...")
        viewModelScope.launch {
            _loading.value = true
            try {
                itemRepository.refresh()
                _loading.value = false
            } catch (e: Exception) {
                Log.w(TAG, "refresh failed", e)
                _loading.value = false
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                ItemsViewModel(app.container.itemRepository)
            }
        }
    }
}
