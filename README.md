# Notes App — Integrasi Smart AI Chatbot (Gemini API)

### 📖 Deskripsi Tugas
Proyek ini adalah pengembangan aplikasi catatan (Notes App) yang mengintegrasikan Google Gemini AI untuk membantu pengguna dalam mengelola catatan secara cerdas. Fokus pengumpulan minggu ini adalah penerapan pengujian perangkat lunak mencakup Unit Testing pada Repository dan ViewModel, serta UI Testing menggunakan Compose Test Rule untuk memastikan aplikasi bebas dari bug dan memiliki coverage kode yang baik.

### 📝 Identitas Mahasiswa
- **Nama**: Choirunnisa Syawaldina
- **NIM**: 123140136
- **Kelas**: Pengembangan Aplikasi Mobile RB

---

### ✅ Daftar Test Cases
Berikut adalah daftar **12 test cases** yang telah diuji dan dinyatakan **Passed**:

| Komponen | Skenario Pengujian | Jumlah | Status |
| :--- | :--- | :---: | :---: |
| **Repository** | Pengujian database local (Insert, Delete, Get by ID, Toggle Favorite) | 5 | ✅ Passed |
| **ViewModel** | Pengujian logika bisnis, StateFlow, dan interaksi dengan Repository | 4 | ✅ Passed |
| **UI (Instrumented)**| Pengujian komponen UI Compose, Input Fields, dan List Rendering | 3 | ✅ Passed |
| **Total Passed** | | **12** | |

---

### 📊 Screenshot Test Coverage
Berikut adalah bukti persentase kode yang ter-cover oleh pengujian:

![Test Coverage](screenshots/test_coverage.png)
*(Catatan: Jika coverage tidak muncul, pastikan Anda menjalankan 'Run with Coverage' pada target Android)*

---

### 🎥 Link Video Demo (45 Detik)
Video ini mendemonstrasikan fitur utama aplikasi termasuk integrasi Gemini AI dan hasil pengujian.

🔗 **[Tonton Video Demo (45 Detik)](https://drive.google.com/file/d/1ApIUR_kP36NE5QXqZvFiPtDuSsdMidxM/view?usp=sharing)**

---

### ✨ Fitur yang Diimplementasikan (Summary)
- **Smart AI Assistant**: Streaming response & multi-turn conversation dengan Gemini API.
- **Notes Management**: CRUD catatan dengan database lokal menggunakan SQLDelight.
- **Platform Specific**: Deteksi status baterai dan koneksi jaringan.
- **Clean Architecture**: Implementasi Dependency Injection dengan Koin.

*Dibuat dengan 🍵 & 🍓 · ITERA 2025*
