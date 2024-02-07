package com.alphacorporations.blackjack.data

/**
 * A "Player" class
 */
data class Player(
    var first_name: String = "",
    var last_name: String = "",
    var winTimes: Int = 0,
    var lossTimes: Int = 0,
    var winRate: Double = 0.00,
    var money: Int = 500,
    var totalScore: Int = 0,
    var ace: Boolean = false,
    var isBusted: Boolean = false,
    var arrayList: ArrayList<String> = ArrayList()
) {


    fun addCard(a: String) {
        arrayList.add(a)
    }

    fun getTotal(b: Int): Int {
        this.totalScore += b
        return this.totalScore
    }

    fun addWinTimes(): Int {
        this.winTimes += 1
        return winTimes
    }

    fun addLossTimes(): Int {
        this.lossTimes += 1
        return lossTimes
    }

    fun updateWinRate(): Double {
        var playTime = winTimes.toFloat() + lossTimes.toDouble()
        winRate = winTimes.toDouble() / playTime
        return this.winRate
    }

    fun Moneyminus(BET: Int): Int {
        this.money -= BET
        return this.money
    }

    fun Moneyadd(BET: Int): Int {
        this.money += BET
        return this.money
    }
}