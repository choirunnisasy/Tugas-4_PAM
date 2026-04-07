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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.ac.itera.choirunnisasy.myprofile.data.Note
import id.ac.itera.choirunnisasy.myprofile.viewmodel.NoteViewModel

// ── COLORS ────────────────────────────────────────────────────────────────────
private val matchaDeep  = Color(0xFF3D5229)
private val matcha      = Color(0xFF5C7A3E)
private val matchaLight = Color(0xFFA8C57E)
private val matchaPale  = Color(0xFFD4E8B8)
private val strawberry  = Color(0xFFC0392B)
private val cream       = Color(0xFFFAF6F0)
private val warmWhite   = Color(0xFFFFFDF9)
private val charcoal    = Color(0xFF1A1A1A)
private val darkBg      = Color(0xFF1A1F14)
private val darkCard    = Color(0xFF2E3822)
private val darkText    = Color(0xFFE8F0D8)
private val darkSubtext = Color(0xFFA8B898)

@Composable
fun NoteListScreen(
    isDark        : Boolean = false,
    onNoteClick   : (Int) -> Unit,
    onAddClick    : () -> Unit,
    viewModel     : NoteViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredNotes = viewModel.getFilteredNotes()

    val bgColor   = if (isDark) darkBg    else cream
    val textColor = if (isDark) darkText  else charcoal

    Scaffold(
        containerColor = bgColor,
        floatingActionButton = {
            // FAB untuk Add Note
            FloatingActionButton(
                onClick          = onAddClick,
                containerColor   = matcha,
                contentColor     = Color.White,
                shape            = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector        = Icons.Rounded.Add,
                    contentDescription = "Add Note"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Header ────────────────────────────────────────────
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
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Text(
                        text       = "My Notes 📝",
                        fontSize   = 26.sp,
                        fontWeight = FontWeight.Black,
                        color      = Color.White
                    )
                    Text(
                        text     = "${filteredNotes.size} catatan",
                        fontSize = 13.sp,
                        color    = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            // ── Search Bar ────────────────────────────────────────
            OutlinedTextField(
                value         = uiState.searchQuery,
                onValueChange = { viewModel.onSearchChange(it) },
                placeholder   = { Text("Cari catatan...", fontSize = 14.sp) },
                leadingIcon   = {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = null,
                        tint = matcha
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = matcha,
                    unfocusedBorderColor = matchaLight.copy(alpha = 0.5f),
                    focusedContainerColor   = if (isDark) darkCard else warmWhite,
                    unfocusedContainerColor = if (isDark) darkCard else warmWhite,
                    focusedTextColor     = textColor,
                    unfocusedTextColor   = textColor
                ),
                singleLine = true
            )

            // ── Note List ─────────────────────────────────────────
            if (filteredNotes.isEmpty()) {
                // Empty state
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🍵", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text       = "Belum ada catatan",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = if (isDark) darkSubtext else Color(0xFF999999)
                        )
                        Text(
                            text     = "Tap + untuk tambah catatan baru",
                            fontSize = 13.sp,
                            color    = if (isDark) darkSubtext else Color(0xFFAAAAAA)
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding    = PaddingValues(
                        start  = 16.dp,
                        end    = 16.dp,
                        bottom = 80.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredNotes, key = { it.id }) { note ->
                        AnimatedVisibility(
                            visible = true,
                            enter   = fadeIn(tween(300)) + slideInVertically(tween(300))
                        ) {
                            NoteCard(
                                note      = note,
                                isDark    = isDark,
                                onClick   = { onNoteClick(note.id) },
                                onFavorite = { viewModel.toggleFavorite(note.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── NoteCard ──────────────────────────────────────────────────────────────────
@Composable
fun NoteCard(
    note      : Note,
    isDark    : Boolean,
    onClick   : () -> Unit,
    onFavorite : () -> Unit
) {
    val cardColor = if (isDark) darkCard else warmWhite

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Emoji
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(matchaPale),
                contentAlignment = Alignment.Center
            ) {
                Text(text = note.emoji, fontSize = 22.sp)
            }

            // Title & Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = note.title,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color      = if (isDark) darkText else charcoal,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text     = note.content,
                    fontSize = 13.sp,
                    color    = if (isDark) darkSubtext else Color(0xFF888888),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Favorite button
            IconButton(onClick = onFavorite) {
                Icon(
                    imageVector = if (note.isFavorite)
                        Icons.Rounded.Favorite
                    else
                        Icons.Rounded.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (note.isFavorite) strawberry else Color(0xFFCCCCCC)
                )
            }
        }
    }
}