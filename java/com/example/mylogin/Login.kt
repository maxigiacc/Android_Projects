package com.example.mylogin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var layoutE: TextInputLayout
    private lateinit var layoutP: TextInputLayout
    private lateinit var btnLogin: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar : ProgressBar
    private lateinit var textView: TextView

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[0-9])(?=.*[!@#\$%^&*()])(?=\\S+\$).{8,}\$")
        return passwordRegex.matches(password)
    }



    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editTextEmail = findViewById<TextInputEditText>(R.id.email)
        editTextPassword = findViewById<TextInputEditText>(R.id.password)
        btnLogin = findViewById<Button>(R.id.login_btn)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        textView = findViewById<TextView>(R.id.regNow)
        mAuth = Firebase.auth

        textView.setOnClickListener {
            val intent: Intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            var email : String = editTextEmail.text.toString()
            var password : String = editTextPassword.text.toString()

            if (TextUtils.isEmpty(email))
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()

            if (TextUtils.isEmpty(password))
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()

            if (!isEmailValid(email))
                Toast.makeText(this, "Not valid email", Toast.LENGTH_SHORT).show()

            if (!isPasswordValid(password))
                Toast.makeText(this, "Not valid password", Toast.LENGTH_SHORT).show()


            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(this, "Authentication succesfull.", Toast.LENGTH_SHORT,).show()
                        val intent: Intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                    }
                }
        }

    }
}