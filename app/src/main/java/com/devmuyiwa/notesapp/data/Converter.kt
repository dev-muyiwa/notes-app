package com.devmuyiwa.notesapp.data

import androidx.room.TypeConverter
import com.devmuyiwa.notesapp.domain.Category

class Converter {
    @TypeConverter
    fun categoryToString(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun stringToCategory(name: String): Category {
        return Category.valueOf(name)
    }
}