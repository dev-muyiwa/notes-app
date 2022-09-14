package com.devmuyiwa.notesapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmuyiwa.notesapp.domain.Category
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Note(
    // Using val instead of var prevents new data from been added
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var category: Category,
    var description: String
): Parcelable
