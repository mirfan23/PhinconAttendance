package com.example.phinconproject.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phinconproject.adapters.AttendanceInAdapter
import com.example.phinconproject.databinding.FragmentHistoryBinding
import com.example.phinconproject.models.AttendanceModel
import com.example.phinconproject.session.UserSession
import com.google.firebase.database.*

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var checkInAdapter: AttendanceInAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInAdapter = AttendanceInAdapter()
        binding.rvLocationCheckIn.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLocationCheckIn.adapter = checkInAdapter

        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        val currentUser = UserSession(requireContext())
        val username = currentUser.passUsername().toString()
        val databaseRef= FirebaseDatabase.getInstance().reference.child("Attendance").child(username)

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val checkInList = ArrayList<AttendanceModel>()

                for (snapshot in dataSnapshot.children) {
                    val attendance = snapshot.getValue(AttendanceModel::class.java)
                    attendance?.let {
                        if (it.check_in?.isNotEmpty() == true) {
                            checkInList.add(it)
                        }
                    }
                }

                checkInAdapter.setHistory(checkInList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO()
            }
        })
    }
}