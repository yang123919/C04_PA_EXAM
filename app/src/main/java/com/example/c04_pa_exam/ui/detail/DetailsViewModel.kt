package com.example.pa_mock.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c04_pa_exam.data.model.Movie
import com.example.c04_pa_exam.data.repository.MovieRepository
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private val repo = MovieRepository()

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadMovieById(id: Int) {
        viewModelScope.launch {
            try {
                val m = repo.getMovieById(id)
                _movie.value = m
            } catch (e: Exception) {
                _error.value = "Failed to load movie."
            }
        }
    }

    fun onErrorShown() { _error.value = null }
}