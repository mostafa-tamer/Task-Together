package com.mostafatamer.tasktogether

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val jsonConverter = Gson()


fun getSystemLanguage(): String {
    return Locale.getDefault().language
}

fun formatDate(date: Date?, pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(date ?: Date())
}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}