package org.codingforanimals.veganuniverse.recipe.data.storage.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

internal object ListConverter {
    @TypeConverter
    fun stringToList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun listToString(value: List<String>): String {
        return Gson().toJson(value)
    }
}

internal object DateConverter {
    @TypeConverter
    fun longToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToLong(value: Date): Long {
        return value.time
    }
}