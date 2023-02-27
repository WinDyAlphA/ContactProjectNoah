package com.contactproject.pojo

import com.google.gson.annotations.SerializedName


class PokemonRessources {
    @SerializedName("ability")
    var ability: String? = null

    @SerializedName("data")
    var data: List<Datum>? = null

    inner class Datum {

        @SerializedName("name")
        var name: String? = null

    }
}