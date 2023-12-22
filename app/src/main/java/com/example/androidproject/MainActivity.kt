package com.example.androidproject

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.androidproject.core.TAG
import com.example.androidproject.todo.ui.location.MyLocation
import com.example.androidproject.todo.ui.status.MyNetworkStatus
import com.example.androidproject.todo.util.Permissions
import com.example.androidproject.ui.theme.MyAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.d(TAG, "onCreate")
            MyApp {
                MyAppNavHost()
//                Permissions(
//                    permissions = listOf(
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ),
//                    rationaleText = "Please allow app to use location (coarse or fine)",
//                    dismissedText = "O noes! No location provider allowed!"
//                ) {
//                    MyLocation()
//                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            (application as MyApplication).container.itemRepository.openWsClient()
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            (application as MyApplication).container.itemRepository.closeWsClient()
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Log.d("MyApp", "recompose")
    MyAppTheme {
        Surface {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewMyApp() {
    MyApp {
        MyAppNavHost()
    }
}
