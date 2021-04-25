package com.shapiraomri.aiapplication.logic

import android.content.Context
import com.shapiraomri.aiapplication.interfaces.Detector
import java.io.BufferedReader
import java.io.InputStreamReader

abstract class AbstractDetector(val context: Context) : Detector {
    var labels: ArrayList<String> = ArrayList()

    override fun initialize(filename: String) {
        val bufferReader = BufferedReader(InputStreamReader(context.assets.open(filename)))
        var line = bufferReader.readLine()

        while (line != null) {
            labels.add(line)
            line = bufferReader.readLine()
        }
    }
}