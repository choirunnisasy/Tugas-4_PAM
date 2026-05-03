package id.ac.itera.choirunnisasy.myprofile.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.itera.choirunnisasy.myprofile.*
import id.ac.itera.choirunnisasy.myprofile.data.ChatMessage
import id.ac.itera.choirunnisasy.myprofile.data.MessageRole
import id.ac.itera.choirunnisasy.myprofile.viewmodel.ChatViewModel
import id.ac.itera.choirunnisasy.myprofile.viewmodel.NoteViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onBack: () -> Unit,
    viewModel: ChatViewModel = koinViewModel(),
    noteViewModel: NoteViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        containerColor = cream,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0), // Full screen control
        topBar = {
            // TopAppBar handles status bar automatically with its windowInsets
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🍵", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Matcha-chan 🍓",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Siap membantumu!",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearChat() }) {
                        Icon(Icons.Default.DeleteSweep, "Clear", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = matcha,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                windowInsets = TopAppBarDefaults.windowInsets // Memastikan matcha background menutupi status bar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()) // Hanya ambil padding atas dari scaffold
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, matchaPale.copy(alpha = 0.2f))
                    )
                )
                .imePadding() // Menangani keyboard naik
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(messages) { message ->
                    ChatBubble(
                        message = message,
                        onSaveNote = { text ->
                            noteViewModel.addNote("Dari Matcha-chan", text, "🍵")
                            scope.launch {
                                snackbarHostState.showSnackbar("Disimpan ke Catatan! 🍓")
                            }
                        }
                    )
                }
                if (uiState.isLoading) {
                    item { TypingIndicator() }
                }
            }

            ChatInputArea(
                text = uiState.inputText,
                onTextChange = viewModel::onInputChange,
                onSend = viewModel::sendMessage,
                isLoading = uiState.isLoading
            )
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    onSaveNote: (String) -> Unit
) {
    val isUser = message.role == MessageRole.USER
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (isUser) matchaPale else Color.White
    val shape = if (isUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            if (!isUser && showMenu && message.text.isNotBlank()) {
                IconButton(
                    onClick = { onSaveNote(message.text); showMenu = false },
                    modifier = Modifier.size(32.dp).background(matchaLight.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(Icons.Default.NoteAdd, "Save", tint = matchaDeep, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .shadow(if (isUser) 1.dp else 2.dp, shape)
                    .clip(shape)
                    .background(bgColor)
                    .clickable { if (message.text.isNotBlank()) showMenu = !showMenu }
                    .padding(12.dp)
            ) {
                if (!isUser) {
                    Text("Matcha-chan", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = matcha)
                }
                Text(
                    text = when {
                        message.text.isNotBlank() -> message.text
                        message.isStreaming -> "Sedang mengetik... 🍓"
                        else -> "..." // Menghindari bubble hampa
                    },
                    color = if (message.isError) strawberry else charcoal,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
            }

            if (isUser && showMenu) {
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = { onSaveNote(message.text); showMenu = false },
                    modifier = Modifier.size(32.dp).background(matchaLight.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(Icons.Default.NoteAdd, "Save", tint = matchaDeep, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun ChatInputArea(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    isLoading: Boolean
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // Memastikan background tetap putih menutupi area tombol navigasi HP
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* TODO: Image Picker */ },
                modifier = Modifier.background(matchaPale, CircleShape).size(40.dp)
            ) {
                Icon(Icons.Default.Image, "Upload", tint = matcha)
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(24.dp)),
                placeholder = { Text("Ketik pesan... 🍓", fontSize = 14.sp) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = cream,
                    unfocusedContainerColor = cream,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onSend,
                enabled = !isLoading && text.isNotBlank(),
                modifier = Modifier
                    .background(if (text.isNotBlank()) matcha else matchaLight, CircleShape)
                    .size(44.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .padding(start = 12.dp)
            .background(Color.White, RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Matcha-chan berpikir", fontSize = 12.sp, color = Color.Gray)
        repeat(3) { index -> DotAnim(index) }
    }
}

@Composable
fun DotAnim(index: Int) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes { durationMillis = 600; 0.3f at index * 100; 1f at index * 100 + 200; 0.3f at index * 100 + 400 },
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(matcha.copy(alpha = alpha)))
}
