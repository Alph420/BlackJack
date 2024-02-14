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
    val winnerLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val revealSecondDealerCard: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val dealerCards: MutableLiveData<MutableList<Card>> = MutableLiveData(mutableListOf())
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
        dealerCards.value!!.add(getCardAndRemoveItFromDeck(true))
        calculateScore(true)
        playerCards.add(getCardAndRemoveItFromDeck(true))
        dealerCards.value!!.add(getCardAndRemoveItFromDeck(false))
        initHandsLiveData.postValue(true)
        calculateScore()
    }

    private fun calculateScore2(isDealerHands: Boolean = false) {
        var score = 0
        var haveAce = false
        val hand = if (isDealerHands) dealerCards.value!! else playerCards

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
                    return

                } else {
                    playerHaveBlackJack.value = true
                    playerCanHit.postValue(false)
                    playerScoreLiveData.postValue("21")
                    return
                }

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
                    if (dealerCards.value!!.size == 2 && !dealerCards.value!![1].isVisible) {
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
            if (isDealerHands) {
                dealerScoreLiveData.value = score.toString()
            } else {
                playerScoreLiveData.value = score.toString()
            }

        }
    }

    fun calculateScore(isDealerHands: Boolean = false) {

        val hand = if (isDealerHands) dealerCards.value!! else playerCards
        var score = 0
        var hasAce = false

        for (card in hand) {
            val adjustedValue = when (card.value) {
                1 -> if (score < 11) 11 else 1 // Adjust ace value based on total score
                else -> card.value
            }
            score += adjustedValue
            hasAce = hasAce || card.isAce
        }

        // Update appropriate Live Data based on hand type and score
        if (isDealerHands) {
            if (score == 21 && hand.size == 2) {
                dealerHaveBlackJack.postValue(true)
                dealerCanHit.postValue(false)
                dealerScoreLiveData.value = "21"
            } else {
                dealerScoreLiveData.value = score.toString()
            }
            if (score > 21) {
                dealerIsBusted.postValue(true)
                dealer.isBusted = true
            }
        } else {
            if (score == 21 && hand.size == 2) {
                playerHaveBlackJack.postValue(true)
                playerCanHit.postValue(false)
                playerScoreLiveData.postValue("21")
            } else {
                if (hasAce) {
                    if (score + 10 > 21) {
                        playerScoreLiveData.postValue("$score")

                    } else {
                        playerScoreLiveData.postValue("${score}/${score + 10}")

                    }
                } else {
                    playerScoreLiveData.postValue(score.toString())
                }
            }
            if (score > 21) {
                playerIsBusted.postValue(true)
                player.isBusted = true
                isPlayerTurn = false
                isDealerTurn = true
            }
        }
    }

    fun playerDouble() {
        playerCards.add(getCardAndRemoveItFromDeck(true))
        calculateScore()
        playerCanHit.postValue(false)
        //TODO double the bet amount
        isPlayerTurn = false
    }

    private fun getDealerScore(): Int {

        var score = 0
        var hasAce = false

        for (card in dealerCards.value!!) {
            val adjustedValue = when (card.value) {
                1 -> if (score < 11) 11 else 1 // Adjust ace value based on total score
                else -> card.value
            }
            score += adjustedValue
            hasAce = hasAce || card.isAce
        }

        if (score == 21 && dealerCards.value!!.size == 2) {
            dealerHaveBlackJack.postValue(true)
            dealerCanHit.postValue(false)
            dealerScoreLiveData.postValue("21")
        } else {
            dealerScoreLiveData.postValue(score.toString())
        }
        if (score > 21) {
            dealerIsBusted.postValue(true)
            dealer.isBusted = true
        }
        calculateScore(true)

        return score
    }

    fun dealerReveal() {
        dealerCards.value!!.map {
            it.isVisible = true
        }


        if (getDealerScore() <= 17 && !player.isBusted && playerHaveBlackJack.value == false) {
            revealSecondDealerCard.postValue(true)
            dealerCards.value!!.add(getCardAndRemoveItFromDeck(true))
            if (getDealerScore() < 17 && !player.isBusted) {
                dealerCards.value!!.add(getCardAndRemoveItFromDeck(true))
                calculateScore2(true)
            } else {
                endGame()
            }
        } else {
            revealSecondDealerCard.postValue(true)
            endGame()
        }
    }

    fun endGame() {
        var playerScore = playerScoreLiveData.value!!.toInt()
        var dealerScore = dealerScoreLiveData.value!!.toInt()

        lateinit var winner: String


        if (dealer.isBusted && playerScore <= 21) winner = "Player"
        else if (player.isBusted && dealerScore <= 21) winner = "Dealer"
        else if (dealerScore == playerScore) {
            winner = "Push"
        } else if (dealerScore > playerScore) {
            winner = "Dealer"

        } else if (playerScore > dealerScore) {
            winner = "Player"
        }


        Log.d("winner is", winner)
        winnerLiveData.postValue(winner)
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
        dealerCards.value!!.clear()

        playerCanHit.postValue(true)
        resetUILiveData.postValue(true)
        clearAdaptersLiveData.postValue(true)

        playerHaveBlackJack.value = false
        player.isBusted = false

        isPlayerTurn = true
        isDealerTurn = false

        playerScoreLiveData.value = ""
        dealerScoreLiveData.value = ""
    }
}