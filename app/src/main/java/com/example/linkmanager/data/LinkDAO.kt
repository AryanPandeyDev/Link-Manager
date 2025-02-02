package com.example.linkmanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface LinkDAO {

    @Update
    suspend fun updateLink(link: Link)

    @Delete
    suspend fun deleteLink(link: Link)

    @Insert
    suspend fun addLink(link: Link)

    @Query("SELECT * from link order by id DESC")
    fun getAllLinks() : Flow<List<Link>>

}