package com.example.linkmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Link(
    val linkName : String,
    val link: String,
    val category: String = "Default",
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
)

