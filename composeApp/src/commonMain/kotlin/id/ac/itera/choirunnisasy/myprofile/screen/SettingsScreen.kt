package id.ac.itera.choirunnisasy.myprofile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
        }
    }
}

@Composable
fun SettingsSection(title: String, isDark: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) matchaLight else matchaDeep,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = if (isDark) darkCard else warmWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
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
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isDark) matchaLight else matcha, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Text(label, color = if (isDark) darkText else charcoal, fontWeight = FontWeight.Medium)
        }

        Box {
            TextButton(onClick = { expanded = true }) {
                Text(currentValue, color = strawberry, fontWeight = FontWeight.Bold)
                Icon(Icons.Rounded.ArrowDropDown, contentDescription = null, tint = strawberry)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(if (isDark) darkCard else warmWhite)
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
