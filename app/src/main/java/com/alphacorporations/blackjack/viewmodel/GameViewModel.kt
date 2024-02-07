package com.alphacorporations.blackjack.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alphacorporations.blackjack.data.Card
import com.alphacorporations.blackjack.data.Player
import kotlinx.coroutines.launch
import java.util.Random

/**
 * Created by Julien Jennequin on 07/02/2024 15:56
 * Project : BlackJack
 **/
class GameViewModel : ViewModel() {

    var player = Player()
    var dealer = Player()

    val playerCards = ArrayList<Card>()
    val dealerCards = ArrayList<Card>()


    var deck = ArrayList<Card>()

    private var isPlayerTurn = false
    private var isDealerTurn = false

    private val rand = Random()

    //region LIVEDATA
    val playerScoreLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val clearAdaptersLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val resetUILiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val initHandsLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val playerHaveBlackJack: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    //endregion


    fun hit() {
        if (player.isBusted) {
            isPlayerTurn = false
            isDealerTurn = true
        }
        if (isPlayerTurn) {
            playerCards.add(getCardAndRemoveItFromDeck(true))
            calculateScore()
        }
    }

    fun initDeck() {
        val symbols = arrayOf("clubs", "diamonds", "hearts", "spades")
        val figures = arrayOf("king", "queen", "jack")
        val values = (2..12).toList()

        reset()

        viewModelScope.launch {
            values.flatMap { value ->
                symbols.map { symbol ->
                    if (value == 11) {
                        figures.map { figure ->
                            deck.add(Card(10, symbol, "${symbol}_${figure}"))
                        }
                    } else if (value == 12) {
                        deck.add(Card(1, symbol, "${symbol}_ace", isAce = true))

                    } else {
                        deck.add(Card(value, symbol, "${symbol}${value}"))
                    }
                }
            }
        }
        initHands()
    }

    fun initHands() {
        playerCards.add(getCardAndRemoveItFromDeck(true))
        dealerCards.add(getCardAndRemoveItFromDeck(true))
        playerCards.add(getCardAndRemoveItFromDeck(true))
        dealerCards.add(getCardAndRemoveItFromDeck(false))
        initHandsLiveData.postValue(true)
        calculateScore()

    }

    private fun calculateScore() {
        var score = 0
        var haveAce = false

        playerCards.map {
            if (it.isAce) haveAce = true
            score += it.value
        }

        if (haveAce) {
            if (score + 10 == 21) {
                playerHaveBlackJack.postValue(true)
                playerScoreLiveData.postValue("21")
                return
            }
            playerScoreLiveData.postValue("$score/${score + 10}")
        } else {
            if (score > 21) {
                player.isBusted = true

                isPlayerTurn = false
                isDealerTurn = true

            }
            playerScoreLiveData.postValue(score.toString())

        }
    }

    private fun getCardAndRemoveItFromDeck(isVisibleCard: Boolean): Card {
        val r = rand.nextInt(deck.size)
        val card = deck[r]
        card.isVisible = isVisibleCard
        deck.remove(card)

        return card
    }

    fun reset() {
        deck.clear()
        playerCards.clear()
        dealerCards.clear()
        resetUILiveData.postValue(true)
        clearAdaptersLiveData.postValue(true)
    }
}