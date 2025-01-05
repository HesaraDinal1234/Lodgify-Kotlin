package com.example.lodgify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val bookHotelButton = findViewById<Button>(R.id.book_hotel)
        val bookGuideButton = findViewById<Button>(R.id.book_guide)
        val addHotelButton = findViewById<Button>(R.id.addhotel)
        val addGuideButton = findViewById<Button>(R.id.addguide)

        bookHotelButton.setOnClickListener {
            val intent = Intent(this, Book_a_hotel::class.java)
            startActivity(intent)
        }
        bookGuideButton.setOnClickListener {
            val intent = Intent(this, Book_a_guide::class.java)
            startActivity(intent)
        }
        addHotelButton.setOnClickListener {
            val intent = Intent(this, HotelAdd::class.java)
            startActivity(intent)
        }
        addGuideButton.setOnClickListener {
            val intent = Intent(this, GuideAddActivity::class.java)
            startActivity(intent)
        }

    }
}