package com.alphacorporations.blackjack.viewmodel

import android.util.Log
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

    private var isPlayerTurn = true
    private var isDealerTurn = false

    private val rand = Random()

    //region LIVEDATA
    val playerScoreLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val dealerScoreLiveData: MutableLiveData<String> by lazy {
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
    val dealerHaveBlackJack: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val gameInProgressLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val BetInProgressLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val playerCanHit: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val dealerCanHit: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val playerIsBusted: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val dealerIsBusted: MutableLiveData<Boolean> by lazy {
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
        } else {
            calculateScore(true)
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
        calculateScore()
        dealerCards.add(getCardAndRemoveItFromDeck(true))
        calculateScore(true)
        playerCards.add(getCardAndRemoveItFromDeck(true))
        dealerCards.add(getCardAndRemoveItFromDeck(false))
        initHandsLiveData.postValue(true)
        calculateScore()
    }

    private fun calculateScore(isDealerHands: Boolean = false) {
        var score = 0
        var haveAce = false
        val hand = if (isDealerHands) dealerCards else playerCards

        hand.map {
            if (it.isAce) haveAce = true
            score += it.value
        }

        if (haveAce) {
            if (score + 10 == 21) {
                if (isDealerHands) {
                    dealerHaveBlackJack.postValue(true)
                    dealerCanHit.postValue(false)
                    dealerScoreLiveData.postValue("21")
                } else {
                    playerHaveBlackJack.postValue(true)
                    playerCanHit.postValue(false)
                    playerScoreLiveData.postValue("21")
                }

                return
            } else if (score + 10 > 21) {
                if (isDealerHands) {
                    if (score > 21) {
                        dealerIsBusted.postValue(true)
                        dealer.isBusted = true
                    }
                    dealerScoreLiveData.postValue(score.toString())

                } else {
                    if (score > 21) {
                        playerIsBusted.postValue(true)
                        player.isBusted = true
                        isPlayerTurn = false
                        isDealerTurn = true
                    }
                }
                playerScoreLiveData.postValue(score.toString())
            } else {
                if (isDealerHands) {
                    if (dealerCards.size == 2 && !dealerCards[1].isVisible) {
                        dealerScoreLiveData.postValue("$score")

                    } else {
                        dealerScoreLiveData.postValue("$score/${score + 10}")

                    }

                } else {
                    playerScoreLiveData.postValue("$score/${score + 10}")

                }

            }
        } else {

            if (score > 21) {
                if (isDealerHands) {
                    dealerIsBusted.postValue(true)
                    dealer.isBusted = true
                } else {
                    playerIsBusted.postValue(true)
                    player.isBusted = true
                    isPlayerTurn = false
                    isDealerTurn = true
                }

            }
            if (isDealerHands)
                dealerScoreLiveData.value = score.toString()
            else
                playerScoreLiveData.value = score.toString()

        }
    }

    fun dealerReveal() {
        dealerCards.map {
            it.isVisible = true
        }
        calculateScore(true)
        if (dealerScoreLiveData.value!!.toInt() <= 17) {
            dealerCards.add(getCardAndRemoveItFromDeck(true))
            calculateScore(true)

        }
    }

    fun endGame() {
        var playerScore = 0
        var dealerScore = 0
        playerCards.map { playerScore += it.value }
        dealerCards.map { dealerScore += it.value }

        val winner = if (dealer.isBusted && playerScore <= 21) "Player" else "Dealer"

        Log.d("winner is", winner)

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
        playerCanHit.postValue(true)
        resetUILiveData.postValue(true)
        clearAdaptersLiveData.postValue(true)
    }
}