package com.oder.cinema.model

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("docs") var movies: ArrayList<Movie> = arrayListOf(),
    @SerializedName("total") var total: Int? = null,
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("page") var page: Int? = null,
    @SerializedName("pages") var pages: Int? = null
)
