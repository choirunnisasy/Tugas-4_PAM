package id.ac.itera.choirunnisasy.myprofile.ui

//  EditProfileScreen.kt  — ui/
//  Screen edit profile dengan state hoisting

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.itera.choirunnisasy.myprofile.data.ProfileUiState

// ── COLOR TOKENS (sesuai tema) ────────────────────────────────────────────────
private val matchaDeep     = Color(0xFF3D5229)
private val matcha         = Color(0xFF5C7A3E)
private val matchaLight    = Color(0xFFA8C57E)
private val matchaPale     = Color(0xFFD4E8B8)
private val strawberry     = Color(0xFFC0392B)
private val strawberryPale = Color(0xFFFDECEA)
private val cream          = Color(0xFFFAF6F0)
private val warmWhite      = Color(0xFFFFFDF9)
private val charcoal       = Color(0xFF1A1A1A)

// Dark mode colors
private val darkBg         = Color(0xFF1A1F14)
private val darkSurface    = Color(0xFF252D1C)
private val darkCard       = Color(0xFF2E3822)
private val darkText       = Color(0xFFE8F0D8)
private val darkSubtext    = Color(0xFFA8B898)

// ── EDIT PROFILE SCREEN ───────────────────────────────────────────────────────

@Composable
fun EditProfileScreen(
    uiState           : ProfileUiState,
    onNameChange      : (String) -> Unit,
    onBioChange       : (String) -> Unit,
    onEmailChange     : (String) -> Unit,
    onPhoneChange     : (String) -> Unit,
    onLocationChange  : (String) -> Unit,
    onSave            : () -> Unit,
    onCancel          : () -> Unit
) {
    val isDark = uiState.isDarkMode
    val bgColor      = if (isDark) darkBg      else cream
    val surfaceColor = if (isDark) darkSurface  else Color(0xFFF0E8DC)
    val cardColor    = if (isDark) darkCard     else warmWhite
    val textColor    = if (isDark) darkText     else charcoal
    val subtextColor = if (isDark) darkSubtext  else Color(0xFF666666)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(bgColor, surfaceColor)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top bar ───────────────────────────────────────────────────
            EditTopBar(
                isDark    = isDark,
                textColor = textColor,
                onCancel  = onCancel,
                onSave    = onSave
            )

            // ── Header info ───────────────────────────────────────────────
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                border = BorderStroke(1.dp, matchaLight.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(matchaPale),
                        contentAlignment = Alignment.Center
                    ) { Text("✏️", fontSize = 18.sp) }
                    Column {
                        Text(
                            text       = "Edit Profile",
                            fontWeight = FontWeight.Bold,
                            fontSize   = 15.sp,
                            color      = textColor
                        )
                        Text(
                            text     = "Perubahan akan langsung tersimpan",
                            fontSize = 12.sp,
                            color    = subtextColor
                        )
                    }
                }
            }

            // ── Form fields ───────────────────────────────────────────────
            Spacer(modifier = Modifier.height(8.dp))

            FormSection(title = "Informasi Utama", isDark = isDark, textColor = textColor) {
                // Nama — state hoisting ke ViewModel
                ProfileTextField(
                    label         = "Nama Lengkap",
                    value         = uiState.name,
                    onValueChange = onNameChange,           // callback ke ViewModel
                    icon          = Icons.Rounded.Person,
                    accentColor   = matcha,
                    bgColor       = matchaPale,
                    isDark        = isDark
                )
                // Bio — state hoisting ke ViewModel
                ProfileTextField(
                    label         = "Bio / Deskripsi",
                    value         = uiState.bio,
                    onValueChange = onBioChange,            // callback ke ViewModel
                    icon          = Icons.Rounded.Edit,
                    accentColor   = strawberry,
                    bgColor       = strawberryPale,
                    isDark        = isDark,
                    singleLine    = false,
                    minLines      = 2
                )
            }

            FormSection(title = "Informasi Kontak", isDark = isDark, textColor = textColor) {
                ProfileTextField(
                    label         = "Email",
                    value         = uiState.email,
                    onValueChange = onEmailChange,
                    icon          = Icons.Rounded.Email,
                    accentColor   = matcha,
                    bgColor       = matchaPale,
                    isDark        = isDark,
                    keyboardType  = KeyboardType.Email
                )
                ProfileTextField(
                    label         = "Nomor Telepon",
                    value         = uiState.phone,
                    onValueChange = onPhoneChange,
                    icon          = Icons.Rounded.Phone,
                    accentColor   = strawberry,
                    bgColor       = strawberryPale,
                    isDark        = isDark,
                    keyboardType  = KeyboardType.Phone
                )
                ProfileTextField(
                    label         = "Lokasi",
                    value         = uiState.location,
                    onValueChange = onLocationChange,
                    icon          = Icons.Rounded.LocationOn,
                    accentColor   = Color(0xFFD4840A),
                    bgColor       = Color(0xFFFFF3E0),
                    isDark        = isDark
                )
            }

            // ── Info readonly ─────────────────────────────────────────────
            FormSection(title = "Informasi Akademik", isDark = isDark, textColor = textColor) {
                ReadOnlyField(label = "NIM",     value = uiState.nim,     isDark = isDark)
                ReadOnlyField(label = "Jurusan", value = uiState.jurusan, isDark = isDark)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Save button ───────────────────────────────────────────────
            Button(
                onClick        = onSave,
                modifier       = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                shape          = RoundedCornerShape(18.dp),
                colors         = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                elevation      = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(matcha, matchaDeep)
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment      = Alignment.CenterVertically,
                        horizontalArrangement  = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            tint     = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text       = "Simpan Perubahan",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color.White
                        )
                    }
                }
            }

            // Cancel button
            TextButton(
                onClick  = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text  = "Batalkan",
                    color = subtextColor,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── COMPOSABLE: EditTopBar ────────────────────────────────────────────────────
/**
 * EditTopBar — Top bar khusus edit screen dengan tombol Cancel dan Save.
 */
@Composable
fun EditTopBar(
    isDark    : Boolean,
    textColor : Color,
    onCancel  : () -> Unit,
    onSave    : () -> Unit
) {
    val barBg = if (isDark) darkSurface else warmWhite

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(barBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = onCancel) {
            Text("Batal", color = strawberry, fontWeight = FontWeight.Medium)
        }
        Text(
            text       = "Edit Profil",
            fontWeight = FontWeight.Bold,
            fontSize   = 17.sp,
            color      = textColor
        )
        TextButton(onClick = onSave) {
            Text("Simpan", color = matcha, fontWeight = FontWeight.Bold)
        }
    }

    // Divider
    Divider(
        color     = matchaLight.copy(alpha = if (isDark) 0.2f else 0.3f),
        thickness = 1.dp
    )
}

