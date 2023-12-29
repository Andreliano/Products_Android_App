package com.example.androidproject.todo.ui.sensors
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


@OptIn(ExperimentalCoroutinesApi::class)
class ProximitySensorMonitor(val context: Context) {

    //private val powerManager: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    //private var wakeLock: PowerManager.WakeLock? = null

    //init {
      //   wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "MyApp:TagForProximityLock")
    //}

    val isNear: Flow<Boolean> = callbackFlow<Boolean> {
        val sensorManager: SensorManager =
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val proximitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        val proximitySensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            }

            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                    if (event.values[0] == 0f) {
                        Log.d("PROXIMITY", event.values[0].toString())
                        channel.trySend(true) // near
                        //wakeLock?.acquire(10*60*1000L)
                    } else {
                        Log.d("PROXIMITY", event.values[0].toString())
                        channel.trySend(false) // away
//                        if (wakeLock?.isHeld == true) {
//                            wakeLock?.release()
//                        }
                    }
                }
            }

        }

        sensorManager.registerListener(
            proximitySensorEventListener,
            proximitySensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        awaitClose {
//            if (wakeLock?.isHeld == true) {
//                wakeLock?.release()
//            }
            sensorManager.unregisterListener(proximitySensorEventListener)
        }
    }
}
