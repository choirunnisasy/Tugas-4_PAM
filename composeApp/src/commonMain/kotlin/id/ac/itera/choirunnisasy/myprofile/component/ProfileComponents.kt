package id.ac.itera.choirunnisasy.myprofile.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── COLOR PALETTE ────────────────────────────────────────────────────────────
private val matchaDeep      = Color(0xFF3D5229)
private val matcha          = Color(0xFF5C7A3E)
private val matchaLight     = Color(0xFFA8C57E)
private val matchaPale      = Color(0xFFD4E8B8)
private val strawberry      = Color(0xFFC0392B)
private val strawberryLight = Color(0xFFE8766D)
private val strawberryPale  = Color(0xFFFDECEA)
private val warmWhite       = Color(0xFFFFFDF9)
private val charcoal        = Color(0xFF1A1A1A)

// ─── COMPOSABLE 1: InfoCard ───────────────────────────────────────────────────
@Composable
fun InfoCard(
    icon        : ImageVector,
    label       : String,
    value       : String,
    accentColor : Color    = matcha,
    bgColor     : Color    = matchaPale,
    modifier    : Modifier = Modifier          // ← tambahan biar bisa padding dari luar
) {
    // Animasi elevation saat pressed
    var isPressed by remember { mutableStateOf(false) }
    val elevationAnim by animateDpAsState(
        targetValue   = if (isPressed) 2.dp else 6.dp,
        animationSpec = tween(200),
        label         = "elevation"
    )

    Card(
        modifier  = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = warmWhite.copy(alpha = 0.92f)),
        elevation = CardDefaults.cardElevation(defaultElevation = elevationAnim),
        border    = BorderStroke(1.dp, Color.White.copy(alpha = 0.9f))
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor)
                    .border(1.5.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector     = icon,
                    contentDescription = label,
                    tint            = accentColor,
                    modifier        = Modifier.size(22.dp)
                )
            }

            // Label & value
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text          = label.uppercase(),
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.SemiBold,
                    color         = accentColor,
                    letterSpacing = 0.9.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text       = value,
                    fontSize   = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color      = charcoal,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
            }

            // Chevron
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "›",
                    fontSize   = 18.sp,
                    color      = accentColor.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ─── COMPOSABLE 2: ProfileTag ─────────────────────────────────────────────────
@Composable
fun ProfileTag(
    emoji    : String,
    text     : String,
    isMatcha : Boolean = true
) {
    val bg          = if (isMatcha) matchaPale    else strawberryPale
    val textColor   = if (isMatcha) matchaDeep    else strawberry
    val borderColor = if (isMatcha) matchaLight   else strawberryLight

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(bg)
            .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(50.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = emoji, fontSize = 12.sp)
        Text(
            text          = text,
            fontSize      = 11.sp,
            fontWeight    = FontWeight.SemiBold,
            color         = textColor,
            letterSpacing = 0.2.sp
        )
    }
}

// ─── COMPOSABLE 3: SectionTitle ───────────────────────────────────────────────
@Composable
fun SectionTitle(
    title      : String,
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
            text          = title,
            fontSize      = 16.sp,
            fontWeight    = FontWeight.Bold,
            color         = charcoal,
            letterSpacing = 0.2.sp
        )
    }
}

// ─── COMPOSABLE 4: SkillChip ──────────────────────────────────────────────────
@Composable
fun SkillChip(
    emoji    : String,
    label    : String,
    isMatcha : Boolean = true
) {
    val bg          = if (isMatcha) matchaPale   else strawberryPale
    val textColor   = if (isMatcha) matchaDeep   else strawberry
    val borderColor = if (isMatcha) matchaLight  else strawberryLight

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(bg)
            .border(1.5.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(50.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = emoji, fontSize = 13.sp)
        Text(
            text       = label,
            fontSize   = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color      = textColor
        )
    }
}

// ─── COMPOSABLE 5: GradientDivider ────────────────────────────────────────────
@Composable
fun GradientDivider(
    modifier : Modifier = Modifier,
    fraction : Float    = 0.4f
) {
    Box(
        modifier        = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            matchaLight.copy(alpha = 0.6f),
                            strawberry.copy(alpha  = 0.4f),
                            matchaLight.copy(alpha = 0.6f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

// ─── COMPOSABLE 6: FooterNote ─────────────────────────────────────────────────
@Composable
fun FooterNote(
    modifier : Modifier = Modifier,
    note     : String   = "crafted with 🍵 & 🍓 · ITERA 2025"
) {
    Column(
        modifier            = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GradientDivider(fraction = 0.4f)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text      = note,
            fontSize  = 11.sp,
            color     = Color(0xFF999999),
            fontStyle = FontStyle.Italic
        )
    }
}

// ─── COMPOSABLE 7: EmojiOption ────────────────────────────────────────────────
@Composable
fun EmojiOption(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) matcha.copy(alpha = 0.15f) else Color.Transparent)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) matcha else Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = emoji, fontSize = 20.sp)
    }
}