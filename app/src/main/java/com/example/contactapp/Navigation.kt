package com.example.contactapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contactapp.ui.screen.AddEditContactScreen
import com.example.contactapp.ui.screen.ContactListScreen
import com.example.contactapp.ui.screen.User

@Composable
fun ContactAppNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "contact_list") {
        composable("contact_list") {
            ContactListScreen(navController)
        }
        composable("User/{contactId}") { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")?.toIntOrNull()
            if (contactId != null) {
                User(navController, contactId)
            }
        }
        composable("AddEditUser/{contactId}") { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")?.toIntOrNull()
            if (contactId != null) {
                AddEditContactScreen(navController, contactId)
            }
        }
    }
}
