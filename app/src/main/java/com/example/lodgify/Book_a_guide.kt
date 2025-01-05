package com.example.lodgify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Book_a_guide : AppCompatActivity() {

    private lateinit var guideNameSpinner: Spinner
    private lateinit var customerNameEditText: EditText
    private lateinit var customerAddressEditText: EditText
    private lateinit var customerPhoneEditText: EditText
    private lateinit var travelCityEditText: EditText
    private lateinit var bookButton: Button
    private lateinit var showbooking: Button

    private val db = FirebaseFirestore.getInstance()

    private lateinit var guideNames: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_guide)

        guideNameSpinner = findViewById(R.id.guide_name_spinner)
        customerNameEditText = findViewById(R.id.customer_name)
        customerAddressEditText = findViewById(R.id.customer_address)
        customerPhoneEditText = findViewById(R.id.customer_phone)
        travelCityEditText = findViewById(R.id.travel_city)
        bookButton = findViewById(R.id.book_button)
        showbooking = findViewById(R.id.mybooking)

        showbooking.setOnClickListener {
            val intent = Intent(this, GuideBookingShowActivity::class.java)
            startActivity(intent)
        }

        fetchGuideNames()

        bookButton.setOnClickListener {
            bookGuide()
        }
    }

    private fun fetchGuideNames() {
        db.collection("guides")
            .get()
            .addOnSuccessListener { documents ->
                guideNames = mutableListOf()
                for (document in documents)
                {
                    val guideName = document.getString("name")
                    guideName?.let {
                        (guideNames as MutableList).add(it)
                    }
                }

                if (guideNames.isNotEmpty()) {
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, guideNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    guideNameSpinner.adapter = adapter
                }
                else
                {
                    Toast.makeText(this, "No guides available", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching guides: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun bookGuide() {
        val guideName = guideNameSpinner.selectedItem.toString()
        val customerName = customerNameEditText.text.toString()
        val customerAddress = customerAddressEditText.text.toString()
        val customerPhone = customerPhoneEditText.text.toString()
        val travelCity = travelCityEditText.text.toString()

        if (guideName.isEmpty() || customerName.isEmpty() || customerAddress.isEmpty() || customerPhone.isEmpty() || travelCity.isEmpty())
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
        else
        {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                val bookingData = hashMapOf(
                    "guideName" to guideName,
                    "customerName" to customerName,
                    "customerAddress" to customerAddress,
                    "customerPhone" to customerPhone,
                    "travelCity" to travelCity,
                    "userId" to it.uid
                )

                db.collection("guide_bookings")
                    .add(bookingData)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this, "Booking successful!", Toast.LENGTH_SHORT).show()

                        customerNameEditText.text.clear()
                        customerAddressEditText.text.clear()
                        customerPhoneEditText.text.clear()
                        travelCityEditText.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error booking guide: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
