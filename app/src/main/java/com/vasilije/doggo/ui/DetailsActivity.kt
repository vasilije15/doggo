package com.vasilije.doggo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.vasilije.doggo.R


class DetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val pictureUrl = intent?.getStringExtra("pictureUrl")
        val breed = intent?.getStringExtra("breed")


        val imageViewDog = findViewById<ImageView>(R.id.dogDetailsImageView)
        Glide.with(this).load(pictureUrl).into(imageViewDog)

        val breedTextView = findViewById<TextView>(R.id.dogDetailsShortText)
        breedTextView.text = breed

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener(View.OnClickListener {
            finish()
        })

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}