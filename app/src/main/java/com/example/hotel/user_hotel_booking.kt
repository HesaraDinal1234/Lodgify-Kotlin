package com.example.hotel

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class user_hotel_booking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_hotel_booking)

        val hotelSpinner = findViewById<Spinner>(R.id.hotelSpinner)
        val hotelNames = arrayOf("Hotel A", "Hotel B", "Hotel C")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hotelNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        hotelSpinner.adapter = adapter


    }
}