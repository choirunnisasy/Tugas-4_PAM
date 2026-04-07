package id.ac.itera.choirunnisasy.myprofile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

// Pilihan emoji untuk note
private val emojiOptions = listOf("📝", "🍵", "🍓", "💡", "📋", "⭐", "🌿", "💻", "🎯", "📌")

@Composable
fun AddNoteScreen(
    isDark    : Boolean = false,
    onBack    : () -> Unit,
    viewModel : NoteViewModel = viewModel()
) {
    var title   by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("📝") }

    val bgColor   = if (isDark) darkBg   else cream
    val textColor = if (isDark) darkText else charcoal
    val cardColor = if (isDark) darkCard else warmWhite

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
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back / Cancel button
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector        = Icons.Rounded.Close,
                            contentDescription = "Cancel",
                            tint               = Color.White
                        )
                    }

                    Text(
                        text       = "Catatan Baru",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White,
                        modifier   = Modifier.weight(1f)
                    )

                    // Save button di top bar
                    TextButton(
                        onClick  = {
                            if (title.isNotBlank()) {
                                viewModel.addNote(
                                    title   = title.trim(),
                                    content = content.trim(),
                                    emoji   = selectedEmoji
                                )
                                onBack()
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text(
                            text       = "Simpan",
                            color      = if (title.isNotBlank())
                                Color.White
                            else
                                Color.White.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Pilih Emoji ───────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(20.dp),
                colors   = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text          = "PILIH EMOJI",
                        fontSize      = 11.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = matcha,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        emojiOptions.take(5).forEach { emoji ->
                            EmojiOption(
                                emoji      = emoji,
                                isSelected = selectedEmoji == emoji,
                                onClick    = { selectedEmoji = emoji }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        emojiOptions.drop(5).forEach { emoji ->
                            EmojiOption(
                                emoji      = emoji,
                                isSelected = selectedEmoji == emoji,
                                onClick    = { selectedEmoji = emoji }
                            )
                        }
                    }
                }
            }

            // ── Judul ─────────────────────────────────────────────
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text          = "JUDUL",
                        fontSize      = 11.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = matcha,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value         = title,
                        onValueChange = { title = it },
                        placeholder   = { Text("Judul catatan...", color = Color(0xFFAAAAAA)) },
                        modifier      = Modifier.fillMaxWidth(),
                        singleLine    = true,
                        shape         = RoundedCornerShape(12.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor      = matcha,
                            unfocusedBorderColor    = matchaLight.copy(alpha = 0.5f),
                            focusedTextColor        = textColor,
                            unfocusedTextColor      = textColor,
                            focusedContainerColor   = if (isDark) darkBg else Color(0xFFF8F4EF),
                            unfocusedContainerColor = if (isDark) darkBg else Color(0xFFF8F4EF)
                        )
                    )
                }
            }

            // ── Isi Catatan ───────────────────────────────────────
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text          = "ISI CATATAN",
                        fontSize      = 11.sp,
                        fontWeight    = FontWeight.SemiBold,
                        color         = matcha,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value         = content,
                        onValueChange = { content = it },
                        placeholder   = { Text("Tulis catatan kamu di sini...", color = Color(0xFFAAAAAA)) },
                        modifier      = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp),
                        singleLine    = false,
                        minLines      = 5,
                        shape         = RoundedCornerShape(12.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor      = matcha,
                            unfocusedBorderColor    = matchaLight.copy(alpha = 0.5f),
                            focusedTextColor        = textColor,
                            unfocusedTextColor      = textColor,
                            focusedContainerColor   = if (isDark) darkBg else Color(0xFFF8F4EF),
                            unfocusedContainerColor = if (isDark) darkBg else Color(0xFFF8F4EF)
                        )
                    )
                }
            }

            // ── Save Button ───────────────────────────────────────
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.addNote(
                            title   = title.trim(),
                            content = content.trim(),
                            emoji   = selectedEmoji
                        )
                        onBack()
                    }
                },
                enabled        = title.isNotBlank(),
                modifier       = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape          = RoundedCornerShape(18.dp),
                colors         = ButtonDefaults.buttonColors(
                    containerColor         = matcha,
                    disabledContainerColor = matchaLight.copy(alpha = 0.4f)
                )
            ) {
                Icon(
                    imageVector        = Icons.Rounded.Check,
                    contentDescription = null,
                    modifier           = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text       = "Simpan Catatan",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── EmojiOption ───────────────────────────────────────────────────────────────
@Composable
fun EmojiOption(
    emoji      : String,
    isSelected : Boolean,
    onClick    : () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) matchaPale
                else Color(0xFFF0F0F0)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(matchaLight.copy(alpha = 0.3f))
            )
        }
        Text(text = emoji, fontSize = 20.sp)
    }
}