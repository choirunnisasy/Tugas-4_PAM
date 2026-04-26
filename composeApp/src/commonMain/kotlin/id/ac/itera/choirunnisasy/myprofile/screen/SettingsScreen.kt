package id.ac.itera.choirunnisasy.myprofile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.itera.choirunnisasy.myprofile.*
import id.ac.itera.choirunnisasy.myprofile.data.SortOrder
import id.ac.itera.choirunnisasy.myprofile.data.ThemeConfig
import id.ac.itera.choirunnisasy.myprofile.viewmodel.SettingsViewModel
import dev.icerock.moko.permissions.compose.BindEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    BindEffect(viewModel.permissionsController)

    val themeConfig by viewModel.themeConfig.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val isDark = when (themeConfig) {
        ThemeConfig.LIGHT -> false
        ThemeConfig.DARK -> true
        ThemeConfig.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) darkSurface else matcha,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = if (isDark) darkBg else cream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            SettingsSection(title = "Appearance", isDark = isDark) {
                SettingsDropdownItem(
                    label = "Theme",
                    icon = Icons.Rounded.Palette,
                    currentValue = themeConfig.name,
                    options = ThemeConfig.entries.map { it.name },
                    onSelected = { viewModel.setThemeConfig(ThemeConfig.valueOf(it)) },
                    isDark = isDark
                )
            }

            SettingsSection(title = "Notes Preferences", isDark = isDark) {
                SettingsDropdownItem(
                    label = "Sort Order",
                    icon = Icons.Rounded.Sort,
                    currentValue = sortOrder.name,
                    options = SortOrder.entries.map { it.name },
                    onSelected = { viewModel.setSortOrder(SortOrder.valueOf(it)) },
                    isDark = isDark
                )
            }

            SettingsSection(title = "Permissions Test", isDark = isDark) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = (if (isDark) matchaDeep else matchaLight).copy(alpha = 0.2f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.CameraAlt,
                                    contentDescription = null,
                                    tint = if (isDark) matchaLight else matcha,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Text(
                            "Camera Permission",
                            color = if (isDark) darkText else charcoal,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Button(
                        onClick = { viewModel.requestCameraPermission() },
                        colors = ButtonDefaults.buttonColors(containerColor = strawberry),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Request", color = Color.White, fontSize = 13.sp)
                    }
                }
            }

            // --- Device Information Card ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) darkCard else warmWhite
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    (if (isDark) matchaDeep else matcha).copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = (if (isDark) matchaLight else matcha).copy(alpha = 0.1f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.Info,
                                contentDescription = null,
                                tint = if (isDark) matchaLight else matcha,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    
                    Text(
                        text = "Device Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) matchaLight else matchaDeep
                    )
                    
                    Spacer(Modifier.height(12.dp))
                    
                    InfoRow(
                        icon = Icons.Rounded.PhoneAndroid,
                        label = "OS Name",
                        value = viewModel.deviceName,
                        isDark = isDark
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 0.5.dp,
                        color = (if (isDark) darkSubtext else Color.LightGray).copy(alpha = 0.3f)
                    )
                    
                    InfoRow(
                        icon = Icons.Rounded.BatteryChargingFull,
                        label = "Battery",
                        value = "${viewModel.batteryLevel}% ${if (viewModel.isCharging) "(Charging)" else ""}",
                        isDark = isDark
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 0.5.dp,
                        color = (if (isDark) darkSubtext else Color.LightGray).copy(alpha = 0.3f)
                    )
                    
                    InfoRow(
                        icon = Icons.Rounded.Numbers,
                        label = "App Version",
                        value = viewModel.appVersion,
                        isDark = isDark
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String, isDark: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                tint = (if (isDark) darkSubtext else Color.Gray).copy(alpha = 0.8f),
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                color = if (isDark) darkSubtext else Color.Gray
            )
        }
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDark) darkText else charcoal
        )
    }
}

@Composable
fun SettingsSection(title: String, isDark: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) matchaLight.copy(alpha = 0.8f) else matchaDeep.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = if (isDark) darkCard else warmWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsDropdownItem(
    label: String,
    icon: ImageVector,
    currentValue: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    isDark: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = (if (isDark) matchaDeep else matchaLight).copy(alpha = 0.2f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon, 
                        contentDescription = null, 
                        tint = if (isDark) matchaLight else matcha, 
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            Text(
                label, 
                color = if (isDark) darkText else charcoal, 
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }

        Box {
            Surface(
                onClick = { expanded = true },
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(currentValue, color = strawberry, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Icon(Icons.Rounded.ArrowDropDown, contentDescription = null, tint = strawberry)
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(if (isDark) darkCard else warmWhite)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, color = if (isDark) darkText else charcoal) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
