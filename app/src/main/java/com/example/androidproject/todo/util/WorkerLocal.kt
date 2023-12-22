package com.example.androidproject.todo.util

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.androidproject.MyApplication

class WorkerLocal(
    context: Context,
    private val workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val itemRepository = (applicationContext as MyApplication).container.itemRepository

        val notSaved = itemRepository.getLocallySaved()
        Log.d("WorkerLocal", notSaved.toString())

        notSaved.forEach { item ->
            item.isSaved = true
            itemRepository.save(item)
        }

        return Result.success()
    }
}