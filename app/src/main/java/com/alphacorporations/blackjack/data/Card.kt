package com.alphacorporations.blackjack.data

data class Card(
    val value: Int,
    val symbol: String,
    val name: String,
    var isVisible: Boolean = false,
    var isAce: Boolean = false
)
