# Notes App - Offline First (KMP)

- **Nama:** Choirunnisa
- **NIM:** 123140136
- **Mata Kuliah:** Pengembangan Aplikasi Mobile RB

[cite_start]Aplikasi pencatatan (*Notes App*) berbasis **Kotlin Multiplatform (KMP)** yang dirancang dengan arsitektur *Offline-First*[cite: 514, 519]. Aplikasi ini memastikan data pengguna tetap aman dan dapat diakses kapan saja, tanpa bergantung pada koneksi internet, dengan mengusung antarmuka yang bersih, *minimalist*, dan *modern*.

[cite_start]Proyek ini dikembangkan untuk memenuhi **Tugas Praktikum Minggu 7 - Pengembangan Aplikasi Mobile**[cite: 511].

## ✨ Fitur Utama

* [cite_start]**CRUD Operations:** Mendukung pembuatan (Create), pembacaan (Read), pembaruan (Update), dan penghapusan (Delete) catatan secara lokal[cite: 516].
* [cite_start]**Smart Search:** Fitur pencarian *real-time* untuk menemukan catatan spesifik berdasarkan judul atau isi[cite: 517].
* [cite_start]**User Preferences (DataStore):** Pengaturan personalisasi aplikasi seperti pilihan Tema (Light/Dark/System) dan urutan penyortiran catatan, disimpan dengan aman menggunakan `multiplatform-settings`[cite: 518, 78, 80].
* [cite_start]**Offline-First Architecture:** Mengandalkan **SQLDelight** sebagai *Single Source of Truth*, memastikan pengalaman pengguna yang mulus tanpa jeda *network*[cite: 515, 519, 490].
* [cite_start]**Reactive UI States:** Penanganan status layar yang responsif menggunakan `StateFlow` untuk transisi *Loading*, *Empty State* (saat belum ada catatan), dan *Content*[cite: 520, 312].

## 🗄️ Database Schema (SQLDelight)

[cite_start]Aplikasi ini menggunakan SQLDelight untuk menghasilkan API Kotlin yang *type-safe* dari *query* SQL[cite: 166]. [cite_start]Berikut adalah skema tabel `Note` yang digunakan[cite: 204, 205, 206, 207, 208, 209, 210]:

```sql
CREATE TABLE Note (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL
);