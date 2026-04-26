package id.ac.itera.choirunnisasy.myprofile.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import id.ac.itera.choirunnisasy.myprofile.ProfileScreen
import id.ac.itera.choirunnisasy.myprofile.ui.*
import id.ac.itera.choirunnisasy.myprofile.screen.*
import id.ac.itera.choirunnisasy.myprofile.viewmodel.*
import id.ac.itera.choirunnisasy.myprofile.data.*
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val profileViewModel: ProfileViewModel = koinViewModel()
    val noteViewModel: NoteViewModel = koinViewModel()
    val newsViewModel: NewsViewModel = koinViewModel()
    val settingsViewModel: SettingsViewModel = koinViewModel()

    val themeConfig by settingsViewModel.themeConfig.collectAsState(ThemeConfig.SYSTEM)
    val isDark = when (themeConfig) {
        ThemeConfig.LIGHT -> false
        ThemeConfig.DARK -> true
        ThemeConfig.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute in listOf(Screen.Notes.route, Screen.Favorites.route, Screen.Profile.route)) {
                NavigationBar(containerColor = if (isDark) Color(0xFF252D1C) else Color.White) {
                    NavigationBarItem(
                        selected = currentRoute == Screen.Notes.route,
                        onClick = { navController.navigate(Screen.Notes.route) { popUpTo(Screen.Notes.route); launchSingleTop = true } },
                        icon = { Icon(Icons.Rounded.EditNote, "Notes") },
                        label = { Text("Notes") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Favorites.route,
                        onClick = { navController.navigate(Screen.Favorites.route) { launchSingleTop = true } },
                        icon = { Icon(Icons.Rounded.Favorite, "Fav") },
                        label = { Text("Favorites") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Profile.route,
                        onClick = { navController.navigate(Screen.Profile.route) { launchSingleTop = true } },
                        icon = { Icon(Icons.Rounded.Person, "Profile") },
                        label = { Text("Profile") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController, Screen.Notes.route, Modifier.padding(padding)) {
            composable(Screen.Notes.route) {
                NoteListScreen(
                    isDark = isDark,
                    onNoteClick = { navController.navigate(Screen.NoteDetail.createRoute(it)) },
                    onAddClick = { navController.navigate(Screen.AddNote.route) },
                    onSettingsClick = { navController.navigate(Screen.Settings.route) },
                    viewModel = noteViewModel
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    isDark = isDark,
                    onNoteClick = { navController.navigate(Screen.NoteDetail.createRoute(it)) },
                    viewModel = noteViewModel
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreenWrapper(
                    profileViewModel = profileViewModel,
                    isDark = isDark,
                    onToggleDark = {
                        val newConfig = if (isDark) ThemeConfig.LIGHT else ThemeConfig.DARK
                        settingsViewModel.setThemeConfig(newConfig)
                    },
                    onNewsClick = { navController.navigate(Screen.NewsList.route) }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(settingsViewModel) {
                    navController.popBackStack()
                }
            }
            composable(Screen.NewsList.route) {
                NewsListScreen(newsViewModel, isDark, { navController.popBackStack() }) { id ->
                    navController.navigate(Screen.NewsDetail.createRoute(id))
                }
            }
            composable(
                Screen.NewsDetail.route,
                listOf(navArgument("articleId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("articleId") ?: 0
                NewsDetailScreen(id, newsViewModel, isDark) { navController.popBackStack() }
            }
            composable(
                Screen.NoteDetail.route,
                listOf(navArgument("noteId") { type = NavType.IntType })
            ) {
                NoteDetailScreen(
                    noteId = it.arguments?.getInt("noteId") ?: 0,
                    isDark = isDark,
                    onBack = { navController.popBackStack() },
                    onEdit = { id -> navController.navigate(Screen.EditNote.createRoute(id)) },
                    viewModel = noteViewModel
                )
            }
            composable(Screen.AddNote.route) {
                AddNoteScreen(isDark, { navController.popBackStack() }, noteViewModel)
            }
            composable(
                Screen.EditNote.route,
                listOf(navArgument("noteId") { type = NavType.IntType })
            ) {
                EditNoteScreen(
                    noteId = it.arguments?.getInt("noteId") ?: 0,
                    isDark = isDark,
                    onBack = { navController.popBackStack() },
                    viewModel = noteViewModel
                )
            }
        }
    }
}

@Composable
fun ProfileScreenWrapper(
    profileViewModel: ProfileViewModel,
    isDark: Boolean,
    onToggleDark: () -> Unit,
    onNewsClick: () -> Unit
) {
    val uiState by profileViewModel.uiState.collectAsState()
    AnimatedContent(targetState = uiState.isEditMode, label = "ProfileTransition") { isEditing ->
        if (isEditing) {
            EditProfileScreen(
                uiState = uiState,
                onNameChange = { profileViewModel.onNameChange(it) },
                onBioChange = { profileViewModel.onBioChange(it) },
                onEmailChange = { profileViewModel.onEmailChange(it) },
                onPhoneChange = { profileViewModel.onPhoneChange(it) },
                onLocationChange = { profileViewModel.onLocationChange(it) },
                onSave = { profileViewModel.saveProfile() },
                onCancel = { profileViewModel.cancelEdit() }
            )
        } else {
            ProfileScreen(
                uiState = uiState.copy(isDarkMode = isDark),
                onEditClick = { profileViewModel.enterEditMode() },
                onToggleDark = onToggleDark,
                onNewsClick = onNewsClick
            )
        }
    }
}
