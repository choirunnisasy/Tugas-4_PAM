#  News Reader App — Tugas Praktikum Minggu 6

> Pengembangan Aplikasi Mobile | IF25-22017  
> Institut Teknologi Sumatera

---
- **Nama:** Choirunnisa
- **NIM:** 123140136
- **Mata Kuliah:** Pengembangan Aplikasi Mobile RB

## 🌐 API yang Digunakan

| API | URL | Kegunaan |
|-----|-----|----------|
| **JSONPlaceholder** | `https://jsonplaceholder.typicode.com` | Data artikel (posts) dan POST request |
| **Picsum Photos** | `https://picsum.photos/seed/{id}/...` | Gambar artikel |

### Endpoint yang dipakai:
- `GET /posts` — mengambil daftar semua artikel
- `GET /posts/{id}` — mengambil detail artikel berdasarkan ID
- `POST /posts` — membuat artikel baru

---

## ✅ Fitur yang Diimplementasikan

### 1. Fetch Berita dari Public API
Aplikasi mengambil data dari **JSONPlaceholder API** menggunakan **Ktor Client** dengan Repository Pattern. Data post digabungkan dengan gambar dari **Picsum Photos** untuk menghasilkan artikel yang lengkap.

### 2. List Artikel (Title, Description, Image)
Menampilkan daftar artikel dalam `LazyColumn` dengan card yang berisi:
- Thumbnail gambar (via Kamel Image)
- Judul artikel
- Deskripsi singkat
- Tombol "Baca selengkapnya"

### 3. Detail Screen
Saat artikel diklik, aplikasi navigasi ke halaman detail yang menampilkan:
- Gambar full-width (hero image)
- Judul lengkap
- Isi artikel

### 4. Pull to Refresh
Pengguna dapat menarik layar ke bawah untuk memuat ulang daftar artikel menggunakan `PullToRefreshBox` dari Material 3.

### 5. Loading, Success, dan Error States
Setiap state ditangani dengan UI yang berbeda:
- **Loading** — `CircularProgressIndicator` di tengah layar
- **Success** — daftar artikel tampil dalam `LazyColumn`
- **Error** — pesan error + tombol "Coba Lagi"

### 6. Repository Pattern
Logic pengambilan data dipisahkan dari ViewModel menggunakan `NewsRepository`. ViewModel hanya berinteraksi dengan repository, tidak langsung ke HTTP client.

```
NewsViewModel → NewsRepository → Ktor HTTP Client → API
```

---

##  Arsitektur & Struktur Folder

```
ui/
├── NewsScreen.kt       # NewsListScreen, ArticleCard, NewsDetailScreen
├── NewsViewModel.kt    # State management (NewsUiState, PostUiState)
data/
├── Article.kt          # Data class: Post, Photo, Article, CreatePostRequest
├── NewsRepository.kt   # Repository: getArticles(), getArticleById(), createPost()
navigation/
├── AppNavigation.kt    # NavHost + routing
├── Screen.kt           # Route definitions
```

---

## Screenshot

### Loading State
> *<img width="537" height="882" alt="Screenshot 2026-04-19 204958" src="https://github.com/user-attachments/assets/b40ee5bd-56d1-4743-9727-b369897af780" />
*

### Success State — List Artikel
> *<img width="539" height="815" alt="Screenshot 2026-04-19 205010" src="https://github.com/user-attachments/assets/3463a7c0-64e9-4c8d-be87-6db73b886a24" />
*

### Detail Screen
> *<img width="507" height="658" alt="image" src="https://github.com/user-attachments/assets/8bdddde2-f758-490e-ae71-c7f487944749" />
*

### Error State
> *<img width="539" height="811" alt="Screenshot 2026-04-19 205118" src="https://github.com/user-attachments/assets/d03d6254-4dbd-41ed-9299-359ac02cfb1b" />
*

### Pull to Refresh
> *<img width="557" height="827" alt="Screenshot 2026-04-19 205048" src="https://github.com/user-attachments/assets/aa12db7f-3b61-423b-98e7-53365b76f263" />
*

---
## 🎥 Video Demo (30 Detik)
Video demonstrasi yang menunjukkan semua alur navigasi dapat dilihat pada tautan berikut:
**[Tonton Video Demo di sini](https://drive.google.com/file/d/1eMPofH1_rb5TyRv9aoE93GQz9WfNn6Kz/view?usp=sharing)**

---

##  Tech Stack

| Teknologi | Versi | Kegunaan |
|-----------|-------|----------|
| Kotlin Multiplatform | - | Framework utama |
| Jetpack Compose | - | UI |
| Ktor Client | 2.3.7 | HTTP requests |
| Kotlinx Serialization | - | JSON parsing |
| Kamel Image | - | Async image loading |
| Material 3 | - | Komponen UI |
| ViewModel + StateFlow | - | State management |

---

##  Cara Menjalankan

1. Clone repository ini
2. Buka dengan Android Studio
3. Pastikan branch aktif adalah `week-6`
4. Jalankan di emulator atau device Android

---
