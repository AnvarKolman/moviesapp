package com.oder.cinema.data

import com.oder.cinema.model.Docs
import com.oder.cinema.model.Result
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryName
import retrofit2.http.Url

interface MoviesService {

    @GET("/movie?field=year&search=2022")
    suspend fun movie(@Query(value = "token", encoded = true) token: String): Response<Result>

    @GET("/movie?field=year&search=2020")
    fun movies(@Query(value = "token", encoded = true) token: String): Single<Docs>

    @GET("/person")
    suspend fun person(@Query(value = "token", encoded = false) token: String): Call<ResponseBody>

    @GET
    fun image(@Url imageUrl: String): Single<ResponseBody>

    @GET("/movie")
    fun findByName(
        @Query(value = "token", encoded = true) token: String,
        @Query("field", encoded = true) field: String = "name",
        @Query("search", encoded = true) name: String = "Fight club",
        @Query("isStrict") isStrict: Boolean = false
    ): Single<Result>

    @GET("/movie")
    fun findById(
        @Query(value = "token", encoded = true) token: String,
        @Query("field", encoded = true) field: String = "id",
        @Query("search", encoded = true) id: String
    ): Single<Result>

}