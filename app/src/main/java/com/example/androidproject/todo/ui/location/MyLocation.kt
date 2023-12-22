package com.example.androidproject.todo.ui.location

import android.app.Application
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MyLocation(latitude: Double, longitude: Double, onLocationUpdate: (Double, Double) -> Unit) {
    val myLocationViewModel = viewModel<MyLocationViewModel>(
        factory = MyLocationViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )
    val location = myLocationViewModel.uiState
    if (latitude == 0.0 && longitude == 0.0 && location != null) {
        MyMap(location.latitude, location.longitude) { latLng ->
            onLocationUpdate(latLng.latitude, latLng.longitude)
        }
        onLocationUpdate(location.latitude, location.longitude)
    } else if (latitude != 0.0 && longitude != 0.0 && location != null) {
        MyMap(latitude, longitude) { latLng ->
            onLocationUpdate(latLng.latitude, latLng.longitude)
        }
        onLocationUpdate(latitude, longitude)
    } else {
        LinearProgressIndicator()
    }
}
