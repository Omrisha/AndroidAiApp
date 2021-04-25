package com.shapiraomri.aiapplication.logic

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.shapiraomri.aiapplication.interfaces.Detector
import com.shapiraomri.aiapplication.ml.MobilenetV110224Quant
import com.shapiraomri.aiapplication.models.Word
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.absoluteValue

class ObjectDetectorImpl(context: Context): AbstractDetector(context) {

    override fun detect(bitmap: Bitmap): ArrayList<Word> {
        val words = ArrayList<Word>()
        val model = MobilenetV110224Quant.newInstance(context)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)

        val selectImage = TensorImage.fromBitmap(bitmap)
        val byteBuffer = selectImage.buffer

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val max = outputFeature0.floatArray.withIndex().filter { it.value / 255 > 0.6 }
        max.forEach { words.add(Word(labels[it.index], getPercent(it.value), 0f,0f, 0f, 0f)) }

        // Releases model resources if no longer used.
        model.close()

        return words
    }

    private fun getPercent(value: Float): Int {
        return ((value / 255) * 100).toInt()
    }
}