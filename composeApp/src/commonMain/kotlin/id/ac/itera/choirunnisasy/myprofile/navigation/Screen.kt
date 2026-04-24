package id.ac.itera.choirunnisasy.myprofile.navigation

sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object AddNote : Screen("add_note")
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
    object NewsList : Screen("news_list")
    object NewsDetail : Screen("news_detail/{articleId}") {
        fun createRoute(articleId: Int) = "news_detail/$articleId"
    }
    object Settings : Screen("settings")
}
