package com.contactproject

data class Person(
    var firstName: String,
    var lastName: String,
    var date: String,
    var num: String,
    var email: String,
    var genre: GENRE

) : java.io.Serializable



enum class GENRE {
    Homme, Femme, Autre
}

