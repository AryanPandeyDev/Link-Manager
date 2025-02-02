package com.example.linkmanager.ui.theme

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkmanager.data.Link
import com.example.linkmanager.data.LinkDAO
import com.example.linkmanager.data.LinkDataBase
import com.example.linkmanager.data.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinkViewModel @Inject constructor(
    private val repository: LinkRepository
) : ViewModel()  {

    val allLink: Flow<List<Link>> = repository.allLink

    fun addLink(link: Link) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLink(link)
        }
    }

    fun updateLink(link: Link) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLink(link)
        }
    }

    fun deleteLink(link: Link) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLink(link)
        }
    }

}