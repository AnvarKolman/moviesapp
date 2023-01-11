package com.oder.cinema

import android.graphics.Bitmap

data class MovieModel(
    var movieName: String?,
    var imageUrl: String? = null,
    var image: Bitmap? = null
)
