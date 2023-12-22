package com.example.androidproject.todo.util

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit.SECONDS

class MyWorker(
    context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        // perform long running operation
        val items = workerParams.inputData.keyValueMap.filterKeys { it.startsWith("item_") }
        var s = 0.0
        var k = 0
        for (el in items.values) {
            if(isStopped){
                break;
            }
            SECONDS.sleep(1)
            k++
            Log.d("MyWorker", "progress: $k")
            setProgressAsync(workDataOf("progress" to k))
            s += (el as? Double) ?: 0.0
        }
        return Result.success(workDataOf(
            "result" to s,
            "progress" to k))
    }
}