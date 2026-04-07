package id.ac.itera.choirunnisasy.myprofile.navigation

sealed class Screen(val route: String) {

    // ── Bottom Navigation Tabs ────────────────────────────────────
    object Notes     : Screen("notes")
    object Favorites : Screen("favorites")
    object Profile   : Screen("profile")

    // ── Note Screens ──────────────────────────────────────────────
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }

    object AddNote : Screen("add_note")

    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
}