package com.example.phinconproject.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.phinconproject.dashboard.DashboardActivity
import com.example.phinconproject.databinding.ActivityLoginBinding
import com.example.phinconproject.models.UserCredentialModel
import com.example.phinconproject.session.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference("User")
        firebaseAuth = FirebaseAuth.getInstance()

        setupAction()
        isForgotPassword()
        registerButton()
    }

    private fun setupAction() {
        binding.apply {
            usernameEditText.addTextChangedListener {
                setButtonEnable()
            }
            passwordEditText.addTextChangedListener {
                setButtonEnable()
            }
            loginButton.setOnClickListener {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()

                showLoading(true)
                database.orderByChild("username").equalTo(username)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            showLoading(false)
                            if (snapshot.exists()) {
                                for (user in snapshot.children) {
                                    val userCred = user.getValue(UserCredentialModel::class.java)

                                    if (userCred?.password.equals(password)) {
                                        showLoading(false)

                                        performLogin(userCred?.username)
                                        return
                                    }
                                }
                                showLoading(false)
                                Toast.makeText(this@LoginActivity, "Username/Password is wrong", Toast.LENGTH_SHORT).show()
                            } else {
                                showLoading(false)
                                Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("FirebaseDatabase", "Error getting data", error.toException())
                            Toast.makeText(this@LoginActivity, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    private fun performLogin(username: String?) {
        val userSession = UserSession(this@LoginActivity)
        username?.let {
            userSession.saveUsername(it)
        }

        AlertDialog.Builder(this@LoginActivity).apply {
            setTitle("Yeah!")
            setMessage("You are now logging in. Let's connect!")
            setPositiveButton("Continue") { _, _ ->
                val intent = Intent(context, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun isForgotPassword() {
        binding.apply {
            forgotPasswordButton.setOnClickListener {
                val toForgotPass = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                startActivity(toForgotPass)
            }
        }
    }

    private fun registerButton() {
        binding.apply {
            registerButton.setOnClickListener {
                val toRegister= Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(toRegister)
            }
        }
    }

    private fun setButtonEnable() {
        binding.apply {
            val usernameSet = usernameEditText.text
            val passwordSet = passwordEditText.text
            loginButton.isEnabled =
                usernameSet != null && passwordSet !=null && usernameSet.toString()
                    .isNotEmpty() && passwordSet.toString().isNotEmpty()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
