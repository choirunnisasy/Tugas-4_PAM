package id.ac.itera.choirunnisasy.myprofile.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

// 🍓 IMPORT MANUAL (PASTIKAN JALURNYA BENAR)
import id.ac.itera.choirunnisasy.myprofile.ProfileScreen
import id.ac.itera.choirunnisasy.myprofile.ui.*
import id.ac.itera.choirunnisasy.myprofile.screen.*
import id.ac.itera.choirunnisasy.myprofile.viewmodel.NoteViewModel
import id.ac.itera.choirunnisasy.myprofile.viewmodel.ProfileViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val profileViewModel: ProfileViewModel = viewModel()
    val noteViewModel: NoteViewModel = viewModel()
    val newsViewModel: NewsViewModel = viewModel()

    val profileUiState by profileViewModel.uiState.collectAsState()
    val isDark = profileUiState.isDarkMode

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
            composable(Screen.Notes.route) { NoteListScreen(isDark, { navController.navigate(Screen.NoteDetail.createRoute(it)) }, { navController.navigate(Screen.AddNote.route) }, noteViewModel) }
            composable(Screen.Favorites.route) { FavoritesScreen(isDark, { navController.navigate(Screen.NoteDetail.createRoute(it)) }, noteViewModel) }
            composable(Screen.Profile.route) { ProfileScreenWrapper(profileViewModel, isDark) { navController.navigate(Screen.NewsList.route) } }

            // 🍓 RUTE NEWS
            composable(Screen.NewsList.route) { NewsListScreen(newsViewModel, isDark, { navController.popBackStack() }) { id -> navController.navigate(Screen.NewsDetail.createRoute(id)) } }
            composable(Screen.NewsDetail.route, listOf(navArgument("articleId") { type = NavType.IntType })) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("articleId") ?: 0
                NewsDetailScreen(id, newsViewModel, isDark) { navController.popBackStack() }
            }

            // Rute Lainnya
            composable(Screen.NoteDetail.route, listOf(navArgument("noteId") { type = NavType.IntType })) { NoteDetailScreen(it.arguments?.getInt("noteId") ?: 0, isDark, { navController.popBackStack() }, { id -> navController.navigate(Screen.EditNote.createRoute(id)) }, noteViewModel) }
            composable(Screen.AddNote.route) { AddNoteScreen(isDark, { navController.popBackStack() }, noteViewModel) }
            composable(Screen.EditNote.route, listOf(navArgument("noteId") { type = NavType.IntType })) { EditNoteScreen(it.arguments?.getInt("noteId") ?: 0, isDark, { navController.popBackStack() }, noteViewModel) }
        }
    }
}

@Composable
fun ProfileScreenWrapper(profileViewModel: ProfileViewModel, isDark: Boolean, onNewsClick: () -> Unit) {
    val uiState by profileViewModel.uiState.collectAsState()
    AnimatedContent(targetState = uiState.isEditMode) { isEditing ->
        if (isEditing) EditProfileScreen(uiState, { profileViewModel.onNameChange(it) }, { profileViewModel.onBioChange(it) }, { profileViewModel.onEmailChange(it) }, { profileViewModel.onPhoneChange(it) }, { profileViewModel.onLocationChange(it) }, { profileViewModel.saveProfile() }, { profileViewModel.cancelEdit() })
        else ProfileScreen(uiState, { profileViewModel.enterEditMode() }, { profileViewModel.toggleDarkMode() }, onNewsClick)
    }
}