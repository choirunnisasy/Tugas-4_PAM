package id.ac.itera.choirunnisasy.myprofile.viewmodel

//  ProfileViewModel.kt  — viewmodel/
//  ViewModel untuk Profile App menggunakan StateFlow

import androidx.lifecycle.ViewModel
import id.ac.itera.choirunnisasy.myprofile.data.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    // Private MutableStateFlow — hanya ViewModel yang bisa ubah
    private val _uiState = MutableStateFlow(ProfileUiState())

    // Public StateFlow — View hanya bisa read
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // ── Edit mode ────────────────────────────────────────────────────────────

    fun enterEditMode() {
        _uiState.update { it.copy(isEditMode = true, isSaved = false) }
    }

    fun cancelEdit() {
        _uiState.update { it.copy(isEditMode = false) }
    }

    // ── Field updates (state hoisting callbacks dari UI) ─────────────────────

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onBioChange(newBio: String) {
        _uiState.update { it.copy(bio = newBio) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phone = newPhone) }
    }

    fun onLocationChange(newLocation: String) {
        _uiState.update { it.copy(location = newLocation) }
    }

    // ── Save ─────────────────────────────────────────────────────────────────

    fun saveProfile() {
        _uiState.update { it.copy(isEditMode = false, isSaved = true) }
    }
    fun onSavedFeedbackShown() {
        _uiState.update { it.copy(isSaved = false) }
    }

    // ── Dark Mode Toggle ─────────────────────────────────────────

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }
}
