package com.example.contactapp.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.contactapp.ContactViewModel
import com.example.contactapp.R

@Composable
fun User(
    navController: NavController,
    contactId: Int,
    viewModel: ContactViewModel = hiltViewModel()
) {
    val contacts by viewModel.contacts.collectAsState()
    val contact = contacts.find { it.id == contactId }
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() } // Interaction Source

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.baseline_keyboard_backspace_24),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.baseline_delete_24),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(30.dp)
                        .clickable {
                            contact?.let {
                                viewModel.deleteContact(it)
                                navController.popBackStack()
                            }
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.baseline_mode_edit_outline_24),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(30.dp)
                        .clickable {
                            navController.navigate("AddEditUser/${contactId}")
                        }
                )
            }
            Image(painter = painterResource(id = R.drawable.user), contentDescription = "")
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = contact?.name ?: "No Name",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = contact?.phoneNumber ?: "No Number",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.baseline_textsms_24),
                    contentDescription = "Send SMS",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            contact?.phoneNumber?.let { phoneNumber ->
                                // SMS yuborish intenti
                                val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$phoneNumber"))
                                context.startActivity(smsIntent)
                            }
                        }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.baseline_call_24),
                    contentDescription = "Call",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable (
                            interactionSource = interactionSource,
                            indication = null,
                           onClick = {
                               contact?.phoneNumber?.let { phoneNumber ->
                                   // Telefon qilish intenti
                                   val callIntent =
                                       Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                                   context.startActivity(callIntent)
                               }
                           }
                        )
                )
            }
        }
    }
}