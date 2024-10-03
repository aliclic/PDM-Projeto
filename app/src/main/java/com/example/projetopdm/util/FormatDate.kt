package com.example.projetopdm.util

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDateToBrazilian(dateString: String?): String {
    return dateString?.let {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            val date = inputFormat.parse(it)
            outputFormat.format(date)
        } catch (e: Exception) {
            "N/A" // Retorna "N/A" em caso de erro
        }
    } ?: "N/A"
}