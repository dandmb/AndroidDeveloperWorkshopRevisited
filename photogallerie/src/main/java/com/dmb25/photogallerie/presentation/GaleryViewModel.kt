package com.dmb25.photogallerie.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmb25.photogallerie.data.model.MyGallery
import com.dmb25.photogallerie.data.repo.GalleryRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@Immutable
data class UiState(
    val item: MyGallery? = null,
    val isLoading: Boolean = false,
    val isError: String = ""
)

sealed class UiEvent{
    object NoNextItem: UiEvent()
    object NoPreviousItem : UiEvent()
}


class GaleryViewModel(
    private val galleryRepo: GalleryRepo
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent?>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var dataSize = 0

    init {
        getGallery()
    }

    fun getGallery(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            dataSize = 0
            galleryRepo.getGallery().catch {
                _uiState.update {
                    it.copy(isError = it.toString(), isLoading = false, item = null)
                }
                dataSize = 0
            }.collect{
                _uiState.update { currentState ->
                    currentState.copy(item = it.first(), isLoading = false, isError = "")
                }
                dataSize = it.size
            }
        }
    }

    fun getNextImage(id: Int) {
        viewModelScope.launch {
            val nextItemPosition = id + 1
            if (nextItemPosition <= dataSize) {
                val nextItem = galleryRepo.getGalleryById(nextItemPosition)
                _uiState.update { currentState ->
                    currentState.copy(item = nextItem, isLoading = false, isError = "")
                }
            } else {
                _uiEvent.emit(UiEvent.NoNextItem)
            }
        }
    }

    fun getPreviousImage(id: Int) {
        viewModelScope.launch {
            val previousItemPosition = id - 1
            if (previousItemPosition >= 1) {
                val previousItem = galleryRepo.getGalleryById(previousItemPosition)
                _uiState.update { currentState ->
                    currentState.copy(item = previousItem, isLoading = false, isError = "")
                }
            } else {
                _uiEvent.emit(UiEvent.NoPreviousItem)
            }
        }
    }


}