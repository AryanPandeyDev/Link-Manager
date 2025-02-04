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

    @Insert
    suspend fun addCategory(category: LinkCategory)

    @Delete
    suspend fun deleteCategory(category: LinkCategory)

    @Update
    suspend fun updateCategory(category : LinkCategory)

    @Query("SELECT * FROM LinkCategory order by categoryId")
    fun getAllCategories() : Flow<List<LinkCategory>>

    @Query("SELECT * FROM LINK where link.category = :value order by link.id desc")
    fun getAllLinks(value : String) : Flow<List<Link>>

    @Query("SELECT * FROM LINK ORDER BY ID DESC")
    fun allLinks() : Flow<List<Link>>
}