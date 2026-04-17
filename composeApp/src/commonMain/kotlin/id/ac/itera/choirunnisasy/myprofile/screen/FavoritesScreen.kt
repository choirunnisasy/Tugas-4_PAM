package id.ac.itera.choirunnisasy.myprofile.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
private val strawberryPale = Color(0xFFFDECEA)
private val cream       = Color(0xFFFAF6F0)
private val warmWhite   = Color(0xFFFFFDF9)
private val charcoal    = Color(0xFF1A1A1A)
private val darkBg      = Color(0xFF1A1F14)
private val darkCard    = Color(0xFF2E3822)
private val darkText    = Color(0xFFE8F0D8)
private val darkSubtext = Color(0xFFA8B898)

@Composable
fun FavoritesScreen(
    isDark      : Boolean = false,
    onNoteClick : (Int) -> Unit,
    viewModel   : NoteViewModel = viewModel()
) {
    val uiState      by viewModel.uiState.collectAsState()
    val favoriteNotes = uiState.notes.filter { it.isFavorite }

    val bgColor = if (isDark) darkBg else cream

    Scaffold(
        containerColor = bgColor
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
                            colors = listOf(
                                strawberry,
                                Color(0xFFA93226)
                            )
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Text(
                        text       = "Favorites ❤️",
                        fontSize   = 26.sp,
                        fontWeight = FontWeight.Black,
                        color      = Color.White
                    )
                    Text(
                        text     = "${favoriteNotes.size} catatan favorit",
                        fontSize = 13.sp,
                        color    = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            // ── Content ───────────────────────────────────────────
            if (favoriteNotes.isEmpty()) {
                // Empty state
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🍓", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text       = "Belum ada favorit",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = if (isDark) darkSubtext else Color(0xFF999999)
                        )
                        Text(
                            text     = "Tap ❤️ pada catatan untuk menambahkan",
                            fontSize = 13.sp,
                            color    = if (isDark) darkSubtext else Color(0xFFAAAAAA)
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start  = 16.dp,
                        end    = 16.dp,
                        top    = 12.dp,
                        bottom = 80.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(favoriteNotes, key = { it.id }) { note ->
                        AnimatedVisibility(
                            visible = true,
                            enter   = fadeIn(tween(300)) +
                                    slideInVertically(tween(300))
                        ) {
                            FavoriteNoteCard(
                                note      = note,
                                isDark    = isDark,
                                onClick   = { onNoteClick(note.id) },
                                onRemove  = { viewModel.toggleFavorite(note.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── FavoriteNoteCard ──────────────────────────────────────────────────────────
@Composable
fun FavoriteNoteCard(
    note     : Note,
    isDark   : Boolean,
    onClick  : () -> Unit,
    onRemove : () -> Unit
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
        // Accent top bar merah
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(strawberry, Color(0xFFE8766D))
                    )
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Emoji
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(strawberryPale),
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

            // Remove from favorite
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector        = Icons.Rounded.Favorite,
                    contentDescription = "Remove from favorites",
                    tint               = strawberry
                )
            }
        }
    }
}