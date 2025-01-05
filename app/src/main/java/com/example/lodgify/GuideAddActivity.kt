package com.example.lodgify

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lodgify.R
import com.google.firebase.firestore.FirebaseFirestore

class GuideAddActivity : AppCompatActivity() {

    private lateinit var guideNameEditText: EditText
    private lateinit var guideIdEditText: EditText
    private lateinit var guideAddressEditText: EditText
    private lateinit var guidePhoneNumberEditText: EditText
    private lateinit var saveGuideButton: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guide)

        guideNameEditText = findViewById(R.id.guide_name)
        guideIdEditText = findViewById(R.id.guide_id_number)
        guideAddressEditText = findViewById(R.id.guide_address)
        guidePhoneNumberEditText = findViewById(R.id.guide_phone_number)
        saveGuideButton = findViewById(R.id.save_guide_button)

        saveGuideButton.setOnClickListener {
            saveGuideData()
        }
    }

    private fun saveGuideData() {
        val guideName = guideNameEditText.text.toString()
        val guideId = guideIdEditText.text.toString()
        val guideAddress = guideAddressEditText.text.toString()
        val guidePhoneNumber = guidePhoneNumberEditText.text.toString()

        if (guideName.isEmpty() || guideId.isEmpty() || guideAddress.isEmpty() || guidePhoneNumber.isEmpty())
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
        else {
            val guideData = hashMapOf(
                "name" to guideName,
                "idNumber" to guideId,
                "address" to guideAddress,
                "phoneNumber" to guidePhoneNumber
            )

            db.collection("guides")
                .add(guideData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Guide saved successfully!", Toast.LENGTH_LONG).show()

                    guideNameEditText.text.clear()
                    guideIdEditText.text.clear()
                    guideAddressEditText.text.clear()
                    guidePhoneNumberEditText.text.clear()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving guide: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
