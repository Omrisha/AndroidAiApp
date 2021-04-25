package com.shapiraomri.aiapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shapiraomri.aiapplication.adapters.DetectWordAdapter
import com.shapiraomri.aiapplication.interfaces.Detector
import com.shapiraomri.aiapplication.logic.LocationDetectorImpl
import com.shapiraomri.aiapplication.logic.ObjectDetectorImpl
import com.shapiraomri.aiapplication.models.Word

private const val TAG = "MAIN_ACTIVITY"
// labels.txt for the image classification
// labelmap.txt for the object detection
private const val FILENAME = "labelmap.txt"
class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var wordList: RecyclerView
    private lateinit var detectorLogic: Detector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()
    }

    private fun initComponents() {
        imageView = findViewById(R.id.image_to_detect)
        wordList = findViewById(R.id.probability_list)
        detectorLogic = LocationDetectorImpl(this)
        detectorLogic.initialize(FILENAME)

        val selectButton = findViewById<Button>(R.id.button_select)
        selectButton.setOnClickListener(onSelectPhotoClickListener())

        val detectButton = findViewById<Button>(R.id.button_detect)
        detectButton.setOnClickListener(onDetectButtonPress())
    }

    private fun onSelectPhotoClickListener() : View.OnClickListener {
        return View.OnClickListener {
            val photoSelect = Intent(Intent.ACTION_PICK)
            photoSelect.type = "image/*"

            startActivityForResult(photoSelect, 1)
        }
    }

    private fun onDetectButtonPress() : View.OnClickListener {
        return View.OnClickListener {
            val bm = (imageView.drawable as BitmapDrawable).bitmap

            val resized = Bitmap.createScaledBitmap(bm, 224, 224, true)
            val startTime = SystemClock.uptimeMillis()
            val words = detectorLogic.detect(resized)

            val widthRatio = bm.width
            val heightRatio = bm.height
            val canvas = Canvas(bm)
            for (word in words) {
                val paint = Paint()
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 5.0f
                paint.color = Color.RED
                canvas.drawRect(word.left * widthRatio, word.top * heightRatio, word.right * widthRatio, word.bottom * heightRatio, paint)
            }
            imageView.setImageBitmap(bm)
            val lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime
            Log.d(TAG, "Detect in $lastProcessingTimeMs ms")
            val wordAdapter = DetectWordAdapter(words)
            wordList.apply {
                layoutManager = GridLayoutManager(context, 1)
                adapter = wordAdapter
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            Glide
                .with(this)
                .load(data?.data)
                .into(imageView)
            wordList.apply {
                adapter = DetectWordAdapter(ArrayList<Word>())
            }
        } else {
            Toast.makeText(this, "You haven't picked an image", Toast.LENGTH_SHORT).show()
        }
    }
}