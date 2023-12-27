package com.example.phinconproject.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.phinconproject.R
import com.example.phinconproject.auth.LoginActivity
import com.example.phinconproject.databinding.FragmentHistoryBinding
import com.example.phinconproject.databinding.FragmentProfileBinding
import com.example.phinconproject.models.UserCredentialModel
import com.example.phinconproject.session.UserSession
import com.example.phinconproject.viewModels.SharedViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentProfileBinding.inflate(inflater, container, false)
         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.getReference("User")

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        getProfileData()
        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun getProfileData() {
        binding.apply {
            val userSession = UserSession(requireContext())
            val currUser = userSession.passUsername()
            Log.d(
                "LoginActivity",
                "ID : Username : ${userSession.passUsername()}"
            )

            database.orderByChild("username").equalTo(currUser)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (user in snapshot.children) {
                            // do something with the individual "issues"
                            val userCred = user.getValue(UserCredentialModel::class.java)
                            userFullname.text = userCred?.username.toString()
                            noKaryawan.text = userCred?.email.toString()
                            alamat.text = "Jakarta"
//                            changePass.text = userCred?.password.toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast
                            .makeText(
                                requireContext(),
                                "Username/Password is wrong",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                })
        }
    }

    private fun logout() {
        // Membersihkan sesi pengguna atau data login yang relevan
        val userSession = UserSession(requireContext())
        userSession.clearSession() // Contoh: metode untuk membersihkan sesi pengguna

        // Kembali ke halaman login
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}