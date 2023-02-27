package com.contactproject

import android.graphics.Bitmap

data class Person(
    var id: Int,
    var firstName: String,
    var lastName: String,
    var date: String,
    var num: String,
    var email: String,
    var genre: GENRE,
    var imageuri: String

) : java.io.Serializable



enum class GENRE {
    Homme, Femme, Autre, Default
}

