package ir.fallahpoor.eks.libraries.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.fallahpoor.eks.data.ExceptionParser
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.repository.LibraryRepository
import ir.fallahpoor.eks.libraries.ui.LibrariesScreenUiState
import ir.fallahpoor.eks.libraries.ui.LibrariesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LibrariesViewModel
@Inject constructor(
    private val libraryRepository: LibraryRepository,
    private val exceptionParser: ExceptionParser
) : ViewModel() {

    sealed class Event {
        object GetLibraries : Event()
        data class PinLibrary(val library: Library, val pin: Boolean) : Event()
        data class ChangeSortOrder(val sortOrder: SortOrder) : Event()
        data class ChangeSearchQuery(val searchQuery: String) : Event()
    }

    private val _librariesScreenUiState = MutableStateFlow(
        LibrariesScreenUiState(sortOrder = libraryRepository.getSortOrder())
    )
    val librariesScreenUiState: StateFlow<LibrariesScreenUiState> = _librariesScreenUiState

    init {
        viewModelScope.launch {
            libraryRepository.getRefreshDate().collect { refreshDate: String ->
                setState(_librariesScreenUiState.value.copy(refreshDate = refreshDate))
            }
        }
    }

    fun handleEvent(event: Event) {
        when (event) {
            is Event.GetLibraries -> getLibraries()
            is Event.PinLibrary -> pinLibrary(event.library, event.pin)
            is Event.ChangeSortOrder -> changeSortOrder(event.sortOrder)
            is Event.ChangeSearchQuery -> changeSearchQuery(event.searchQuery)
        }
    }

    private fun getLibraries() {
        setState(_librariesScreenUiState.value.copy(librariesState = LibrariesState.Loading))
        getLibs()
    }

    private fun pinLibrary(library: Library, pin: Boolean) {
        viewModelScope.launch {
            val state: LibrariesState =
                try {
                    libraryRepository.pinLibrary(library = library, pinned = pin)
                    val libraries: List<Library> = libraryRepository.getLibraries(
                        sortOrder = _librariesScreenUiState.value.sortOrder,
                        searchQuery = _librariesScreenUiState.value.searchQuery
                    )
                    LibrariesState.Success(libraries)
                } catch (e: Throwable) {
                    Timber.e(e)
                    LibrariesState.Error(exceptionParser.getMessage(e))
                }
            setState(_librariesScreenUiState.value.copy(librariesState = state))
        }
    }

    private fun changeSortOrder(sortOrder: SortOrder) {
        setState(
            _librariesScreenUiState.value.copy(
                librariesState = LibrariesState.Loading,
                sortOrder = sortOrder
            )
        )
        getLibs()
    }

    private fun changeSearchQuery(searchQuery: String) {
        setState(
            _librariesScreenUiState.value.copy(
                librariesState = LibrariesState.Loading,
                searchQuery = searchQuery
            )
        )
        getLibs()
    }

    private fun getLibs() {
        viewModelScope.launch {
            val state: LibrariesState =
                try {
                    val libraries: List<Library> = libraryRepository.getLibraries(
                        sortOrder = _librariesScreenUiState.value.sortOrder,
                        searchQuery = _librariesScreenUiState.value.searchQuery
                    )
                    LibrariesState.Success(libraries)
                } catch (e: Throwable) {
                    Timber.e(e)
                    LibrariesState.Error(exceptionParser.getMessage(e))
                }
            setState(_librariesScreenUiState.value.copy(librariesState = state))
        }
    }

    private fun setState(state: LibrariesScreenUiState) {
        _librariesScreenUiState.value = state
    }

}