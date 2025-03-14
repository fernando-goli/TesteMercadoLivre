package com.example.testemercadolivre.core.util

import androidx.appcompat.widget.SearchView

fun SearchView.setupQueryTextSubmit(callback: (String?) -> Unit) {
    setOnQueryTextListener(
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                callback(query)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })
}