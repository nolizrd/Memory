package com.example.meme

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.meme.Game.Game


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val game = Game(context = this)

        setContentView(game.generateGame())
    }
}