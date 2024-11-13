package com.example.contactapp

import com.example.contactapp.local.Contact
import com.example.contactapp.local.ContactDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow


@Singleton
class ContactRepository @Inject constructor(private val contactDao: ContactDao) {
    fun getAllContacts(): Flow<List<Contact>> = contactDao.getAllContacts()
    fun searchContacts(query: String): Flow<List<Contact>> = contactDao.searchContacts(query)
    suspend fun insertContact(contact: Contact) = contactDao.insertContact(contact)
    suspend fun deleteContact(contact: Contact) = contactDao.deleteContact(contact)
}

