package com.example.contactapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.contactapp.ContactViewModel
import com.example.contactapp.R
import com.example.contactapp.local.Contact
import java.time.format.TextStyle

@Composable
fun AddEditContactScreen(
    navController: NavController,
    contactId: Int,
    viewModel: ContactViewModel = hiltViewModel()
) {
    val contacts by viewModel.contacts.collectAsState()
    val interactionSource = remember { MutableInteractionSource() } // Interaction Source
    // Foydalanuvchi kirganida kerakli kontaktni olish
    val contact = if (contactId == -1) null else contacts.find { it.id == contactId }

    var contactName by remember { mutableStateOf(contact?.name ?: "") }
    var contactPhoneNumber by remember { mutableStateOf(contact?.phoneNumber ?: "") }

    // LaunchedEffect orqali kontakt yuklanishini kuzatamiz va mavjud qiymatlarini maydonlarga o'rnatamiz
    LaunchedEffect(contact) {
        contact?.let {
            contactName = it.name
            contactPhoneNumber = it.phoneNumber
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_keyboard_backspace_24),
            contentDescription = "",
            modifier = Modifier
                .padding(top = 10.dp)
                .size(40.dp)
                .clickable (
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { navController.popBackStack()} )
        )
        androidx.compose.material.OutlinedTextField(
            value = contactName,
            onValueChange = { contactName = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(15.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        androidx.compose.material.OutlinedTextField(
            value = contactPhoneNumber,
            onValueChange = { contactPhoneNumber = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(15.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Faqat raqam kiritish imkoniyati
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val newContact = Contact(
                    id = contact?.id ?: 0, // Mavjud kontakt bo'lsa eski id saqlanadi, aks holda 0
                    name = contactName,
                    phoneNumber = contactPhoneNumber
                )
                if (contact == null) {
                    viewModel.addContact(newContact)
                } else {
                    viewModel.updateContact(newContact)
                }
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}
