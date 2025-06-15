package com.hamalawey.thmanyah.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamalawey.domain.model.Section
import com.hamalawey.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    // --- Primary Data Sources ---
    // This holds ALL original sections fetched once at startup, unfiltered.
    private val _homeBaseSections = MutableStateFlow<List<Section>>(emptyList())

    // This is the single MutableStateFlow that the UI (HomeScreen) will observe.
    // Its value will be explicitly updated by either home section logic or search logic.
    private val _displayedSections = MutableStateFlow<List<Section>>(emptyList())
    val displayedSections: StateFlow<List<Section>> = _displayedSections.asStateFlow()

    // --- UI Control States ---
    private val _selectedContentType = MutableStateFlow<String?>(null)
    val selectedContentType: StateFlow<String?> = _selectedContentType.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // --- Flow for Debounced Search Triggers ---
    private val _searchTriggerFlow = MutableSharedFlow<String>(replay = 0)


    init {
        // Start collecting from the debounced search trigger flow
        collectSearchTriggers()

        // Load all home sections initially and display them (filtered by default 'All' or initial selectedType)
        loadHomeSectionsAndDisplay()
    }

    /**
     * Sets up the coroutine to observe _searchTriggerFlow and apply debounce/distinctUntilChanged logic.
     * This triggers `executeSearchAndDisplayResults` only when the user stops typing.
     */
    private fun collectSearchTriggers() {
        viewModelScope.launch {
            _searchTriggerFlow
                .debounce(200L) // Wait for 200 milliseconds of inactivity
                .distinctUntilChanged() // Only proceed if the query value actually changed after debounce
                .collectLatest { debouncedQuery -> // collectLatest cancels any ongoing search for an older query
                    if (debouncedQuery.isBlank()) {
                        // If the debounced query is blank, revert to displaying filtered home sections
                        displayFilteredHomeSections()
                    } else {
                        // Otherwise, execute the search
                        executeSearchAndDisplayResults(debouncedQuery, _selectedContentType.value)
                    }
                }
        }
    }

    /**
     * Fetches all initial home sections from the repository and displays them.
     * This is typically called once on ViewModel initialization.
     */
    private fun loadHomeSectionsAndDisplay() {
        viewModelScope.launch {
            _isLoading.value = true // Loading for initial data
            _errorMessage.value = null
            repository.getHomeSections()
                .onSuccess { data ->
                    _homeBaseSections.value = data // Update the base home sections
                    displayFilteredHomeSections() // Display them, applying the current filter
                }
                .onFailure { throwable ->
                    _errorMessage.value = "Error fetching home sections: ${throwable.localizedMessage}"
                }
            _isLoading.value = false
        }
    }

    /**
     * Filters the _homeBaseSections based on _selectedContentType and updates _displayedSections.
     * This is used when chips are selected (if search is not active) or search is cleared.
     */
    private fun displayFilteredHomeSections() {
        val currentHomeSections = _homeBaseSections.value
        val currentSelectedType = _selectedContentType.value

        _displayedSections.value = currentHomeSections.filter { section ->
            currentSelectedType == null || section.contentType == currentSelectedType
        }
    }

    /**
     * Performs a search using the provided query and content type, then updates _displayedSections.
     */
    private fun executeSearchAndDisplayResults(query: String, contentType: String?) {
        viewModelScope.launch {
            _isLoading.value = true // Loading for search data
            _errorMessage.value = null
            repository.searchContent(
                query = query,
                type = contentType // Pass selected type as filter to search API
            )
                .onSuccess { data ->
                    _displayedSections.value = data // Update the displayed sections with search results
                }
                .onFailure { throwable ->
                    _errorMessage.value = "Error searching: ${throwable.localizedMessage}"
                }
            _isLoading.value = false
        }
    }

    // Add this to your MainViewModel
    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.getHomeSections()
                .onSuccess { data ->
                    _homeBaseSections.value = data
                    if (_searchQuery.value.isBlank()) {
                        displayFilteredHomeSections()
                    } else {
                        // If there's an active search, refresh the search results
                        executeSearchAndDisplayResults(_searchQuery.value, _selectedContentType.value)
                    }
                }
                .onFailure { throwable ->
                    _errorMessage.value = "Error refreshing data: ${throwable.localizedMessage}"
                }
            _isLoading.value = false
        }
    }

    /**
     * Called by the UI whenever the search input text changes.
     * Updates the displayed query immediately and emits to the trigger flow for debouncing.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query // Update the UI's TextField value immediately

        viewModelScope.launch {
            _searchTriggerFlow.emit(query) // Emit the new query to the debounce flow
        }
    }

    /**
     * Handles selecting a content type chip.
     * This only affects the *home sections* view; it does not trigger a search operation
     * if a search query is currently active.
     */
    fun selectContentType(contentType: String?) {
        _selectedContentType.value = contentType
        // ONLY update the displayed sections with filtered home data if no search is active.
        if (_searchQuery.value.isBlank()) {
            displayFilteredHomeSections()
        }
        // If a search query is active, selecting a chip does NOT change the displayed results.
    }

    /**
     * Provides the list of all available content types for the chips.
     * This should always come from the _homeBaseSections to ensure consistent chip options.
     */
    fun getAllContentTypes(): List<String> {
        return _homeBaseSections.value.map { it.contentType }.distinct()
    }
}