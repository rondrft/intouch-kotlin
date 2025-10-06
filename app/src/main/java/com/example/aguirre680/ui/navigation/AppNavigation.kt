package com.example.aguirre680.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aguirre680.ui.screens.login.LoginScreen
import com.example.aguirre680.ui.screens.contacts.ContactListScreen
import com.example.aguirre680.ui.screens.newcontact.NewContactScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object ContactList : Screen("contact_list")
    object NewContact : Screen("new_contact")
    object ContactDetail : Screen("contact_detail/{contactId}") {
        fun createRoute(contactId: String) = "contact_detail/$contactId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.ContactList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.ContactList.route) {
            ContactListScreen(
                onNavigateToNewContact = {
                    navController.navigate(Screen.NewContact.route)
                },
                onContactClick = { contactId ->
                    navController.navigate(Screen.ContactDetail.createRoute(contactId))
                },
                showWelcomeMessage = true
            )
        }

        composable(route = Screen.NewContact.route) {
            NewContactScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContactSaved = {
                    // Despues voy a agregar un Snackbar
                }
            )
        }

        composable(route = Screen.ContactDetail.route) {
            ContactDetailPlaceholder(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailPlaceholder(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Contacto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Detalle del Contacto\n(Pendiente)",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}