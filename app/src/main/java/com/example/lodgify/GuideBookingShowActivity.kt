package com.example.lodgify

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class GuideBookingShowActivity : AppCompatActivity() {

    private lateinit var guideNameSpinner: Spinner
    private lateinit var bookingsListView: ListView
    private lateinit var noBookingsTextView: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_booking_show)

        guideNameSpinner = findViewById(R.id.guide_name_spinner)
        bookingsListView = findViewById(R.id.bookings_list_view)
        noBookingsTextView = findViewById(R.id.no_bookings_text)

        fetchGuideNames()

        guideNameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGuide = guideNameSpinner.selectedItem.toString()
                fetchBookingsForGuide(selectedGuide)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun fetchGuideNames() {
        db.collection("guides")
            .get()
            .addOnSuccessListener { documents ->
                val guideNames = mutableListOf<String>()
                for (document in documents) {
                    val guideName = document.getString("name") ?: ""
                    if (guideName.isNotEmpty()) {
                        guideNames.add(guideName)
                    }
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, guideNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                guideNameSpinner.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching guide names: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchBookingsForGuide(guideName: String) {
        db.collection("guide_bookings")
            .whereEqualTo("guideName", guideName)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    noBookingsTextView.visibility = TextView.VISIBLE
                    bookingsListView.visibility = ListView.GONE
                } else {
                    val bookingsList = mutableListOf<String>()
                    for (document in documents) {
                        val customerName = document.getString("customerName") ?: "Unknown"
                        val travelCity = document.getString("travelCity") ?: "Unknown"
                        val customerPhone = document.getString("customerPhone") ?: "Not Provided"
                        bookingsList.add("Customer: $customerName, City: $travelCity, Phone: $customerPhone")
                    }

                    noBookingsTextView.visibility = TextView.GONE
                    bookingsListView.visibility = ListView.VISIBLE

                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bookingsList)
                    bookingsListView.adapter = adapter
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching bookings: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
