package id.ac.itera.choirunnisasy.myprofile.screen

import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun NoteDetailScreen(
    noteId    : Int,
    isDark    : Boolean = false,
    onBack    : () -> Unit,
    onEdit    : (Int) -> Unit,
    viewModel : NoteViewModel = viewModel()
) {
    val note = viewModel.getNoteById(noteId)

    // Jika note tidak ditemukan
    if (note == null) {
        Box(
            modifier         = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "😕", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text       = "Catatan tidak ditemukan",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onBack,
                    colors  = ButtonDefaults.buttonColors(containerColor = matcha)
                ) {
                    Text("Kembali", color = Color.White)
                }
            }
        }
        return
    }

    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { contentVisible = true }

    val bgColor = if (isDark) darkBg else cream

    Scaffold(
        containerColor = bgColor,
        topBar = {
            // Top Bar
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
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector        = Icons.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint               = Color.White
                        )
                    }

                    Text(
                        text       = "Detail Catatan",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White,
                        modifier   = Modifier.weight(1f)
                    )

                    // Edit button
                    IconButton(onClick = { onEdit(noteId) }) {
                        Icon(
                            imageVector        = Icons.Rounded.Edit,
                            contentDescription = "Edit",
                            tint               = Color.White
                        )
                    }

                    // Delete button
                    IconButton(onClick = {
                        viewModel.deleteNote(noteId)
                        onBack()
                    }) {
                        Icon(
                            imageVector        = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            tint               = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // ── Emoji & Title ─────────────────────────────────────
            AnimatedVisibility(
                visible = contentVisible,
                enter   = fadeIn(tween(400)) + slideInVertically(tween(400)) { -30 }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(24.dp),
                    colors   = CardDefaults.cardColors(
                        containerColor = if (isDark) darkCard else warmWhite
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Emoji
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(matchaPale),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = note.emoji, fontSize = 36.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Title
                        Text(
                            text       = note.title,
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Black,
                            color      = if (isDark) darkText else charcoal
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Favorite badge
                        if (note.isFavorite) {
                            Row(
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(strawberry.copy(alpha = 0.1f))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Favorite,
                                    contentDescription = null,
                                    tint     = strawberry,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text     = "Favorit",
                                    fontSize = 12.sp,
                                    color    = strawberry,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Content ───────────────────────────────────────────
            AnimatedVisibility(
                visible = contentVisible,
                enter   = fadeIn(tween(500, delayMillis = 200)) +
                        slideInVertically(tween(500, delayMillis = 200)) { 30 }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(20.dp),
                    colors   = CardDefaults.cardColors(
                        containerColor = if (isDark) darkCard else warmWhite
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(20.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(matcha)
                            )
                            Text(
                                text       = "ISI CATATAN",
                                fontSize   = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = matcha,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text       = note.content,
                            fontSize   = 15.sp,
                            color      = if (isDark) darkText else charcoal,
                            lineHeight = 24.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Note ID info ──────────────────────────────────────
            AnimatedVisibility(
                visible = contentVisible,
                enter   = fadeIn(tween(500, delayMillis = 300))
            ) {
                Text(
                    text     = "Note ID: $noteId",
                    fontSize = 11.sp,
                    color    = if (isDark) darkSubtext else Color(0xFFAAAAAA),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}