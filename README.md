# Pengembangan My Profile App — Tugas Minggu 4

> **IF25-22017 Pengembangan Aplikasi Mobile**  
> Tugas 4: State Management dan MVVM  
> Program Studi Teknik Informatika · Institut Teknologi Sumatera

---

## Identitas Mahasiswa
- **Nama**: Choirunnisa Syawaldina
- **NIM**: 123140136
- **Mata Kuliah**: Pengembangan Aplikasi Mobile RB

---

##  Deskripsi Tugas

Tugas ini merupakan pengembangan dari **Profile App (Tugas 3)** dengan menerapkan konsep **State Management** dan **MVVM Architecture Pattern** menggunakan Compose Multiplatform.

Tiga fitur utama yang dikembangkan:

**1. Implementasi MVVM Pattern**  
Memisahkan UI dari logic menggunakan `ProfileViewModel` yang menyimpan dan mengelola `ProfileUiState` melalui `StateFlow`. View hanya membaca state dan mengirim event ke ViewModel — tidak ada logic di dalam composable.

**2. Fitur Edit Profile**  
User dapat mengedit nama, bio, email, nomor telepon, dan lokasi melalui form yang muncul dengan animasi slide. Setiap `TextField` bersifat stateless menggunakan pola **state hoisting** — nilai (`value`) dan callback (`onValueChange`) dikirim dari ViewModel, bukan disimpan di dalam komponen.

**3. Fitur Dark Mode Toggle**  
Switch di header profil untuk berpindah antara light mode dan dark mode. State `isDarkMode` disimpan di ViewModel sehingga tidak hilang saat terjadi konfigurasi ulang. Seluruh warna UI menyesuaikan secara otomatis saat mode diubah *(Bonus +10%)*.

---

##  Arsitektur MVVM

```
┌─────────────────────────────────────┐
│           DATA LAYER                │
│      ProfileUiState.kt              │
│  (data class: name, bio, email...)  │
└────────────────┬────────────────────┘
                 │
┌────────────────▼────────────────────┐
│         VIEWMODEL LAYER             │
│      ProfileViewModel.kt            │
│  MutableStateFlow → StateFlow       │
│  fun onNameChange(), saveProfile()  │
│  fun toggleDarkMode(), enterEdit()  │
└────────────────┬────────────────────┘
                 │ collectAsState()
┌────────────────▼────────────────────┐
│            UI LAYER                 │
│  App.kt → ProfileScreen             │
│  ui/EditProfileScreen.kt            │
│  ProfileComponent.kt                │
└─────────────────────────────────────┘
```

**Alur State Hoisting di EditProfileScreen:**
```
ViewModel._uiState (MutableStateFlow)
       ↓ uiState.name
ProfileTextField(value = uiState.name, onValueChange = { viewModel.onNameChange(it) })
       ↑ onNameChange(newValue)
ViewModel._uiState.update { it.copy(name = newValue) }
```

---

##  Struktur Folder

```
composeApp/src/commonMain/kotlin/.../myprofile/
├── data/
│   └── ProfileUiState.kt       ← Data class UI State
├── viewmodel/
│   └── ProfileViewModel.kt     ← ViewModel + StateFlow
├── ui/
│   └── EditProfileScreen.kt    ← Form edit profil
├── App.kt                      ← Root + ProfileScreen
└── ProfileComponent.kt         ← Reusable composables
```

---

##  Komponen Reusable (State Hoisting)

| Composable | File | Keterangan |
|-----------|------|-----------|
| `ProfileTextField` | EditProfileScreen.kt | Stateless — `value` + `onValueChange` dari ViewModel |
| `FormSection` | EditProfileScreen.kt | Wrapper section form dengan judul |
| `ReadOnlyField` | EditProfileScreen.kt | Field NIM & Jurusan (tidak bisa diedit) |
| `EditTopBar` | EditProfileScreen.kt | Top bar dengan tombol Batal & Simpan |
| `InfoCard` | ProfileComponent.kt | Card info kontak, reactive terhadap state |
| `ProfileTag` | ProfileComponent.kt | Pill tag NIM dan jurusan |

---

##  Screenshot

### Light Mode
| Profile Screen | Edit Profile |
|---|---|
| ![Profile Light](screenshots/profile_light.png) | ![Edit Profile](screenshots/edit_profile.png) |

### Dark Mode
| Profile Screen | Edit Profile Dark |
|---|---|
| ![Profile Dark](screenshots/profile_dark.png) | ![Edit Dark](screenshots/edit_dark.png) |

---


## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI:** Compose Multiplatform
- **Architecture:** MVVM
- **State:** `StateFlow` + `collectAsState()`
- **Animation:** `AnimatedContent`, `AnimatedVisibility`
- **Target:** Android

---

*crafted with 🍵 & 🍓 · ITERA 2025*
