# Tugas Praktikum Minggu 5 - Navigasi Antar Layar

- **Nama:** Choirunnisa Syawaldina
- **NIM:** 123140136
- **Mata Kuliah:** Pengembangan Aplikasi Mobile RB

##  Deskripsi Tugas
Project ini merupakan pengembangan dari Notes App sebelumnya dengan penambahan fitur navigasi multi-screen menggunakan Compose Multiplatform sesuai dengan modul pertemuan 5.

##  Fitur yang Diimplementasikan
- [x] Bottom Navigation dengan 3 tabs (Notes, Favorites, Profile).
- [x] Navigasi Note List → Note Detail dengan mengirimkan `noteId`.
- [x] Floating Action Button (FAB) untuk navigasi ke halaman Add Note.
- [x] Back navigation yang berjalan baik dari semua layar.
- [x] Navigasi ke halaman Edit Note dengan mengirimkan `noteId` sebagai *argument*.
- [x] Struktur folder telah disesuaikan (`navigation/`, `screens/`, `components/`).

##  Screenshots Layar
*(Tambahkan screenshot untuk masing-masing layar di bawah ini)*

| Notes List | Favorites | Profile |
|:---:|:---:|:---:|
| ![Notes List](screenshots/Screenshot_20260417_121249.png) | ![Favorites](screenshots/Screenshot_20260417_121327.png) | ![Profile](screenshots/Screenshot_20260417_121342.png) |

| Add Note | Note Detail | Edit Note |
|:---:|:---:|:---:|
| ![Add Note](screenshots/Screenshot_20260417_121401.png) | ![Note Detail](screenshots/Screenshot_20260417_121430.png) | ![Edit Note](screenshots/Screenshot_20260417_121440.png) |

## 🗺️ Navigation Flow Diagram
![Navigation Flow]
```mermaid
flowchart TD
    %% Tabs Navigation
    T_Notes[Notes Tab]
    T_Fav[Favorites Tab]
    T_Prof[Profile Tab]
    
    %% Main Screens
    S_List[Note List Screen]
    S_Add[Add Note Screen]
    S_Detail[Note Detail Screen]
    S_Edit[Edit Note Screen]

    %% Forward Navigasi
    T_Notes ===> S_List
    S_List -- "Click FAB" --> S_Add
    S_List -- "Click Item" --> S_Detail
    T_Fav -- "Click Item" --> S_Detail
    S_Detail -- "Click Edit" --> S_Edit

    %% Back Navigasi
    S_Add -. "Back" .-> S_List
    S_Detail -. "Back" .-> S_List
    S_Edit -. "Back" .-> S_Detail

    %% Styling Warna
    classDef tab fill:#5C7A3E,stroke:#3D5229,stroke-width:2px,color:#fff;
    classDef screen fill:#252D1C,stroke:#A8C57E,stroke-width:2px,color:#fff;
    
    class T_Notes,T_Fav,T_Prof tab;
    class S_List,S_Add,S_Detail,S_Edit screen;
```

## 🎥 Video Demo (30 Detik)
Video demonstrasi yang menunjukkan semua alur navigasi dapat dilihat pada tautan berikut:
**[Tonton Video Demo di sini](https://drive.google.com/file/d/1UnAs8fvI0VvetaU7oNxArBhuxyv76ckN/view?usp=sharing)**
