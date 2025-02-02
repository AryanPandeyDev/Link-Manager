package com.example.linkmanager.data

import android.content.Context
import androidx.compose.runtime.IntState
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Link::class], version = 2, exportSchema = true)
abstract class LinkDataBase : RoomDatabase() {

    abstract fun getLinkDao() : LinkDAO

}

