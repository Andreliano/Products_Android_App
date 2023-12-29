package com.example.androidproject.todo.ui.items

import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidproject.R
import com.example.androidproject.todo.ui.animations.MyFloatingActionButton
import com.example.androidproject.todo.ui.jobs.MyJobs
import com.example.androidproject.todo.ui.status.MyNetworkStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(onItemClick: (id: String?) -> Unit, onAddItem: () -> Unit, onLogout: () -> Unit) {
    Log.d("ItemsScreen", "recompose")
    var isAdding by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val itemsViewModel = viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory)

    val isLoading by itemsViewModel.loading.collectAsStateWithLifecycle()

    val itemsUiState by itemsViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )

    suspend fun showAddMessage() {
        if (!isAdding) {
            isAdding = true
            delay(1500L)
            isAdding = false
            delay(1000L)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = stringResource(id = R.string.items))
                        MyNetworkStatus()
                    }
                },
                actions = {
                    Button(onClick = onLogout) { Text("Logout") }
                }
            )
        },
        floatingActionButton = {
            MyFloatingActionButton(
                isAdding = isAdding,
                onClick = {
                    coroutineScope.launch {
                        showAddMessage()
                        Log.d("ItemsScreen", "add")
                        onAddItem()
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                LoadingRow() // Show loading row when items are being fetched
            } else {
                MyJobs(itemsUiState)
                ItemList(
                    itemList = itemsUiState,
                    onItemClick = onItemClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }

    }
}

@Composable
private fun LoadingRow() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue.copy(alpha = 1f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { },
                    onTap = { }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Fetching products",
            color = Color.White
        )
    }
}

@Preview
@Composable
fun PreviewItemsScreen() {
    ItemsScreen(onItemClick = {}, onAddItem = {}, onLogout = {})
}
