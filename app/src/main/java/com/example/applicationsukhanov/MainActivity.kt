package com.example.applicationsukhanov

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var sp = getSharedPreferences("PC", Context.MODE_PRIVATE)
        if (sp.getString("TY", "-9") != "-9"){
            startActivity(Intent(this, signup::class.java))
        }
        else {
            var signuptext: TextView = findViewById(R.id.signuptext)
            signuptext.setOnClickListener {
                var intent = Intent(this, signup::class.java)
                startActivity(intent)
            }
            var email:TextView = findViewById(R.id.email)
            var password:TextView = findViewById(R.id.password)
            var button: ConstraintLayout = findViewById(R.id.button)
            var db = Firebase.firestore

            button.setOnClickListener{
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            if (document.getString("email")==email.text){
                                if (document.getString("password")==password.text){
                                   
                                    sp.edit().putString("email", email.text.toString()).commit()
                                    startActivity(Intent(this, MainActivity2::class.java))
                                }
                                else if (document.getString("password")!=password.text){
                                    password.text = ""
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Не получилось", Toast.LENGTH_LONG).show()
                    }

            }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}