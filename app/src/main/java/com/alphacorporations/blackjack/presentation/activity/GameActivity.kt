package com.alphacorporations.blackjack.presentation.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alphacorporations.blackjack.databinding.ActivityGameBinding
import com.alphacorporations.blackjack.presentation.CardAdapter
import com.alphacorporations.blackjack.viewmodel.GameViewModel
import com.alphacorporations.blackjack.viewmodel.MainViewModel

/**
 * Created by Julien Jennequin on 06/02/2024 17:51
 * Project : BlackJack
 **/
class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var gameViewModel: GameViewModel


    private lateinit var playerAdapter: CardAdapter
    private lateinit var dealerAdapter: CardAdapter


    private var money = 1000
    private var isPlayerTurn = false
    private var isDealerTurn = false
    private var r = 0
    private var id = 0
    private var index = 0
    private var dealerIndex = 0
    private var dealerFirstCard = 0
    private var store = 0
    private var BET = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]

        gameViewModel.initDeck()
        initAdapters()
        //showCard()
        //init()
        initGame()
        initListener()
        initObserver()

        setContentView(binding.root)

    }


    private fun initAdapters() {
        playerAdapter = CardAdapter()
        dealerAdapter = CardAdapter()


        binding.dealerCards.adapter = dealerAdapter
        binding.playCards.adapter = playerAdapter

    }

    private fun initGame() {
        binding.moneyleft.text = "$money $"

        gameViewModel.initDeck()



        isPlayerTurn = true

        /* for (i in 0 until 4) {
             r = rand.nextInt(cardList.size)
             id = cardList[r]
             val name = resources.getResourceEntryName(id)
             // add card one by one
             if (i % 2 == 0) {
                 player.addCard(name)
                 val cardValue = findCardValue(name)
                 player.getTotal(cardValue)
                 if (cardValue == 11 && !player.ace) player.ace = true
                 else if (cardValue == 11 && player.ace) {
                     player.totalScore -= 10
                     player.ace = false
                 }
                 binding.score.text = "Score: " + player.totalScore
                 val x = playerCards[index].x
                 playerCards[index].translationX = -x;
                 playerCards[index].setImageResource(id)
                 moveTo(playerCards[index], 0f, 0f);
             } else {
                 dealer.addCard(name)
                 // who first one by one
                 if (dealerFirst) {
                     dealerFirstCard = id
                     dealerFirst = false
                 }
                 val cardValue = findCardValue(name)
                 if (cardValue == 11 && !dealer.ace) dealer.ace = true
                 else if (cardValue == 11 && dealer.ace) {
                     dealer.totalScore -= 10
                     dealer.ace = false
                 }
                 dealer.getTotal(cardValue)

                 if (index == 0) {
                     //figuring out how to make first card back.png
                     val x = dealerCards[index].x
                     dealerCards[index].translationX = -x;
                     dealerCards[index].setImageResource(R.drawable.back)
                     moveTo(dealerCards[index], 0f, 0f);
                     store = id
                 } else {
                     val x = playerCards[index].x
                     playerCards[index].translationX = -x;
                     dealerCards[index].setImageResource(id)
                     moveTo(playerCards[index], 0f, 0f);
                 }
                 index++
                 dealerIndex++
             }
             cardList.remove(id)
         }*/
    }


    private fun initListener() {

        binding.hit.setOnClickListener {
            gameViewModel.hit()
        }
        binding.stand.setOnClickListener {
            stand()
        }
        binding.doublation.setOnClickListener {

        }
        binding.split.setOnClickListener {

        }

        binding.newGame.setOnClickListener {
            gameViewModel.reset()
            initGame()
        }

        binding.bet.setOnClickListener {

            initGame()
        }

    }

    private fun initObserver() {

        gameViewModel.resetUILiveData.observe(this) {
            binding.BJ.visibility = View.INVISIBLE
        }

        gameViewModel.playerHaveBlackJack.observe(this) {
            if (it) binding.BJ.visibility = View.VISIBLE
            else binding.BJ.visibility = View.INVISIBLE
        }

        gameViewModel.playerScoreLiveData.observe(this) {
            binding.score.text = it.toString()
        }
        gameViewModel.initHandsLiveData.observe(this) {
            playerAdapter.setDataList(gameViewModel.playerCards)
            dealerAdapter.setDataList(gameViewModel.dealerCards)
        }

        gameViewModel.clearAdaptersLiveData.observe(this) {
            playerAdapter.clear()
            dealerAdapter.clear()
        }
    }

    private fun stand() {
        if (isPlayerTurn) isPlayerTurn = false
        dealerHit()
    }

    private fun dealerHit() {

    }


    fun moveTo(faceView: View, targetX: Float, targetY: Float) {

        val animSetXY = AnimatorSet()

        val x = ObjectAnimator.ofFloat(
            faceView, "translationX", faceView.translationX, targetX
        )

        val y = ObjectAnimator.ofFloat(
            faceView, "translationY", faceView.translationY, targetY
        )

        animSetXY.playTogether(x, y)
        animSetXY.duration = 300
        animSetXY.start()
    }


}