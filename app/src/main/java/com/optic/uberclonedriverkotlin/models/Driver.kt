package com.optic.uberclonedriverkotlin.models

import com.beust.klaxon.*

private val klaxon = Klaxon()

data class Driver (
    val id: String? = null,
    val name: String? = null,
    val lastname: String? = null,
    val email: String? = null,
    val phone: String? = null,
    var image: String? = null,
    val plateNumber: String? = null,
    val colorCar: String? = null,
    val brandCar: String? = null,
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Client>(json)
    }
}
