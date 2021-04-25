package com.shapiraomri.aiapplication.adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import com.shapiraomri.aiapplication.models.Word

class ExtendedImageView(context: Context?) : ImageView(context) {
    private var words: ArrayList<Word> = ArrayList()
    fun setWords(words: ArrayList<Word>) {
        this.words = words
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }
}