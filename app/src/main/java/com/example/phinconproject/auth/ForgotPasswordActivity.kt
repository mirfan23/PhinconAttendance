package com.example.phinconproject.auth
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.view.WindowManager
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat.startActivity
//import androidx.core.widget.addTextChangedListener
//import androidx.databinding.DataBindingUtil.setContentView
//import com.example.phinconproject.databinding.ActivityForgotPasswordBinding
//import com.example.phinconproject.models.UserCredentialModel
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
//
//class ForgotPasswordActivity: AppCompatActivity() {
//    private lateinit var binding: ActivityForgotPasswordBinding
//    private lateinit var database: DatabaseReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        database = Firebase.database.getReference("User")
//
//
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//
//        setupAction()
//        toLoginButton()
//    }
//
//    private fun setMyButtonEnable() {
//        binding.apply {
//            val usernameSet = usernameEditText.text
//            val passwordSet = passwordEditText.text
//            resetButton.isEnabled =
//                usernameSet != null && passwordSet != null && usernameSet.toString()
//                    .isNotEmpty() && passwordSet.toString().isNotEmpty()
//        }
//    }
//
//    private fun setupAction(){
//        binding.apply {
//            usernameEditText.addTextChangedListener {
//                setMyButtonEnable()
//            }
//
//            passwordEditText.addTextChangedListener {
//                setMyButtonEnable()
//            }
//
//            resetButton.setOnClickListener {
//                val username = usernameEditText.text.toString()
//                val password = passwordEditText.text.toString()
//
//                showLoading(true)
//                database.orderByChild("username").equalTo(username)
//                    .addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for (user in snapshot.children) {
//                                // do something with the individual "issues"
//                                val userCred = user.getValue(UserCredentialModel::class.java)
//                                if (userCred?.password.equals(password)
//                                ) {
//                                    showLoading(false)
//                                    database.child(userCred?.username.toString()).removeValue()
//                                    Log.d("username", "hasil ktp : ${userCred?.username}")
//                                    Log.d("password", "hasil password : ${userCred?.password}")
//
//                                    AlertDialog.Builder(this@ForgotPasswordActivity).apply {
//                                        setTitle("Yeah!")
//                                        setMessage("Account successfully reset. Let's sign you up again!")
//                                        setPositiveButton("Continue") { _, _ ->
//                                            val intent = Intent(context, RegisterActivity::class.java)
//                                            intent.flags =
//                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                                            startActivity(intent)
//                                            finish()
//                                        }
//                                        create()
//                                        show()
//                                    }
//                                }
//                                else {
//                                    Toast
//                                        .makeText(
//                                            this@ForgotPasswordActivity,
//                                            "Username/Password is wrong",
//                                            Toast.LENGTH_SHORT
//                                        )
//                                        .show()
//                                }
//
//                            }
//
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            Toast
//                                .makeText(
//                                    this@ForgotPasswordActivity,
//                                    "Username/Password is wrong",
//                                    Toast.LENGTH_SHORT
//                                )
//                                .show()
//                        }
//
//                    })
//            }
//        }
//    }
//
//    private fun toLoginButton() {
//        binding.apply {
//            toLoginButton.setOnClickListener {
//                val toLogin = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
//                startActivity(toLogin)
//            }
//        }
//    }
//
//    private fun showLoading(isLoading: Boolean) {
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
//}

//package com.example.phinconproject.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.phinconproject.databinding.ActivityForgotPasswordBinding
import com.example.phinconproject.models.UserCredentialModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("User")

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )

        setupAction()
        toLoginButton()
    }

    private fun setMyButtonEnable() {
        binding.apply {
            val usernameSet = usernameEditText.text
            val passwordSet = passwordEditText.text
            resetButton.isEnabled =
                usernameSet != null && passwordSet != null && usernameSet.toString()
                    .isNotEmpty() && passwordSet.toString().isNotEmpty()
        }
    }

    private fun setupAction() {
        binding.apply {
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Implementation before text changed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Implementation on text changed
                    setMyButtonEnable()
                }

                override fun afterTextChanged(s: Editable?) {
                    // Implementation after text changed
                }
            }

            usernameEditText.addTextChangedListener(textWatcher)
            passwordEditText.addTextChangedListener(textWatcher)


            resetButton.setOnClickListener {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()

                showLoading(true)
                database.orderByChild("username").equalTo(username)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (user in snapshot.children) {
                                    val userCred = user.getValue(UserCredentialModel::class.java)
                                    if (userCred?.password == password) {
                                        showLoading(false)
                                        // Reset password logic di sini
                                        // Hapus data pengguna dari Firebase
                                        user.ref.removeValue()

                                        // Menampilkan pesan bahwa akun berhasil direset
                                        AlertDialog.Builder(this@ForgotPasswordActivity).apply {
                                            setTitle("Yeah!")
                                            setMessage("Account successfully reset. Let's sign you up again!")
                                            setPositiveButton("Continue") { _, _ ->
                                                val intent = Intent(context, RegisterActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(intent)
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                        return // Keluar dari loop setelah menemukan pengguna
                                    }
                                }
                                showLoading(false)
                                Toast.makeText(this@ForgotPasswordActivity, "Username/Password is wrong", Toast.LENGTH_SHORT).show()
                            } else {
                                showLoading(false)
                                Toast.makeText(this@ForgotPasswordActivity, "User not found", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            showLoading(false)
                            Toast.makeText(this@ForgotPasswordActivity, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    private fun toLoginButton() {
        binding.apply {
            toLoginButton.setOnClickListener {
                val toLogin = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
                startActivity(toLogin)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
