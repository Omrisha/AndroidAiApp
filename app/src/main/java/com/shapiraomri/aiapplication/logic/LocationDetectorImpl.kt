package com.shapiraomri.aiapplication.logic

import android.content.Context
import android.graphics.Bitmap
import com.shapiraomri.aiapplication.interfaces.Detector
import com.shapiraomri.aiapplication.ml.MobilenetV110224Quant
import com.shapiraomri.aiapplication.ml.SsdMobilenetV11Metadata1
import com.shapiraomri.aiapplication.models.Word
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.metadata.MetadataExtractor
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader
import java.io.InputStreamReader

class LocationDetectorImpl(context: Context) : AbstractDetector(context) {

    override fun detect(bitmap: Bitmap): ArrayList<Word> {
        val words = ArrayList<Word>()
        val model = SsdMobilenetV11Metadata1.newInstance(context)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, false)

        val selectImage = TensorImage.fromBitmap(resized)

        // Runs model inference and gets result.
        val outputs = model.process(selectImage)
        val scores = outputs.scoresAsTensorBuffer
        val locations = outputs.locationsAsTensorBuffer
        val classes = outputs.classesAsTensorBuffer

        val maxScores = scores.floatArray.withIndex()
        val classLabel = classes.floatArray.withIndex()
        var index = 0
        for (score in maxScores) {
            if (score.value > 0.6) {
                index = score.index * 4
                val labelIndex = classes.floatArray[score.index]
                val ymin = locations.floatArray[index]
                val xmin = locations.floatArray[index+1]
                val ymax = locations.floatArray[index+2]
                val xmax = locations.floatArray[index+3]
                words.add(Word(labels[labelIndex.toInt()], getPercent(score.value), ymin, ymax , xmin, xmax))
            }
        }

        // Releases model resources if no longer used.
        model.close()

        return words
    }

    private fun getPercent(value: Float): Int {
        return ((value) * 100).toInt()
    }
}