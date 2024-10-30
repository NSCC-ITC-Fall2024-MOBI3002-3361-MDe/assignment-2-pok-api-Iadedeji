package com.example.pokemon.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val sprites: Sprites
)

data class Type(
    val slot: Int,
    val type: TypeName
)

data class TypeName(
    val name: String
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String
)
