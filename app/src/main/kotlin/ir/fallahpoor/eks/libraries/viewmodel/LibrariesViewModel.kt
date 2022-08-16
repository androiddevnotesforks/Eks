package ir.fallahpoor.eks.libraries.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.repository.LibraryRepository
import ir.fallahpoor.eks.libraries.ui.LibrariesScreenUiState
import ir.fallahpoor.eks.libraries.ui.LibrariesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
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

    private var currentJob: Job? = null
    private val _librariesScreenUiState = MutableStateFlow(
        LibrariesScreenUiState(sortOrder = libraryRepository.getSortOrder())
    )
    val librariesScreenUiState: StateFlow<LibrariesScreenUiState> =
        combine(
            _librariesScreenUiState,
            libraryRepository.getRefreshDate()
        ) { libraries, refreshDate ->
            libraries.copy(refreshDate = refreshDate)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            LibrariesScreenUiState(sortOrder = libraryRepository.getSortOrder())
        )

    fun handleEvent(event: Event) {
        when (event) {
            is Event.GetLibraries -> getLibraries()
            is Event.PinLibrary -> pinLibrary(event.library, event.pin)
            is Event.ChangeSortOrder -> changeSortOrder(event.sortOrder)
            is Event.ChangeSearchQuery -> changeSearchQuery(event.searchQuery)
        }
    }

    private fun getLibraries() {
        performActionAndGetLibraries()
    }

    private fun pinLibrary(library: Library, pin: Boolean) {
        performActionAndGetLibraries {
            libraryRepository.pinLibrary(library = library, pinned = pin)
        }
    }

    private fun performActionAndGetLibraries(action: suspend () -> Unit = {}) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val state: LibrariesState = try {
                action()
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
                librariesState = LibrariesState.Loading, sortOrder = sortOrder
            )
        )
        getLibraries()
    }

    private fun changeSearchQuery(searchQuery: String) {
        setState(
            _librariesScreenUiState.value.copy(
                librariesState = LibrariesState.Loading, searchQuery = searchQuery
            )
        )
        getLibraries()
    }

    private fun setState(state: LibrariesScreenUiState) {
        _librariesScreenUiState.update { state }
    }

}