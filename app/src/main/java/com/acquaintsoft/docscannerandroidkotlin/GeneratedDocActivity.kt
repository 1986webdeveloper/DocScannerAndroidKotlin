package com.acquaintsoft.docscannerandroidkotlin

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class GeneratedDocActivity : AppCompatActivity() {

    private var ivImg: ImageView? = null
    private var tvPath: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generated_doc)

        findViewById()

        val intent = intent

        if (intent != null) {
            val img = intent.getByteArrayExtra("img")
            val bmp = BitmapFactory.decodeByteArray(img, 0, img!!.size)
            ivImg?.setImageBitmap(bmp)

            tvPath?.setText(intent.getStringExtra("path"))
        }
    }

    private fun findViewById() {

        ivImg = findViewById(R.id.ivImg)
        tvPath = findViewById(R.id.tvPath)

    }
}
