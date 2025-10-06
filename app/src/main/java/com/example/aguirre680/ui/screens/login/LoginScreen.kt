package com.example.aguirre680.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {

    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    // Estado para mostrar/ocultar contraseña
    var passwordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            onLoginSuccess()
        }
    }

    //Degradado lo busque en varios lados porq no sabia como hacerlo y tampoco lo encontraba
    // Colores para el degradado
    val gradientColors = listOf(
        Color(0xFF6B46C1), // Violeta oscuro
        Color(0xFF805AD5), // Violeta medio
        Color(0xFF9F7AEA), // Violeta claro
        Color(0xFF667EEA), // Azul-violeta
        Color(0xFF4299E1)  // Azul
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9F7AEA).copy(alpha = 0.6f), // Violeta
                        Color(0xFF805AD5).copy(alpha = 0.5f), // Violeta medio
                        Color(0xFF667EEA).copy(alpha = 0.4f), // Azul-violeta
                        Color(0xFF4299E1).copy(alpha = 0.3f), // Celeste
                        Color(0xFF63B3ED).copy(alpha = 0.2f), // Celeste claro
                        Color.White.copy(alpha = 0.5f),
                        Color.White
                    ),
                    startY = 0f,
                    endY = 900f
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Título de la app
            Text(
                text = "InTouch",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Campo de Usuario
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "USUARIO",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                TextField(
                    value = username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    placeholder = {
                        Text(
                            text = "rondrft",
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            spotColor = Color.Black.copy(alpha = 0.15f)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Gray.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Icono de usuario",
                            tint = Color(0xFF9F7AEA).copy(alpha = 0.7f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de Contraseña
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "CONTRASEÑA",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                TextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    placeholder = {
                        Text(
                            text = "XdxhE9jdof3",
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            spotColor = Color.Black.copy(alpha = 0.15f)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Gray.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.Check
                                    else
                                        Icons.Default.Close,
                                    contentDescription = if (passwordVisible)
                                        "Ocultar contraseña"
                                    else
                                        "Mostrar contraseña",
                                    tint = Color(0xFF9F7AEA).copy(alpha = 0.7f)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Icono de contraseña",
                                tint = Color(0xFF9F7AEA).copy(alpha = 0.7f),
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            viewModel.login()
                        }
                    ),
                    enabled = !isLoading
                )
            }

            // Mensaje de error
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage ?: "",
                    color = Color(0xFFE53E3E),
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Boton de Login
            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.login()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color(0xFF805AD5).copy(alpha = 0.4f)
                    ),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF805AD5),
                    disabledContainerColor = Color(0xFF805AD5).copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Iniciar Sesion",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Credenciales para testing
            Text(
                text = "Credenciales de prueba: admin / 1234",
                fontSize = 11.sp,
                color = Color.Gray.copy(alpha = 0.7f)
            )
        }
    }
}