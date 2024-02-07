package com.alphacorporations.blackjack.utils

import android.content.Context
import android.graphics.drawable.Drawable
import com.alphacorporations.blackjack.R

/**
 * Created by Julien Jennequin on 07/02/2024 13:06
 * Project : BlackJack
 **/
object Utils {
    fun getCardImage(name: String, context: Context): Drawable? {
        when (name) {
            //Clubs
            "clubs2" -> return context.getDrawable(R.drawable.clubs2)
            "clubs3" -> return context.getDrawable(R.drawable.clubs3)
            "clubs4" -> return context.getDrawable(R.drawable.clubs4)
            "clubs5" -> return context.getDrawable(R.drawable.clubs5)
            "clubs6" -> return context.getDrawable(R.drawable.clubs3)
            "clubs7" -> return context.getDrawable(R.drawable.clubs7)
            "clubs8" -> return context.getDrawable(R.drawable.clubs8)
            "clubs9" -> return context.getDrawable(R.drawable.clubs9)
            "clubs10" -> return context.getDrawable(R.drawable.clubs10)
            "clubs_ace" -> return context.getDrawable(R.drawable.clubs_ace)
            "clubs_jack" -> return context.getDrawable(R.drawable.clubs_jack)
            "clubs_queen" -> return context.getDrawable(R.drawable.clubs_queen)
            "clubs_king" -> return context.getDrawable(R.drawable.clubs_king)
            //Diamonds
            "diamonds2" -> return context.getDrawable(R.drawable.diamonds2)
            "diamonds3" -> return context.getDrawable(R.drawable.diamonds3)
            "diamonds4" -> return context.getDrawable(R.drawable.diamonds4)
            "diamonds5" -> return context.getDrawable(R.drawable.diamonds5)
            "diamonds6" -> return context.getDrawable(R.drawable.diamonds6)
            "diamonds7" -> return context.getDrawable(R.drawable.diamonds7)
            "diamonds8" -> return context.getDrawable(R.drawable.diamonds8)
            "diamonds9" -> return context.getDrawable(R.drawable.diamonds9)
            "diamonds10" -> return context.getDrawable(R.drawable.diamonds10)
            "diamonds_ace" -> return context.getDrawable(R.drawable.diamonds_ace)
            "diamonds_jack" -> return context.getDrawable(R.drawable.diamonds_jack)
            "diamonds_queen" -> return context.getDrawable(R.drawable.diamonds_queen)
            "diamonds_king" -> return context.getDrawable(R.drawable.diamonds_king)
            //Hearts
            "hearts2" -> return context.getDrawable(R.drawable.hearts2)
            "hearts3" -> return context.getDrawable(R.drawable.hearts3)
            "hearts4" -> return context.getDrawable(R.drawable.hearts4)
            "hearts5" -> return context.getDrawable(R.drawable.hearts5)
            "hearts6" -> return context.getDrawable(R.drawable.hearts6)
            "hearts7" -> return context.getDrawable(R.drawable.hearts7)
            "hearts8" -> return context.getDrawable(R.drawable.hearts8)
            "hearts9" -> return context.getDrawable(R.drawable.hearts9)
            "hearts10" -> return context.getDrawable(R.drawable.hearts10)
            "hearts_ace" -> return context.getDrawable(R.drawable.hearts_ace)
            "hearts_jack" -> return context.getDrawable(R.drawable.hearts_jack)
            "hearts_queen" -> return context.getDrawable(R.drawable.hearts_queen)
            "hearts_king" -> return context.getDrawable(R.drawable.hearts_king)
            //Spades
            "spades2" -> return context.getDrawable(R.drawable.spades2)
            "spades3" -> return context.getDrawable(R.drawable.spades3)
            "spades4" -> return context.getDrawable(R.drawable.spades4)
            "spades5" -> return context.getDrawable(R.drawable.spades5)
            "spades6" -> return context.getDrawable(R.drawable.spades6)
            "spades7" -> return context.getDrawable(R.drawable.spades7)
            "spades8" -> return context.getDrawable(R.drawable.spades8)
            "spades9" -> return context.getDrawable(R.drawable.spades9)
            "spades10" -> return context.getDrawable(R.drawable.spades10)
            "spades_ace" -> return context.getDrawable(R.drawable.spades_ace)
            "spades_jack" -> return context.getDrawable(R.drawable.spades_jack)
            "spades_queen" -> return context.getDrawable(R.drawable.spades_queen)
            "spades_king" -> return context.getDrawable(R.drawable.spades_king)
        }
        return context.getDrawable(R.drawable.back)
    }
}