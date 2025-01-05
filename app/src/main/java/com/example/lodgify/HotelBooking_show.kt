package com.example.lodgify

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class HotelBooking_show : AppCompatActivity() {

    private lateinit var hotelNameSpinner: Spinner
    private lateinit var bookingsListView: ListView
    private lateinit var noBookingsTextView: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_booking_show)

        hotelNameSpinner = findViewById(R.id.hotel_name_spinner)
        bookingsListView = findViewById(R.id.bookings_list_view)
        noBookingsTextView = findViewById(R.id.no_bookings_text)

        hotelNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedHotelName = parent?.getItemAtPosition(position).toString()
                fetchBookingsForHotel(selectedHotelName)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        fetchHotelNames()
    }

    private fun fetchHotelNames() {
        db.collection("hotels")
            .get()
            .addOnSuccessListener { documents ->
                val hotelNames = mutableListOf<String>()

                for (document in documents) {
                    val hotelName = document.getString("hotelName")
                    hotelName?.let {
                        hotelNames.add(it)
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

    private fun fetchBookingsForHotel(hotelName: String) {
        db.collection("hotel_bookings")
            .whereEqualTo("hotelName", hotelName)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    noBookingsTextView.visibility = View.VISIBLE
                    bookingsListView.visibility = View.GONE
                } else {
                    noBookingsTextView.visibility = View.GONE
                    bookingsListView.visibility = View.VISIBLE

                    val bookingList = mutableListOf<String>()

                    for (document in documents) {
                        val customerName = document.getString("customerName")
                        val checkInDate = document.getString("checkInDate")
                        val checkOutDate = document.getString("checkOutDate")
                        val customerPhone = document.getString("customerPhone")

                        customerName?.let {
                            val bookingDetails = "Name: $customerName\nPhone: $customerPhone\nCheck-In: $checkInDate\nCheck-Out: $checkOutDate"
                            bookingList.add(bookingDetails)
                        }
                    }

                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bookingList)
                    bookingsListView.adapter = adapter
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching bookings: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
