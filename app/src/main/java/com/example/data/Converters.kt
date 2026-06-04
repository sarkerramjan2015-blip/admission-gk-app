package com.example.data

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Converters {
    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, String::class.java)
    private val jsonAdapter = moshi.adapter<List<String>>(listType)

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return if (value == null) "[]" else jsonAdapter.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        return if (value == null) emptyList() else jsonAdapter.fromJson(value) ?: emptyList()
    }
}
