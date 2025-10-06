package com.example.aguirre680.ui.screens.contacts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aguirre680.data.model.Contact
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    onNavigateToNewContact: () -> Unit,
    onContactClick: (String) -> Unit,
    showWelcomeMessage: Boolean = false,
    viewModel: ContactListViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredContacts by viewModel.filteredContacts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    var isSearchExpanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()

    // Mostrar mensaje de login exitoso
    LaunchedEffect(Unit) {
        if (showWelcomeMessage) {
            viewModel.showSuccessMessage("Inicio de sesion exitoso")
            delay(3000)
            viewModel.clearSnackbarMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                ContactListTopBar(
                    searchQuery = searchQuery,
                    isSearchExpanded = isSearchExpanded,
                    onSearchExpandedChange = { isSearchExpanded = it },
                    onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                    onClearSearch = {
                        viewModel.clearSearch()
                        isSearchExpanded = false
                    },
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToNewContact,
                    containerColor = Color(0xFF805AD5),
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar contacto"
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTab) {
                    0 -> {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else if (filteredContacts.isEmpty()) {
                            EmptyContactsMessage(
                                hasSearchQuery = searchQuery.isNotBlank()
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(filteredContacts) { contact ->
                                    ContactListItem(
                                        contact = contact,
                                        onClick = { onContactClick(contact.id) },
                                        onCallClick = { }
                                    )
                                }
                            }
                        }
                    }
                    1 -> {
                        EmptyFavoritesMessage()
                    }
                }
            }
        }

        // Snackbar personalizado
        AnimatedVisibility(
            visible = snackbarMessage != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            snackbarMessage?.let { data ->
                CustomSnackbar(
                    message = data.message,
                    type = data.type,
                    onDismiss = { viewModel.clearSnackbarMessage() }
                )
            }
        }
    }
}

@Composable
fun CustomSnackbar(
    message: String,
    type: SnackbarType,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .widthIn(max = 340.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (type == SnackbarType.SUCCESS)
                    Icons.Default.CheckCircle
                else
                    Icons.Default.Close,
                contentDescription = null,
                tint = if (type == SnackbarType.SUCCESS)
                    Color(0xFF10B981)
                else
                    Color(0xFFEF4444),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = message,
                color = Color(0xFF2D3748),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListTopBar(
    searchQuery: String,
    isSearchExpanded: Boolean,
    onSearchExpandedChange: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF805AD5),
                shape = RoundedCornerShape(
                    bottomStart = 32.dp,
                    bottomEnd = 32.dp
                )
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                if (!isSearchExpanded) {
                    Text(
                        text = "Admin",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            if (isSearchExpanded) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    placeholder = {
                        Text(
                            text = "Buscar...",
                            color = Color.Gray.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )

                Spacer(modifier = Modifier.width(8.dp))
            }

            Row {
                IconButton(
                    onClick = {
                        if (isSearchExpanded) {
                            onClearSearch()
                        } else {
                            onSearchExpandedChange(true)
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = if (isSearchExpanded) "Cerrar busqueda" else "Buscar",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Mas opciones",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (selectedTab == 0) Color.White else Color.Transparent
                    )
                    .clickable { onTabSelected(0) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Contactos",
                    color = if (selectedTab == 0) Color(0xFF805AD5) else Color.White,
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (selectedTab == 1) Color.White else Color.Transparent
                    )
                    .clickable { onTabSelected(1) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Favoritos",
                    color = if (selectedTab == 1) Color(0xFF805AD5) else Color.White,
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ContactListItem(
    contact: Contact,
    onClick: () -> Unit,
    onCallClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactAvatar(
                initials = contact.getInitials()
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = contact.getFullName(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D3748),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = contact.phoneNumber,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = onCallClick
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Llamar",
                    tint = Color(0xFF805AD5)
                )
            }
        }
    }
}

@Composable
fun ContactAvatar(
    initials: String
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(Color(0xFF805AD5).copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        if (initials.isNotEmpty()) {
            Text(
                text = initials,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF805AD5)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = Color(0xFF805AD5),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun EmptyContactsMessage(
    hasSearchQuery: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (hasSearchQuery)
                Icons.Default.Search
            else
                Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.Gray.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (hasSearchQuery)
                "No se encontraron contactos"
            else
                "No hay contactos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (hasSearchQuery)
                "Intenta con otro termino de busqueda"
            else
                "Presiona el boton + para agregar un contacto",
            fontSize = 14.sp,
            color = Color.Gray.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun EmptyFavoritesMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.Gray.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No hay favoritos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Los contactos marcados como favoritos apareceran aca",
            fontSize = 14.sp,
            color = Color.Gray.copy(alpha = 0.7f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}