package com.alphacorporations.blackjack.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alphacorporations.blackjack.databinding.ActivityMainBinding
import com.alphacorporations.blackjack.viewmodel.MainViewModel

/**
 * Created by Julien Jennequin on 06/02/2024 17:00
 * Project : BlackJack
 **/
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        initListener()

        setContentView(binding.root)

    }

    private fun initListener() {

        binding.playBtn.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }


}