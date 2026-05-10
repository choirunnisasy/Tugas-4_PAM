package id.ac.itera.choirunnisasy.myprofile.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.itera.choirunnisasy.myprofile.*
import id.ac.itera.choirunnisasy.myprofile.data.Note
import id.ac.itera.choirunnisasy.myprofile.viewmodel.NoteViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    isDark: Boolean,
    onNoteClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: NoteViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredNotes = uiState.notes

    val bgColor = if (isDark) darkBg else cream
    val textColor = if (isDark) darkText else charcoal

    Scaffold(
        containerColor = bgColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = if (isDark)
                                listOf(Color(0xFF1E2D14), Color(0xFF2E4020))
                            else
                                listOf(matchaDeep, matcha)
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "My Notes 📝",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "${filteredNotes.size} catatan",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onSettingsClick) {
                            Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    windowInsets = TopAppBarDefaults.windowInsets
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            NetworkStatusIndicator()

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchChange(it) },
                placeholder = { Text("Cari catatan...", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Rounded.Search, contentDescription = null, tint = matcha)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("search_bar"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = matcha,
                    unfocusedBorderColor = matchaLight.copy(alpha = 0.5f),
                    focusedContainerColor = if (isDark) darkCard else warmWhite,
                    unfocusedContainerColor = if (isDark) darkCard else warmWhite,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                singleLine = true
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize().testTag("loading_indicator"), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = strawberry)
                }
            } else if (filteredNotes.isEmpty()) {
                EmptyNotesState(isDark)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize().testTag("notes_list")
                ) {
                    items(filteredNotes, key = { it.id }) { note ->
                        NoteCard(
                            note = note,
                            isDark = isDark,
                            onClick = { onNoteClick(note.id) },
                            onFavorite = { viewModel.toggleFavorite(note.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyNotesState(isDark: Boolean) {
    Box(modifier = Modifier.fillMaxSize().testTag("empty_state"), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🍵", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Belum ada catatan",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDark) darkSubtext else Color(0xFF999999)
            )
        }
    }
}

@Composable
fun NoteCard(note: Note, isDark: Boolean, onClick: () -> Unit, onFavorite: () -> Unit) {
    val cardColor = if (isDark) darkCard else warmWhite
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.testTag("note_item_${note.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(matchaPale),
                contentAlignment = Alignment.Center
            ) { Text(text = note.emoji, fontSize = 22.sp) }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = if (isDark) darkText else charcoal, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = note.content, fontSize = 13.sp, color = if (isDark) darkSubtext else Color(0xFF888888), maxLines = 2, overflow = TextOverflow.Ellipsis)
            }

            IconButton(onClick = onFavorite, modifier = Modifier.testTag("favorite_button_${note.id}")) {
                Icon(
                    imageVector = if (note.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = null,
                    tint = if (note.isFavorite) strawberry else Color(0xFFCCCCCC)
                )
            }
        }
    }
}
