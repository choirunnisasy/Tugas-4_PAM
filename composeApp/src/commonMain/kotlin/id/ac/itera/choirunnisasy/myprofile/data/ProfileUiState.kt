package id.ac.itera.choirunnisasy.myprofile.data

//  ProfileUiState.kt  — data/
//  Data class untuk UI State profile app

data class ProfileUiState(
    val name     : String  = "Choirunnisa Syawaldina",
    val bio      : String  = "Where the calm of matcha meets the sweetness of strawberry",
    val nim      : String  = "123140136",
    val jurusan  : String  = "Teknik Informatika",
    val email    : String  = "choirunnisa.123140136@student.itera.ac.id",
    val phone    : String  = "+62 812 3456 7890",
    val location : String  = "Bandar Lampung, Indonesia",
    val isDarkMode : Boolean = false,
    val isEditMode : Boolean = false,
    val isSaved    : Boolean = false
)