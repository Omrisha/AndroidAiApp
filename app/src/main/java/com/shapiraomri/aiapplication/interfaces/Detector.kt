package com.shapiraomri.aiapplication.interfaces

import android.graphics.Bitmap
import com.shapiraomri.aiapplication.models.Word

interface Detector {
    fun initialize(filename: String)
    fun detect(bitmap: Bitmap) : ArrayList<Word>
}