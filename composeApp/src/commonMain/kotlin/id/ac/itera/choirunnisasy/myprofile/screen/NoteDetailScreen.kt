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
import id.ac.itera.choirunnisasy.myprofile.*
import id.ac.itera.choirunnisasy.myprofile.viewmodel.NoteViewModel

@Composable
fun NoteDetailScreen(
    noteId: Int,
    isDark: Boolean,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
    viewModel: NoteViewModel
) {
    val noteFlow = remember(noteId) { viewModel.getNoteById(noteId) }
    val note by noteFlow.collectAsState(initial = null)

    if (note == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = strawberry)
        }
        return
    }

    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { contentVisible = true }

    val bgColor = if (isDark) darkBg else cream

    Scaffold(
        containerColor = bgColor,
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
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = "Detail Catatan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { onEdit(noteId) }) {
                        Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = Color.White)
                    }
                    IconButton(onClick = {
                        viewModel.deleteNote(noteId)
                        onBack()
                    }) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = Color.White.copy(alpha = 0.8f))
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
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { -30 }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isDark) darkCard else warmWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(matchaPale),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = note?.emoji ?: "📝", fontSize = 36.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = note?.title ?: "",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isDark) darkText else charcoal
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (note?.isFavorite == true) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(strawberry.copy(alpha = 0.1f))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Rounded.Favorite, contentDescription = null, tint = strawberry, modifier = Modifier.size(14.dp))
                                Text(text = "Favorit", fontSize = 12.sp, color = strawberry, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(500, delayMillis = 200)) + slideInVertically(tween(500, delayMillis = 200)) { 30 }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isDark) darkCard else warmWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.width(4.dp).height(20.dp).clip(RoundedCornerShape(4.dp)).background(matcha))
                            Text("ISI CATATAN", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = matcha, letterSpacing = 1.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = note?.content ?: "", fontSize = 15.sp, color = if (isDark) darkText else charcoal, lineHeight = 24.sp)
                    }
                }
            }
        }
    }
}
