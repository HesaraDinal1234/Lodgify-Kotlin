package com.example.lodgify

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Book_a_hotel : AppCompatActivity() {

    private lateinit var hotelNameSpinner: Spinner
    private lateinit var customerNameEditText: EditText
    private lateinit var customerAddressEditText: EditText
    private lateinit var customerPhoneEditText: EditText
    private lateinit var checkInDateEditText: EditText
    private lateinit var checkOutDateEditText: EditText
    private lateinit var bookButton: Button
    private lateinit var showBookingButton: Button

    private val db = FirebaseFirestore.getInstance()
    private lateinit var hotelNames: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_book)

        hotelNameSpinner = findViewById(R.id.hotel_name_spinner)
        customerNameEditText = findViewById(R.id.customer_name)
        customerAddressEditText = findViewById(R.id.customer_address)
        customerPhoneEditText = findViewById(R.id.customer_phone)
        checkInDateEditText = findViewById(R.id.check_in_date)
        checkOutDateEditText = findViewById(R.id.check_out_date)
        bookButton = findViewById(R.id.book_button)
        showBookingButton = findViewById(R.id.mybooking)

        showBookingButton.setOnClickListener {
            val intent = Intent(this, HotelBooking_show::class.java)
            startActivity(intent)
        }

        fetchHotelNames()

        bookButton.setOnClickListener {
            bookHotel()
        }
    }

    private fun fetchHotelNames() {
        db.collection("hotels")
            .get()
            .addOnSuccessListener { documents ->
                hotelNames = mutableListOf()

                for (document in documents) {
                    val hotelName = document.getString("hotelName")
                    hotelName?.let {
                        (hotelNames as MutableList).add(it)
                    }
                }

                if (hotelNames.isNotEmpty()) {
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hotelNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    hotelNameSpinner.adapter = adapter
                } else {
                    Toast.makeText(this, "No hotels available", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching hotels: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun bookHotel() {
        val hotelName = hotelNameSpinner.selectedItem.toString()
        val customerName = customerNameEditText.text.toString()
        val customerAddress = customerAddressEditText.text.toString()
        val customerPhone = customerPhoneEditText.text.toString()
        val checkInDate = checkInDateEditText.text.toString()
        val checkOutDate = checkOutDateEditText.text.toString()

        if (hotelName.isEmpty() || customerName.isEmpty() || customerAddress.isEmpty() || customerPhone.isEmpty() || checkInDate.isEmpty() || checkOutDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                val bookingData = hashMapOf(
                    "hotelName" to hotelName,
                    "customerName" to customerName,
                    "customerAddress" to customerAddress,
                    "customerPhone" to customerPhone,
                    "checkInDate" to checkInDate,
                    "checkOutDate" to checkOutDate,
                    "userId" to it.uid
                )

                db.collection("hotel_bookings")
                    .add(bookingData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Booking successful!", Toast.LENGTH_SHORT).show()
                        customerNameEditText.text.clear()
                        customerAddressEditText.text.clear()
                        customerPhoneEditText.text.clear()
                        checkInDateEditText.text.clear()
                        checkOutDateEditText.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error booking hotel: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
