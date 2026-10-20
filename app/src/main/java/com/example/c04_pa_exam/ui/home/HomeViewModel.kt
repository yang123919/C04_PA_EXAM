package com.example.c04_pa_exam.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c04_pa_exam.data.model.Movie
import com.example.c04_pa_exam.data.repository.MovieRepository
import kotlinx.coroutines.launch

enum class MovieSortField { TITLE, RATING }
enum class SortOrder { ASC, DESC }

class HomeViewModel : ViewModel() {
    private val repo = MovieRepository()

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _sortField = MutableLiveData(MovieSortField.TITLE)
    val sortField: LiveData<MovieSortField> = _sortField

    private val _sortOrder = MutableLiveData(SortOrder.ASC)
    val sortOrder: LiveData<SortOrder> = _sortOrder

    // Full list from the API. Search and sort are applied to a copy of this.
    private var allMovies: List<Movie> = emptyList()
    private var query = ""

    fun loadAllMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                allMovies = repo.getAllMovies()
                applyFilterAndSort()
            } catch (e: Exception) {
                _error.value = "Failed to load movies."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearch(text: String) {
        query = text.trim()
        applyFilterAndSort()
    }

    fun onSortChipClicked(field: MovieSortField) {
        if (_sortField.value == field) {
            // Same chip tapped again: flip the direction
            _sortOrder.value = if (_sortOrder.value == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC
        } else {
            // New chip: select it with its default direction
            _sortField.value = field
            _sortOrder.value = if (field == MovieSortField.RATING) SortOrder.DESC else SortOrder.ASC
        }
        applyFilterAndSort()
    }

    private fun applyFilterAndSort() {
        var result = allMovies

        if (query.isNotEmpty()) {
            result = result.filter { it.title.contains(query, ignoreCase = true) }
        }

        // Rating is a String in Movie, so convert to a number before comparing
        result = when (_sortField.value) {
            MovieSortField.RATING -> result.sortedBy { it.rating.toDoubleOrNull() ?: 0.0 }
            else -> result.sortedBy { it.title.lowercase() }
        }
        if (_sortOrder.value == SortOrder.DESC) result = result.reversed()

        _movies.value = result
    }

    fun onErrorShown() {
        _error.value = null
    }
}