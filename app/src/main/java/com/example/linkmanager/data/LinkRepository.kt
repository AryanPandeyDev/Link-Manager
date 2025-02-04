package com.example.linkmanager.data

import androidx.lifecycle.viewModelScope
import androidx.room.Update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LinkRepository
    @Inject constructor(private val linkDAO: LinkDAO)
{

    fun allLink(category : String): Flow<List<Link>> {
        return linkDAO.getAllLinks(category)
    }

    val allDefaultLinks = linkDAO.allLinks()

    val allCategory = linkDAO.getAllCategories()

    suspend fun addCategory(category : LinkCategory) {
        return linkDAO.addCategory(category)
    }

    suspend fun deleteCategory(category: LinkCategory) {
        return linkDAO.deleteCategory(category)
    }

    suspend fun updateCategory(category : LinkCategory) {
        return linkDAO.updateCategory(category)
    }

    suspend fun addLink(link: Link) {
        linkDAO.addLink(link)
    }

    suspend fun updateLink(link: Link) {
        linkDAO.updateLink(link)
    }

    suspend fun deleteLink(link: Link) {
        linkDAO.deleteLink(link)
    }


}