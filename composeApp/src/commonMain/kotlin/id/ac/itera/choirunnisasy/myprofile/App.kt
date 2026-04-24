package id.ac.itera.choirunnisasy.myprofile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import myprofileapp.composeapp.generated.resources.Res
import myprofileapp.composeapp.generated.resources.profile_nisa
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.itera.choirunnisasy.myprofile.component.InfoCard
import id.ac.itera.choirunnisasy.myprofile.component.ProfileTag
import id.ac.itera.choirunnisasy.myprofile.component.SkillChip
import id.ac.itera.choirunnisasy.myprofile.navigation.AppNavigation
import id.ac.itera.choirunnisasy.myprofile.data.ProfileUiState
import id.ac.itera.choirunnisasy.myprofile.db.DatabaseDriverFactory
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.delay

@OptIn(ExperimentalSettingsApi::class)
@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    val settings = remember { createSettings() }  // ← pakai expect/actual
    AppNavigation(driverFactory, settings)
}


@Composable
fun ProfileScreen(
    uiState      : ProfileUiState,
    onEditClick  : () -> Unit,
    onToggleDark : () -> Unit,
    onNewsClick  : () -> Unit
) {
    val isDark = uiState.isDarkMode

    var headerVisible  by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }
    var cardsVisible   by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        headerVisible  = true
        delay(400)
        contentVisible = true
        delay(250)
        cardsVisible   = true
    }

    val bgColors = if (isDark) {
        listOf(darkBg, darkSurface, Color(0xFF1E2818))
    } else {
        listOf(cream, creamDark, Color(0xFFE8D8C8))
    }

    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val bgAlpha by infiniteTransition.animateFloat(
        initialValue  = 0.3f,
        targetValue   = 0.6f,
        animationSpec = infiniteRepeatable(
            animation  = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bgPulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = bgColors))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush  = Brush.radialGradient(
                    colors = listOf(matchaPale.copy(alpha = bgAlpha * 0.8f), Color.Transparent),
                    radius = 600f
                ),
                radius = 600f,
                center = Offset(size.width * 0.95f, -80f)
            )
            drawCircle(
                brush  = Brush.radialGradient(
                    colors = listOf(strawberryPale.copy(alpha = bgAlpha * 0.7f), Color.Transparent),
                    radius = 400f
                ),
                radius = 400f,
                center = Offset(-60f, size.height * 0.85f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeaderWithToggle(
                visible      = headerVisible,
                isDark       = isDark,
                onToggleDark = onToggleDark
            )

            NameCard(
                visible  = contentVisible,
                uiState  = uiState,
                isDark   = isDark,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-52).dp)
            )

            Spacer(modifier = Modifier.height((-38).dp))

            SectionTitleDark(
                title    = "Contact Information",
                isDark   = isDark,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
            )

            AnimatedVisibility(
                visible = cardsVisible,
                enter   = fadeIn(tween(500)) + slideInHorizontally(tween(500)) { -60 }
            ) {
                InfoCard(
                    icon = Icons.Rounded.Email,
                    label = "Email Address",
                    value = uiState.email,
                    accentColor = matcha,
                    bgColor = matchaPale,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            AnimatedVisibility(
                visible = cardsVisible,
                enter   = fadeIn(tween(500, delayMillis = 150)) +
                        slideInHorizontally(tween(500, delayMillis = 150)) { -60 }
            ) {
                InfoCard(
                    icon = Icons.Rounded.Phone,
                    label = "Phone Number",
                    value = uiState.phone,
                    accentColor = strawberry,
                    bgColor = strawberryPale,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            AnimatedVisibility(
                visible = cardsVisible,
                enter   = fadeIn(tween(500, delayMillis = 300)) +
                        slideInHorizontally(tween(500, delayMillis = 300)) { -60 }
            ) {
                InfoCard(
                    icon = Icons.Rounded.LocationOn,
                    label = "Current Location",
                    value = uiState.location,
                    accentColor = Color(0xFFD4840A),
                    bgColor = Color(0xFFFFF3E0),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionTitleDark(
                title      = "Skills & Interests",
                isDark     = isDark,
                modifier   = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                isReversed = true
            )

            AnimatedVisibility(
                visible = cardsVisible,
                enter   = fadeIn(tween(600, delayMillis = 500)) +
                        slideInVertically(tween(600, delayMillis = 500)) { 40 }
            ) {
                SkillsSectionDark(
                    isDark   = isDark,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(
                visible = cardsVisible,
                enter   = fadeIn(tween(600, delayMillis = 600)) +
                        slideInVertically(tween(600, delayMillis = 600)) { 50 }
            ) {
                OutlinedButton(
                    onClick  = onEditClick,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(56.dp),
                    shape  = RoundedCornerShape(18.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = matchaLight.copy(alpha = if (isDark) 0.5f else 0.8f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (isDark) matchaLight else matchaDeep
                    )
                ) {
                    androidx.compose.material3.Icon(
                        imageVector     = Icons.Rounded.Edit,
                        contentDescription = null,
                        modifier        = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text       = "Edit Profil",
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = cardsVisible,
                enter   = fadeIn(tween(600, delayMillis = 700)) +
                        slideInVertically(tween(600, delayMillis = 700)) { 50 }
            ) {
                Button(
                    onClick        = { },
                    modifier       = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    shape          = RoundedCornerShape(22.dp),
                    colors         = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    elevation      = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(strawberry, Color(0xFFA93226), strawberry)
                                ),
                                shape = RoundedCornerShape(22.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = "🍓", fontSize = 16.sp)
                            Text(
                                text       = "Contact Me",
                                fontSize   = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color      = Color.White
                            )
                            androidx.compose.material3.Icon(
                                imageVector     = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                tint            = Color.White.copy(alpha = 0.8f),
                                modifier        = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedVisibility(
                visible = cardsVisible,
                enter   = fadeIn(tween(600, delayMillis = 800)) +
                        slideInVertically(tween(600, delayMillis = 800)) { 50 }
            ) {
                Button(
                    onClick = onNewsClick,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) darkSurface else matchaLight,
                        contentColor = if (isDark) matchaLight else matchaDeep
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "🗞️ Baca Berita Tech",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier        = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        matchaLight.copy(alpha = 0.5f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text      = "crafted with 🍵 & 🍓 · ITERA 2025",
                    fontSize  = 11.sp,
                    color     = if (isDark) darkSubtext else Color(0xFF999999),
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun ProfileHeaderWithToggle(
    visible      : Boolean,
    isDark       : Boolean,
    onToggleDark : () -> Unit
) {
    val headerColors = if (isDark) {
        listOf(Color(0xFF1E2D14), Color(0xFF2E4020), Color(0xFF3D5229))
    } else {
        listOf(matchaDeep, matcha, matchaLight)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .clip(RoundedCornerShape(bottomStart = 56.dp, bottomEnd = 56.dp))
            .background(
                Brush.linearGradient(
                    colors = headerColors,
                    start  = Offset(0f, 0f),
                    end    = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color  = Color.White.copy(alpha = 0.07f),
                radius = 180f,
                center = Offset(size.width - 30f, -40f)
            )
            drawCircle(
                color  = Color.White.copy(alpha = 0.05f),
                radius = 100f,
                center = Offset(40f, size.height - 30f)
            )
            listOf(
                Offset(size.width * 0.15f, size.height * 0.25f),
                Offset(size.width * 0.85f, size.height * 0.65f),
                Offset(size.width * 0.70f, size.height * 0.15f),
                Offset(size.width * 0.25f, size.height * 0.75f),
            ).forEach {
                drawCircle(color = Color.White.copy(alpha = 0.2f), radius = 4f, center = it)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Icon(
                    imageVector     = if (isDark) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                    contentDescription = "Toggle dark mode",
                    tint            = Color.White.copy(alpha = 0.8f),
                    modifier        = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Switch(
                    checked         = isDark,
                    onCheckedChange = { onToggleDark() },
                    colors          = SwitchDefaults.colors(
                        checkedThumbColor   = matchaDeep,
                        checkedTrackColor   = matchaLight,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.White.copy(alpha = 0.3f)
                    )
                )
            }

            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(800)) + scaleIn(tween(800), initialScale = 0.5f)
            ) {
                ProfilePhotoWidget()
            }
        }
    }
}

@Composable
private fun ProfilePhotoWidget() {
    val infiniteTransition = rememberInfiniteTransition(label = "ring")
    val rotation by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing)
        ),
        label = "rotate"
    )

    Box(
        modifier         = Modifier.size(160.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color  = Color.White.copy(alpha = 0.35f),
                radius = size.minDimension / 2,
                style  = Stroke(
                    width      = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), phase = rotation)
                )
            )
        }

        Box(
            modifier = Modifier
                .size(138.dp)
                .clip(CircleShape)
                .border(width = 4.dp, color = Color.White, shape = CircleShape)
                .border(width = 7.dp, color = strawberry, shape = CircleShape)
                .padding(6.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(colors = listOf(matchaLight, matchaDeep))
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter            = painterResource(Res.drawable.profile_nisa),
                contentDescription = "Profile Photo",
                modifier           = Modifier.fillMaxSize(),
                contentScale       = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 4.dp, y = (-4).dp)
                .clip(CircleShape)
                .background(strawberry)
                .border(width = 2.dp, color = Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🍓", fontSize = 14.sp)
        }
    }
}

@Composable
private fun NameCard(
    visible  : Boolean,
    uiState  : ProfileUiState,
    isDark   : Boolean,
    modifier : Modifier = Modifier
) {
    val cardColor = if (isDark) darkCard else warmWhite
    val textColor = if (isDark) darkText else charcoal

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(700)) + slideInVertically(tween(700)) { 50 }
    ) {
        Card(
            modifier  = modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(28.dp),
            colors    = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            border    = BorderStroke(
                width = 1.5.dp,
                color = matchaLight.copy(alpha = if (isDark) 0.2f else 0.3f)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(matchaLight, strawberry, matchaLight)
                        )
                    )
            )
            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val nameParts = uiState.name.trim().split(" ")
                Text(
                    text       = nameParts.firstOrNull() ?: uiState.name,
                    fontSize   = 26.sp,
                    fontWeight = FontWeight.Black,
                    color      = textColor
                )
                if (nameParts.size > 1) {
                    Text(
                        text       = nameParts.drop(1).joinToString(" "),
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle  = FontStyle.Italic,
                        color      = if (isDark) matchaLight else matchaDeep
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text       = "\"${uiState.bio}\"",
                    fontSize   = 13.sp,
                    color      = if (isDark) darkSubtext else strawberry,
                    fontStyle  = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
                    textAlign  = TextAlign.Center,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProfileTag(emoji = "🌿", text = uiState.nim, isMatcha = true)
                    ProfileTag(emoji = "💻", text = uiState.jurusan, isMatcha = false)
                }
            }
        }
    }
}

@Composable
private fun SectionTitleDark(
    title      : String,
    isDark     : Boolean,
    modifier   : Modifier = Modifier,
    isReversed : Boolean  = false
) {
    Row(
        modifier              = modifier,
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(22.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    Brush.verticalGradient(
                        colors = if (isReversed) listOf(strawberry, matcha)
                        else             listOf(matcha, strawberry)
                    )
                )
        )
        Text(
            text       = title,
            fontSize   = 16.sp,
            fontWeight = FontWeight.Bold,
            color      = if (isDark) darkText else charcoal
        )
    }
}

@Composable
private fun SkillsSectionDark(isDark: Boolean, modifier: Modifier = Modifier) {
    val skills = listOf(
        Triple("🍵", "Compose UI",  true),
        Triple("🍓", "Kotlin",      false),
        Triple("🌿", "Mobile Dev",  true),
        Triple("☕", "Clean Code",  false),
        Triple("🎨", "UI Design",   true),
        Triple("📱", "Android",     false),
    )

    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(
            containerColor = if (isDark) darkCard else warmWhite.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                skills.take(3).forEach { (e, l, m) ->
                    SkillChip(emoji = e, label = l, isMatcha = m)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                skills.drop(3).forEach { (e, l, m) ->
                    SkillChip(emoji = e, label = l, isMatcha = m)
                }
            }
        }
    }
}
