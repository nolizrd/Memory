package com.example.meme.Game

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.meme.R

class Game(private val gridSize: Int = 4, private val context: Context) {

    private val layout = LinearLayout(context)
    private val rows = ArrayList<LinearLayout>(gridSize)
    private val cardViews = ArrayList<Card>(gridSize * 4)
    private val foundCards = ArrayList<Card>(gridSize * 4)

    var isFirstCardOpen = false
    var isFreezeActivity = false

    init {
        layout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layout.orientation = LinearLayout.VERTICAL
    }

    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.MATCH_PARENT,
        1.toFloat()
    )

    private val images = arrayOf(
        R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
        R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight
    )

    private val colorListener = { view: ImageView ->
        if (isFreezeActivity) return@OnClickListener

        if (isVictory()) {
            val toast = Toast.makeText(context, context.getString(R.string.win_text), Toast.LENGTH_LONG)
            toast.show()
            return@OnClickListener
        }

        val indexCard = cardViews.indexOfFirst { it.cardView == view }

        if (isFirstCardOpen) {
            if (cardViews[indexCard].linkCard?.isOpen == true) {
                cardViews[indexCard].flipCard()
                cardViews[indexCard].isFoundPair = true
                cardViews[indexCard].linkCard!!.isFoundPair = true
                foundCards.addAll(listOf(cardViews[indexCard].linkCard!!, cardViews[indexCard]))
            } else {
                cardViews[indexCard].flipCard()
                isFreezeActivity = true
                closeAllCard()
            }
            isFirstCardOpen = !isFirstCardOpen
        } else {
            if (!cardViews[indexCard].isFoundPair) {
                cardViews[indexCard].flipCard()
                isFirstCardOpen = !isFirstCardOpen
            }
        }
    }

    private fun generateCards() {
        for (n in 1..gridSize * 2) {
            val card = Card(ImageView(context).apply {
                layoutParams = params
                setOnClickListener(colorListener)
            }, images[n - 1])

            val card2 = Card(ImageView(context).apply {
                layoutParams = params
                setOnClickListener(colorListener)
            }, images[n - 1])

            card.linkCard = card2
            card2.linkCard = card
            cardViews.addAll(listOf(card, card2))
        }

        cardViews.shuffle()
    }

    private fun generateGame(): LinearLayout {
        for (i in 1..gridSize) {
            rows.add(LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = params
                for (j in 1..gridSize) {
                    addView(cardViews[(i - 1) * gridSize + (j - 1)].cardView)
                }
            })
            layout.addView(rows[i - 1])
        }

        return layout
    }

    private fun isVictory() = foundCards.size == gridSize * 4

    private fun closeAllCard() {
        android.os.Handler().postDelayed({
            for (card in cardViews) {
                if (!card.isFoundPair && card.isOpen) {
                    card.flipCard()
                }
            }
            isFreezeActivity = false
        }, 1000)
    }
}