// ── COMPOSABLE: FormSection ───────────────────────────────────────────────────
@Composable
fun FormSection(
    title     : String,
    isDark    : Boolean,
    textColor : Color,
    content   : @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
        // Section label
        Text(
            text          = title.uppercase(),
            fontSize      = 11.sp,
            fontWeight    = FontWeight.SemiBold,
            color         = matcha.copy(alpha = if (isDark) 0.8f else 1f),
            letterSpacing = 1.sp,
            modifier      = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        val cardColor = if (isDark) darkCard else warmWhite

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(20.dp),
            colors   = CardDefaults.cardColors(containerColor = cardColor),
            border   = BorderStroke(1.dp, matchaLight.copy(alpha = if (isDark) 0.15f else 0.25f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier              = Modifier.padding(16.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp),
                content               = content
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

// ── COMPOSABLE: ProfileTextField ─────────────────────────────────────────────
@Composable
fun ProfileTextField(
    label         : String,
    value         : String,
    onValueChange : (String) -> Unit,   // ← STATE HOISTING
    icon          : ImageVector,
    accentColor   : Color   = matcha,
    bgColor       : Color   = matchaPale,
    isDark        : Boolean = false,
    singleLine    : Boolean = true,
    minLines      : Int     = 1,
    keyboardType  : KeyboardType = KeyboardType.Text
) {
    val textColor    = if (isDark) darkText    else charcoal
    val borderColor  = if (isDark) matchaLight.copy(alpha = 0.25f) else matchaLight.copy(alpha = 0.4f)
    val fieldBg      = if (isDark) darkSurface else Color(0xFFF8F4EF)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor.copy(alpha = if (isDark) 0.4f else 1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector     = icon,
                contentDescription = label,
                tint            = accentColor,
                modifier        = Modifier.size(18.dp)
            )
        }

        // OutlinedTextField — stateless, nilai dari ViewModel
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,      // ← callback ke ViewModel
            label         = {
                Text(label, fontSize = 12.sp, color = accentColor.copy(alpha = 0.8f))
            },
            modifier      = Modifier.weight(1f),
            singleLine    = singleLine,
            minLines      = minLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape         = RoundedCornerShape(12.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = accentColor,
                unfocusedBorderColor = borderColor,
                focusedLabelColor    = accentColor,
                cursorColor          = accentColor,
                focusedTextColor     = textColor,
                unfocusedTextColor   = textColor,
                unfocusedContainerColor = fieldBg,
                focusedContainerColor   = fieldBg
            ),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize   = 14.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

// ── COMPOSABLE: ReadOnlyField ─────────────────────────────────────────────────

@Composable
fun ReadOnlyField(
    label  : String,
    value  : String,
    isDark : Boolean = false
) {
    val textColor    = if (isDark) darkText    else charcoal
    val subtextColor = if (isDark) darkSubtext else Color(0xFF888888)
    val bgColor      = if (isDark) darkBg.copy(alpha = 0.5f) else Color(0xFFF0EDE8)

    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text          = label,
                fontSize      = 10.sp,
                color         = subtextColor,
                letterSpacing = 0.5.sp,
                fontWeight    = FontWeight.SemiBold
            )
            Text(
                text       = value,
                fontSize   = 14.sp,
                color      = textColor,
                fontWeight = FontWeight.Medium
            )
        }
        Icon(
            Icons.Rounded.Lock,
            contentDescription = "Read only",
            tint     = subtextColor,
            modifier = Modifier.size(16.dp)
        )
    }
}