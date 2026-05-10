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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.itera.choirunnisasy.myprofile.*
import id.ac.itera.choirunnisasy.myprofile.component.EmojiOption
import id.ac.itera.choirunnisasy.myprofile.viewmodel.NoteViewModel

private val emojiOptions = listOf("📝", "🍵", "🍓", "💡", "📋", "⭐", "🌿", "💻", "🎯", "📌")

@Composable
fun AddNoteScreen(
    isDark: Boolean,
    onBack: () -> Unit,
    viewModel: NoteViewModel
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("📝") }

    val bgColor = if (isDark) darkBg else cream
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
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.Close, contentDescription = "Cancel", tint = Color.White)
                    }
                    Text(
                        text = "Catatan Baru",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            if (title.isNotBlank()) {
                                viewModel.addNote(
                                    title = title.trim(),
                                    content = content.trim(),
                                    emoji = selectedEmoji
                                )
                                onBack()
                            }
                        },
                        enabled = title.isNotBlank(),
                        modifier = Modifier.testTag("save_button_top")
                    ) {
                        Text(
                            text = "Simpan",
                            color = if (title.isNotBlank()) Color.White else Color.White.copy(alpha = 0.4f),
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("PILIH EMOJI", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = matcha)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        emojiOptions.take(5).forEach { emoji ->
                            EmojiOption(emoji = emoji, isSelected = selectedEmoji == emoji, onClick = { selectedEmoji = emoji })
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        emojiOptions.drop(5).forEach { emoji ->
                            EmojiOption(emoji = emoji, isSelected = selectedEmoji == emoji, onClick = { selectedEmoji = emoji })
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("JUDUL", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = matcha)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Judul catatan...", color = Color(0xFFAAAAAA)) },
                        modifier = Modifier.fillMaxWidth().testTag("title_field"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = matcha,
                            unfocusedBorderColor = matchaLight.copy(alpha = 0.5f),
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            focusedContainerColor = if (isDark) darkBg else Color(0xFFF8F4EF),
                            unfocusedContainerColor = if (isDark) darkBg else Color(0xFFF8F4EF)
                        )
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ISI CATATAN", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = matcha)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        placeholder = { Text("Tulis catatan kamu di sini...", color = Color(0xFFAAAAAA)) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp).testTag("content_field"),
                        singleLine = false,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = matcha,
                            unfocusedBorderColor = matchaLight.copy(alpha = 0.5f),
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            focusedContainerColor = if (isDark) darkBg else Color(0xFFF8F4EF),
                            unfocusedContainerColor = if (isDark) darkBg else Color(0xFFF8F4EF)
                        )
                    )
                }
            }

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.addNote(
                            title = title.trim(),
                            content = content.trim(),
                            emoji = selectedEmoji
                        )
                        onBack()
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(56.dp).testTag("save_button"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = matcha)
            ) {
                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simpan Catatan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
