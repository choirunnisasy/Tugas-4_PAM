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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.ac.itera.choirunnisasy.myprofile.ProfileScreen
import id.ac.itera.choirunnisasy.myprofile.navigation.Screen
import id.ac.itera.choirunnisasy.myprofile.screen.AddNoteScreen
import id.ac.itera.choirunnisasy.myprofile.screen.EditNoteScreen
import id.ac.itera.choirunnisasy.myprofile.screen.FavoritesScreen
import id.ac.itera.choirunnisasy.myprofile.screen.NoteDetailScreen
import id.ac.itera.choirunnisasy.myprofile.screen.NoteListScreen
import id.ac.itera.choirunnisasy.myprofile.ui.*
import id.ac.itera.choirunnisasy.myprofile.viewmodel.NoteViewModel
import id.ac.itera.choirunnisasy.myprofile.viewmodel.ProfileViewModel

// ── COLORS ────────────────────────────────────────────────────────────────────
private val matchaDeep  = Color(0xFF3D5229)
private val matcha      = Color(0xFF5C7A3E)
private val matchaLight = Color(0xFFA8C57E)
private val strawberry  = Color(0xFFC0392B)
private val darkBg      = Color(0xFF1A1F14)
private val darkSurface = Color(0xFF252D1C)

// ── Bottom Nav Item Data Class ─────────────────────────────────────────────────
data class BottomNavItem(
    val screen      : Screen,
    val icon        : ImageVector,
    val iconSelected: ImageVector,
    val label       : String
)

