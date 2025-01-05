package com.example.lodgify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity()
{
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.input_email)
        val password = findViewById<EditText>(R.id.input_password)
        val login = findViewById<Button>(R.id.signin)

        login.setOnClickListener {
            val Email = email.text.toString()
            val Password = password.text.toString()

            if (Email.isNotEmpty() && Password.isNotEmpty())
            {
                loginuser(Email, Password)
            }
            else
            {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loginuser(Email: String, Password: String)
    {
        auth.signInWithEmailAndPassword(Email, Password)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful)
                {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}