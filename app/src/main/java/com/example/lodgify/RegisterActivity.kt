package com.example.lodgify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity()
{
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.input_email)
        val password = findViewById<EditText>(R.id.input_password)
        val register = findViewById<Button>(R.id.signup)
        val login = findViewById<Button>(R.id.signin)

        register.setOnClickListener {
            val Email = email.text.toString()
            val Password = password.text.toString()

            if (Email.isNotEmpty() && Password.isNotEmpty())
            {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                }
                else if (Password.length < 4)
                {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    registerUser(Email, Password)
                }
            }

        }
        login.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun registerUser(Email: String, Password: String)
    {
        auth.createUserWithEmailAndPassword(Email, Password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful)
                {
                    val user: FirebaseUser? = auth.currentUser
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}