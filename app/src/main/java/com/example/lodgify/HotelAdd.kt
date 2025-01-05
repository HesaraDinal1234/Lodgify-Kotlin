package com.example.lodgify

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class HotelAdd : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_add)

        val btnRegisterHotel = findViewById<Button>(R.id.btn_register_hotel)

        btnRegisterHotel.setOnClickListener {
            saveHotel()
        }
    }

    private fun saveHotel() {
        val hotelName = findViewById<EditText>(R.id.et_hotel_name).text.toString()
        val hotelAddress = findViewById<EditText>(R.id.et_hotel_address).text.toString()
        val hotelPhone = findViewById<EditText>(R.id.et_hotel_phone).text.toString()
        val noOfRooms = findViewById<EditText>(R.id.et_no_of_rooms).text.toString()

        if (hotelName.isEmpty() || hotelAddress.isEmpty() || hotelPhone.isEmpty() || noOfRooms.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            val hotel = hashMapOf(
                "hotelName" to hotelName,
                "hotelAddress" to hotelAddress,
                "hotelPhone" to hotelPhone,
                "noOfRooms" to noOfRooms
            )

            db.collection("hotels")
                .add(hotel)
                .addOnSuccessListener {
                    Toast.makeText(this, "Hotel saved successfully!", Toast.LENGTH_SHORT).show()
                    findViewById<EditText>(R.id.et_hotel_name).text.clear()
                    findViewById<EditText>(R.id.et_hotel_address).text.clear()
                    findViewById<EditText>(R.id.et_hotel_phone).text.clear()
                    findViewById<EditText>(R.id.et_no_of_rooms).text.clear()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to save hotel: ${exception.message}", Toast.LENGTH_SHORT).show()
                    exception.printStackTrace()
                }
        }
    }
}
