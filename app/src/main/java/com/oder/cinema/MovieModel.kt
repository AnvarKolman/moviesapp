package com.oder.cinema

import android.graphics.Bitmap

//TODO need remove this class
data class MovieModel(
    var movieName: String?,
    var imageUrl: String? = null,
    var image: Bitmap? = null
)
