package com.example.meme.Game

import android.widget.ImageView
import com.example.meme.R

class Card(
    val cardView: ImageView,
    var linkCard: Card? = null,
    val imageCard: Int,
    val shirtCard: Int = R.drawable.lapka
) {

    init {
        cardView.setImageResource(shirtCard)
    }

    var isOpen: Boolean = false
        private set

    var isFoundPair: Boolean = false
        private set

    fun flipCard() {
        isOpen = !isOpen
        cardView.setImageResource(if (isOpen) imageCard else shirtCard)
    }
}
