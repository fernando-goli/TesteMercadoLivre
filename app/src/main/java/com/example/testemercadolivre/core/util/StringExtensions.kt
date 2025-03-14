package com.example.testemercadolivre.core.util

import java.text.NumberFormat
import java.util.Locale

fun Double.toCurrency(): String {
    val brazil = Locale("pt", "BR")
    return NumberFormat.getCurrencyInstance(brazil).format(this)
}