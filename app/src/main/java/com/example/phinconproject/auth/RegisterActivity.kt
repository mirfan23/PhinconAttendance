package com.example.phinconproject.auth

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.phinconproject.databinding.ActivityRegisterBinding
import com.example.phinconproject.models.UserCredentialModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity(){
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference("User")
        auth = FirebaseAuth.getInstance()

        setupAction()
        toLoginButton()
    }

    private fun setupAction() {
        binding.apply {
            emailEditText.addTextChangedListener {
                setButtonEnable()
            }

            passwordEditText.addTextChangedListener {
                setButtonEnable()
            }

            usernameEditText.addTextChangedListener {
                setButtonEnable()
            }

            repeatPassEditText.addTextChangedListener {
                setButtonEnable()
            }
            registerButton.setOnClickListener {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()
                val email = emailEditText.text.toString()

                val userCred = UserCredentialModel(email, username, password)
                database.child(username).setValue(userCred)
                    .addOnSuccessListener {
                        Log.d("username", "hasil username, password, email : ${userCred.username}, ${userCred.password}, ${userCred.email}")

                        AlertDialog.Builder(this@RegisterActivity).apply {
                            setTitle("Yeah!")
                            setMessage("Account successfully registered. Let's log you in!")
                            setPositiveButton("Continue") { _, _ ->
                                val intent = Intent(context, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@RegisterActivity, "Failed to register", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }


    private fun toLoginButton() {
        binding.apply {
            toLoginButton.setOnClickListener {
                val toLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(toLogin)
            }
        }
    }

    private fun setButtonEnable(){
        binding.apply {
            val usernameSet = usernameEditText.text
            val emailSet = emailEditText.text
            val passwordSet = passwordEditText.text
            val repeatPassSet = repeatPassEditText.text
            registerButton.isEnabled =
                usernameSet != null && usernameSet.toString()
                    .isNotEmpty() && passwordSet.toString().isNotEmpty() && passwordSet != null &&
                        emailSet != null && emailSet.toString()
                    .isNotEmpty() && repeatPassSet != null && repeatPassSet.toString().isNotEmpty()

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}