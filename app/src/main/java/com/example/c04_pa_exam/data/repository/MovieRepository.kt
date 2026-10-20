package com.example.c04_pa_exam.data.repository

import com.example.c04_pa_exam.api.ApiClient
import com.example.c04_pa_exam.data.model.Movie

class MovieRepository {
    suspend fun getAllMovies(): List<Movie> = ApiClient.api.getAllMovies()
    suspend fun getMovieById(id: Int): Movie = ApiClient.api.getMovieById(id)
}