package com.example.aguirre680.ui.screens.newcontact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactScreen(
    onNavigateBack: () -> Unit,
    onContactSaved: () -> Unit = {},
    viewModel: NewContactViewModel = viewModel()
) {
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val email by viewModel.email.collectAsState()
    val company by viewModel.company.collectAsState()
    val address by viewModel.address.collectAsState()

    val firstNameError by viewModel.firstNameError.collectAsState()
    val lastNameError by viewModel.lastNameError.collectAsState()
    val phoneNumberError by viewModel.phoneNumberError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()

    val saveSuccess by viewModel.saveSuccess.collectAsState()

    // Cuando se guarda exitosamente, vuelve atras
    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            onContactSaved()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            NewContactTopBar(
                onNavigateBack = onNavigateBack,
                onSaveClick = {
                    viewModel.saveContact()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Avatar con las iniciales
            ContactAvatarPreview(
                firstName = firstName,
                lastName = lastName
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "INFORMACION BASICA",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF805AD5),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactTextField(
                value = firstName,
                onValueChange = { viewModel.onFirstNameChange(it) },
                label = "Nombre",
                placeholder = "Juan",
                isError = firstNameError != null,
                errorMessage = firstNameError,
                isRequired = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactTextField(
                value = lastName,
                onValueChange = { viewModel.onLastNameChange(it) },
                label = "Apellido",
                placeholder = "Perez",
                isError = lastNameError != null,
                errorMessage = lastNameError,
                isRequired = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactTextField(
                value = phoneNumber,
                onValueChange = { viewModel.onPhoneNumberChange(it) },
                label = "Telefono",
                placeholder = "+54 11 1234-5678",
                keyboardType = KeyboardType.Phone,
                isError = phoneNumberError != null,
                errorMessage = phoneNumberError,
                isRequired = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "INFORMACION ADICIONAL (OPCIONAL)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = "Email",
                placeholder = "ejemplo@email.com",
                keyboardType = KeyboardType.Email,
                isError = emailError != null,
                errorMessage = emailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactTextField(
                value = company,
                onValueChange = { viewModel.onCompanyChange(it) },
                label = "Empresa",
                placeholder = "Tech Solutions"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactTextField(
                value = address,
                onValueChange = { viewModel.onAddressChange(it) },
                label = "Direccion",
                placeholder = "Av. Ejemplo 123, CABA",
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactTopBar(
    onNavigateBack: () -> Unit,
    onSaveClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Nuevo Contacto",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color(0xFF2D3748)
                )
            }
        },
        actions = {
            IconButton(onClick = onSaveClick) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Guardar contacto",
                    tint = Color(0xFF805AD5)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun ContactAvatarPreview(
    firstName: String,
    lastName: String
) {
    val initials = remember(firstName, lastName) {
        val firstInitial = firstName.firstOrNull()?.uppercaseChar() ?: ""
        val lastInitial = lastName.firstOrNull()?.uppercaseChar() ?: ""
        "$firstInitial$lastInitial"
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color(0xFF805AD5).copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        if (initials.isNotEmpty()) {
            Text(
                text = initials,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF805AD5)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = Color(0xFF805AD5),
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null,
    isRequired: Boolean = false,
    maxLines: Int = 1
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isError) MaterialTheme.colorScheme.error else Color(0xFF2D3748),
                letterSpacing = 0.5.sp
            )
            if (isRequired) {
                Text(
                    text = " *",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF805AD5)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray.copy(alpha = 0.5f),
                    fontSize = 16.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedBorderColor = Color(0xFF805AD5),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                errorBorderColor = MaterialTheme.colorScheme.error
            ),
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = maxLines == 1,
            maxLines = maxLines,
            shape = RoundedCornerShape(12.dp)
        )

        if (isError && errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}