// ── AppNavigation ──────────────────────────────────────────────────────────────
@Composable
fun AppNavigation() {
    val navController     = rememberNavController()
    val profileViewModel  : ProfileViewModel = viewModel()
    val noteViewModel     : NoteViewModel    = viewModel()

    val profileUiState by profileViewModel.uiState.collectAsState()
    val isDark = profileUiState.isDarkMode

    // Bottom nav items
    val bottomNavItems = listOf(
        BottomNavItem(
            screen       = Screen.Notes,
            icon         = Icons.Rounded.EditNote,
            iconSelected = Icons.Rounded.EditNote,
            label        = "Notes"
        ),
        BottomNavItem(
            screen       = Screen.Favorites,
            icon         = Icons.Rounded.FavoriteBorder,
            iconSelected = Icons.Rounded.Favorite,
            label        = "Favorites"
        ),
        BottomNavItem(
            screen       = Screen.Profile,
            icon         = Icons.Rounded.PersonOutline,
            iconSelected = Icons.Rounded.Person,
            label        = "Profile"
        )
    )

    // Screens yang tampilkan bottom nav
    val bottomNavRoutes = listOf(
        Screen.Notes.route,
        Screen.Favorites.route,
        Screen.Profile.route
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Warna Bottom Nav berdasarkan dark mode
    val navBarColor     = if (isDark) darkSurface else Color.White
    val navBarIndicator = if (isDark) matchaLight.copy(alpha = 0.2f) else matchaLight.copy(alpha = 0.3f)
    val navBarSelected  = if (isDark) matchaLight else matchaDeep
    val navBarUnselected = if (isDark) Color(0xFF6B7B5A) else Color(0xFF9E9E9E)

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            // Tampilkan bottom nav hanya di tab utama
            if (currentRoute in bottomNavRoutes) {
                NavigationBar(
                    containerColor = navBarColor,
                    tonalElevation = 8.dp,
                    modifier       = Modifier.height(64.dp)
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick  = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(Screen.Notes.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected)
                                        item.iconSelected
                                    else
                                        item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label  = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor   = navBarSelected,
                                selectedTextColor   = navBarSelected,
                                unselectedIconColor = navBarUnselected,
                                unselectedTextColor = navBarUnselected,
                                indicatorColor      = navBarIndicator
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Notes.route,
            modifier         = Modifier.padding(paddingValues)
        ) {
            // ── Notes Tab ─────────────────────────────────────────
            composable(
                route = Screen.Notes.route,
                enterTransition  = { fadeIn(tween(300)) },
                exitTransition   = { fadeOut(tween(300)) }
            ) {
                NoteListScreen(
                    isDark = isDark,
                    onNoteClick = { noteId ->
                        navController.navigate(Screen.NoteDetail.createRoute(noteId))
                    },
                    onAddClick = {
                        navController.navigate(Screen.AddNote.route)
                    },
                    viewModel = noteViewModel
                )
            }

            // ── Favorites Tab ─────────────────────────────────────
            composable(
                route = Screen.Favorites.route,
                enterTransition  = { fadeIn(tween(300)) },
                exitTransition   = { fadeOut(tween(300)) }
            ) {
                FavoritesScreen(
                    isDark = isDark,
                    onNoteClick = { noteId ->
                        navController.navigate(Screen.NoteDetail.createRoute(noteId))
                    },
                    viewModel = noteViewModel
                )
            }

            // ── Profile Tab ───────────────────────────────────────
            composable(
                route = Screen.Profile.route,
                enterTransition  = { fadeIn(tween(300)) },
                exitTransition   = { fadeOut(tween(300)) }
            ) {
                ProfileScreenWrapper(
                    profileViewModel = profileViewModel,
                    isDark           = isDark
                )
            }

            // ── Note Detail ───────────────────────────────────────
            composable(
                route     = Screen.NoteDetail.route,
                arguments = listOf(
                    navArgument("noteId") { type = NavType.IntType }
                ),
                enterTransition = {
                    slideInHorizontally(tween(400)) { it } + fadeIn(tween(400))
                },
                exitTransition = {
                    slideOutHorizontally(tween(400)) { it } + fadeOut(tween(400))
                }
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                NoteDetailScreen(
                    noteId = noteId,
                    isDark = isDark,
                    onBack = { navController.popBackStack() },
                    onEdit = { id ->
                        navController.navigate(
                            Screen.EditNote.createRoute(
                                id
                            )
                        )
                    },
                    viewModel = noteViewModel
                )
            }

            // ── Add Note ──────────────────────────────────────────
            composable(
                route = Screen.AddNote.route,
                enterTransition = {
                    slideInHorizontally(tween(400)) { it } + fadeIn(tween(400))
                },
                exitTransition = {
                    slideOutHorizontally(tween(400)) { it } + fadeOut(tween(400))
                }
            ) {
                AddNoteScreen(
                    isDark = isDark,
                    onBack = { navController.popBackStack() },
                    viewModel = noteViewModel
                )
            }

            // ── Edit Note ─────────────────────────────────────────
            composable(
                route     = Screen.EditNote.route,
                arguments = listOf(
                    navArgument("noteId") { type = NavType.IntType }
                ),
                enterTransition = {
                    slideInHorizontally(tween(400)) { it } + fadeIn(tween(400))
                },
                exitTransition = {
                    slideOutHorizontally(tween(400)) { it } + fadeOut(tween(400))
                }
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                EditNoteScreen(
                    noteId = noteId,
                    isDark = isDark,
                    onBack = { navController.popBackStack() },
                    viewModel = noteViewModel
                )
            }
        }
    }
}

// ── ProfileScreenWrapper ──────────────────────────────────────────────────────
// Wrapper untuk ProfileScreen agar bisa dipakai di NavHost
@Composable
fun ProfileScreenWrapper(
    profileViewModel : ProfileViewModel,
    isDark           : Boolean
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            snackbarHostState.showSnackbar("Profil berhasil disimpan!")
            profileViewModel.onSavedFeedbackShown()
        }
    }

    AnimatedContent(
        targetState  = uiState.isEditMode,
        transitionSpec = {
            if (targetState) {
                (slideInHorizontally(tween(400)) { it } +
                        fadeIn(tween(400))).togetherWith(
                    slideOutHorizontally(tween(400)) { -it } +
                            fadeOut(tween(400))
                )
            } else {
                (slideInHorizontally(tween(400)) { -it } +
                        fadeIn(tween(400))).togetherWith(
                    slideOutHorizontally(tween(400)) { it } +
                            fadeOut(tween(400))
                )
            }
        },
        label = "profileTransition"
    ) { isEditing ->
        if (isEditing) {
            EditProfileScreen(
                uiState          = uiState,
                onNameChange     = { profileViewModel.onNameChange(it) },
                onBioChange      = { profileViewModel.onBioChange(it) },
                onEmailChange    = { profileViewModel.onEmailChange(it) },
                onPhoneChange    = { profileViewModel.onPhoneChange(it) },
                onLocationChange = { profileViewModel.onLocationChange(it) },
                onSave           = { profileViewModel.saveProfile() },
                onCancel         = { profileViewModel.cancelEdit() }
            )
        } else {
            ProfileScreen(
                uiState = uiState,
                onEditClick = { profileViewModel.enterEditMode() },
                onToggleDark = { profileViewModel.toggleDarkMode() }
            )
        }
    }
}