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

    val allLink: Flow<List<Link>> = linkDAO.getAllLinks()

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