package com.example.contactapp.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts ORDER BY name")
    fun getAllContacts(): kotlinx.coroutines.flow.Flow<List<Contact>> // Flow<List<Contact>> qaytaradi

    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :query || '%' ORDER BY name")
    fun searchContacts(query: String): Flow<List<Contact>> // Flow<List<Contact>> qidiruv uchun

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)
}
