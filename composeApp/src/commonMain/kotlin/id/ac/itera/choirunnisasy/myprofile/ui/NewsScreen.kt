package id.ac.itera.choirunnisasy.myprofile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import id.ac.itera.choirunnisasy.myprofile.*
import id.ac.itera.choirunnisasy.myprofile.data.Article
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox

// ── COLORS ────────────────────────────────────────────────────────────────────
private val matchaDeep  = Color(0xFF3D5229)
private val matcha      = Color(0xFF5C7A3E)
private val matchaLight = Color(0xFFA8C57E)
private val strawberry  = Color(0xFFC0392B)
private val cream       = Color(0xFFFAF6F0)
private val charcoal    = Color(0xFF1A1A1A)
private val darkBg      = Color(0xFF1A1F14)
private val darkSurface = Color(0xFF252D1C)
private val darkCard    = Color(0xFF2E3822)
private val darkText    = Color(0xFFE8F0D8)
private val darkSubtext = Color(0xFFA8B898)
private val warmWhite   = Color(0xFFFFFDF9)

// ─────────────────────────────────────────────────────────────────────────────
// NEWS LIST SCREEN
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    viewModel: NewsViewModel,
    isDark: Boolean,
    onBack: () -> Unit,
    onArticleClick: (Int) -> Unit
) {
    val uiState   by viewModel.uiState.collectAsState()
    val postState by viewModel.postState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val isRefreshing = uiState is NewsUiState.Loading

    LaunchedEffect(postState) {
        if (postState is PostUiState.Success) {
            showAddDialog = false
            viewModel.resetPostState()
        }
    }

    Scaffold(
        containerColor = if (isDark) darkBg else cream,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = strawberry,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) { Icon(Icons.Rounded.Create, contentDescription = null) }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            // ── Header ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(matchaDeep, matchaLight)))
                    .padding(top = 48.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically   // ✅ FIX 1
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = cream
                    )
                }
                Text(
                    text = "Tech News",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = cream
                )
            }

            // ── Body ────────────────────────────────────────────────────────
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (val state = uiState) {
                    is NewsUiState.Loading -> CircularProgressIndicator(color = strawberry)

                    is NewsUiState.Error -> Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message,
                            color = strawberry,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Button(
                            onClick = { viewModel.loadArticles() },
                            colors = ButtonDefaults.buttonColors(containerColor = matcha)
                        ) { Text("Coba Lagi") }
                    }

                    is NewsUiState.Success -> {
                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = { viewModel.loadArticles() }
                        ) {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(state.articles) { article ->
                                    ArticleCard(article, isDark) { onArticleClick(article.id) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        CreatePostDialog(
            isDark    = isDark,
            postState = postState,
            onDismiss = { showAddDialog = false; viewModel.resetPostState() },
            onSubmit  = { t, b -> viewModel.createPost(t, b) }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ARTICLE CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ArticleCard(article: Article, isDark: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (isDark) darkCard else warmWhite)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ Aktifkan KamelImage
            KamelImage(
                resource = asyncPainterResource(article.thumbnailUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                onLoading = {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(matchaLight.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = matchaDeep,
                            strokeWidth = 2.dp
                        )
                    }
                },
                onFailure = {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(matchaLight.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.ImageNotSupported, contentDescription = null, tint = matchaDeep)
                    }
                }
            )

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(article.title, fontWeight = FontWeight.Bold, color = if (isDark) darkText else matchaDeep, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(article.description, fontSize = 12.sp, color = if (isDark) darkSubtext else charcoal, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text("Baca selengkapnya", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = strawberry, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// NEWS DETAIL SCREEN
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun NewsDetailScreen(
    articleId: Int,
    viewModel: NewsViewModel,
    isDark: Boolean,
    onBack: () -> Unit
) {
    val article by viewModel.detailArticle.collectAsState()
    LaunchedEffect(articleId) { viewModel.loadDetail(articleId) }

    Scaffold(
        containerColor = if (isDark) darkBg else cream
    ) { paddingValues ->                                         // ✅ FIX 3
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)                          // ✅ FIX 3
                .verticalScroll(rememberScrollState())
        ) {
            // ── Hero image + back button ─────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                article?.let {
                    KamelImage(
                        resource      = asyncPainterResource(it.imageUrl),
                        contentDescription = null,
                        modifier      = Modifier.fillMaxSize(),
                        contentScale  = ContentScale.Crop
                    )
                }
                IconButton(
                    onClick   = { viewModel.clearDetail(); onBack() },
                    modifier  = Modifier
                        .padding(top = 40.dp, start = 16.dp)
                        .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
                }
            }

            // ── Content ─────────────────────────────────────────────────
            article?.let {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text       = it.title,
                        fontSize   = 24.sp,
                        fontWeight = FontWeight.Black,
                        color      = if (isDark) darkText else matchaDeep
                    )
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = strawberry.copy(alpha = 0.3f), thickness = 2.dp)
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text       = it.description,
                        fontSize   = 16.sp,
                        color      = if (isDark) darkText else charcoal,
                        lineHeight = 26.sp
                    )
                }
            } ?: Box(
                modifier        = Modifier.fillMaxWidth().padding(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = strawberry)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CREATE POST DIALOG
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CreatePostDialog(
    isDark: Boolean,
    postState: PostUiState,
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body  by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape  = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = if (isDark) darkSurface else warmWhite)
        ) {
            Column(
                modifier            = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally   // ✅ FIX 2
            ) {
                Text(
                    text       = "Tulis Berita",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = if (isDark) darkText else charcoal
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value         = title,
                    onValueChange = { title = it },
                    label         = { Text("Judul") },
                    modifier      = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value         = body,
                    onValueChange = { body = it },
                    label         = { Text("Isi") },
                    modifier      = Modifier.fillMaxWidth().height(100.dp)
                )
                Spacer(Modifier.height(24.dp))

                if (postState is PostUiState.Loading) {
                    CircularProgressIndicator(color = matcha)
                } else {
                    Row(
                        modifier            = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = onDismiss) { Text("Batal") }
                        Button(
                            onClick = { onSubmit(title, body) },
                            colors  = ButtonDefaults.buttonColors(containerColor = matcha)
                        ) { Text("Kirim") }
                    }
                }
            }
        }
    }
}
