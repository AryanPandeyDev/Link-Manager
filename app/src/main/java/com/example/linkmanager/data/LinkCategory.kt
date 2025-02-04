package com.example.linkmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LinkCategory(
    val category: String,
    @PrimaryKey(autoGenerate = true)
    val categoryId : Int = 0
)
