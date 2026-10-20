package com.example.c04_pa_exam.api

import com.example.c04_pa_exam.data.model.Movie
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {
    @GET("movies")
    suspend fun getAllMovies(): List<Movie>

    @GET("movies/{id}")
    suspend fun getMovieById(@Path("id") id: Int): Movie
}