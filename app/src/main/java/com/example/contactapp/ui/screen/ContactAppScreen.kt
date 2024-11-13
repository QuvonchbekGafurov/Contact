package com.example.contactapp.ui.screen

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.contactapp.ContactViewModel
import com.example.contactapp.local.Contact
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.contactapp.R

@Composable
fun ContactListScreen(navController: NavController, viewModel: ContactViewModel = hiltViewModel()) {
    val contacts = viewModel.contacts.collectAsState().value
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("AddEditUser/-1") // -1 yangi kontaktni bildiradi
                },
                contentColor = contentColorFor(backgroundColor = Color.Green)
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Qidiruv maydoni
            androidx.compose.material.OutlinedTextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    viewModel.updateSearchQuery(query) // Qidiruv so'rovini yangilash
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.LightGray
                ),
                shape = RoundedCornerShape(15.dp)
            )

            LazyColumn {
                items(contacts) { contact ->
                    ContactItem(contact = contact, onClick = {
                        navController.navigate("User/${contact.id}")
                    })
                }
            }
        }
    }
}


@Composable
fun ContactItem(contact: Contact, onClick: () -> Unit) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() } // Interaction Source

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable (
                interactionSource = interactionSource, // Interaction Source bilan ishlash
                indication = null,
                onClick = {onClick()}
                ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = R.drawable.user), contentDescription ="", modifier = Modifier.size(60.dp) )
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text(text = contact.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = contact.phoneNumber, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(painter = painterResource(id = R.drawable.phone), contentDescription ="", modifier = Modifier.size(30.dp)
            .clickable (
                interactionSource = interactionSource, // Interaction Source bilan ishlash
                indication = null,
                onClick = {
                    contact?.phoneNumber?.let { phoneNumber ->
                        // Telefon qilish intenti
                        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                        context.startActivity(callIntent)
                    }
                }
                )
        )

    }
}
