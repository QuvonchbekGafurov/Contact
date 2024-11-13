package com.example.contactapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.local.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    // Qidiruv so'rovini boshqarish uchun `MutableStateFlow`
    private val _searchQuery = MutableStateFlow("")

    // Barcha kontaktlarni yoki qidiruv natijalarini `Flow` orqali kuzatish
    val contacts: StateFlow<List<Contact>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getAllContacts() // Barcha kontaktlarni yuklash
            } else {
                repository.searchContacts(query) // Qidiruv natijalarini olish
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Qidiruv so'rovini yangilash uchun funksiya
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            repository.insertContact(contact)
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            repository.insertContact(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }
}
