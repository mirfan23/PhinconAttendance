package com.example.phinconproject.dashboard


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phinconattendance.viewModels.LocationViewModel
import com.example.phinconproject.adapters.AttendanceInAdapter
import com.example.phinconproject.adapters.LocationAdapter
import com.example.phinconproject.databinding.FragmentHomeBinding
import com.example.phinconproject.models.AttendanceModel
import com.example.phinconproject.models.LocationModel
import com.example.phinconproject.session.AttendanceSession
import com.example.phinconproject.session.UserSession
import com.example.phinconproject.viewModels.SharedViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var attendanceInAdapter: AttendanceInAdapter
    private lateinit var database: DatabaseReference
    private lateinit var viewModel: LocationViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var attendanceId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.getReference("Attendance")

        locationAdapter = LocationAdapter()

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val session = AttendanceSession(requireContext())
        val savedID = session.passID()

        if (savedID != null) {
            attendanceId = savedID
        } else {
            Log.d("id null", "$id")
        }

        binding.rvLocation.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLocation.setHasFixedSize(true)
        binding.rvLocation.adapter = locationAdapter

        locationAdapter.notifyDataSetChanged()

        setupViewModel()
        setupAdapter()

        doCheckIn()
        doCheckOut()
        observeCheckInStatus()
    }

    private fun setupAdapter() {
        locationAdapter = LocationAdapter()

        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        viewModel.getLocation.observe(viewLifecycleOwner) { locations ->
            locations?.let {
                locationAdapter.setLocation(it)
                binding.rvLocation.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    setHasFixedSize(true)
                    adapter = locationAdapter // Gunakan adapter yang sudah diinisialisasi
                }
                locationAdapter.notifyDataSetChanged()
            } ?: run {
                Toast.makeText(requireContext(), "Location is not exist", Toast.LENGTH_SHORT).show()
            }
        }

        locationAdapter.setOnItemClickCallback(object : LocationAdapter.OnItemClickCallback {
            override fun onItemClicked(data: LocationModel) {

            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[LocationViewModel::class.java]
    }

    @SuppressLint("SimpleDateFormat")
    private fun doCheckIn() {
        binding.apply {
            checkButtonIn.visibility = View.VISIBLE
            locationAdapter.setOnItemClickCallback(object : LocationAdapter.OnItemClickCallback {

                override fun onItemClicked(data: LocationModel) {
                    val today = Date(System.currentTimeMillis())
                    val todayDate = SimpleDateFormat("yyyy-MM-dd").format(today)

                    val session = AttendanceSession(requireContext())
                    session.saveDate(todayDate)
                    session.saveAddress(data.address.toString())
                    session.saveLocName(data.loc_name.toString())
                }
            })
            checkButtonIn.setOnClickListener {
                val sessUser = UserSession(requireContext())
                val username = sessUser.passUsername()

                Log.d("Username", "The retrieved username is: $username")

                val session = AttendanceSession(requireContext())
                val date = session.passDate()
                val loc_name = session.passLocName()
                val address = session.passAddress()

                val today = Date(System.currentTimeMillis())
                val todayDate = SimpleDateFormat("yyyy-MM-dd").format(today)
                val todayHour = SimpleDateFormat("hh:mm a").format(today)

                val id = database.child(username.toString()).push().key
                session.saveID(id.toString())
                attendanceId = id.toString()

                checkButtonIn.visibility = View.GONE

                val todayAttendance = AttendanceModel(id, todayDate, loc_name, address, username, todayHour)
                sharedViewModel.setAttendanceData(todayAttendance)
                sharedViewModel.setCheckIn(true)

                database.child(username.toString()).child(id.toString()).setValue(todayAttendance)
                    .addOnSuccessListener {
                        Log.d("getting who's checking in now", "$id, $username at $date $todayHour")

                            attendanceInAdapter = AttendanceInAdapter()

                            viewModel.getAttendanceCheckInUser(username.toString(), id.toString())

                            viewModel.checkInAttendance.observe(requireActivity()) {
                                if (it != null) {
                                    attendanceInAdapter.setHistory(it)

                                    rvLocation.layoutManager = LinearLayoutManager(requireContext())
                                    rvLocation.setHasFixedSize(true)
                                    rvLocation.adapter = attendanceInAdapter

                                    checkButtonOut.visibility = View.VISIBLE

                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Let's get you check in first",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to check in.", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            doCheckOut()
        }

    }

    private fun doCheckOut() {
        binding.apply {
            checkButtonOut.setOnClickListener {
                val sessUser = UserSession(requireContext())
                val username = sessUser.passUsername()

                val session = AttendanceSession(requireContext())
                val date = session.passDate()

                val savedAttendanceId = attendanceId

                val today = Date(System.currentTimeMillis())
                val todayHour = SimpleDateFormat("hh:mm a").format(today)

                checkButtonOut.visibility = View.VISIBLE

                savedAttendanceId?.let { id ->
                    database.child(username.toString()).child(id).child("check_out")
                        .setValue(todayHour)
                        .addOnSuccessListener {
                            checkButtonOut.visibility = View.GONE
                            checkButtonIn.visibility = View.VISIBLE
                            sharedViewModel.setCheckIn(false) // Update status check-in
                            Log.d(
                                "getting who's checking out now",
                                "$it, $username at $date $todayHour"
                            )
                            observeCheckInStatus()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Failed to check out.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
    }

        private fun observeCheckInStatus() {
            sharedViewModel.isCheckIn.observe(viewLifecycleOwner) { isCheckIn ->
                if (isCheckIn) {
                    binding.checkButtonIn.visibility = View.GONE
                    binding.checkButtonOut.visibility = View.VISIBLE
                } else {
                    binding.checkButtonIn.visibility = View.VISIBLE
                    binding.checkButtonOut.visibility = View.GONE
                }
            }
        }


        override fun onResume() {
            super.onResume()
            observeCheckInStatus()
        }

        companion object {
            fun newInstance(sharedViewModel: SharedViewModel): HomeFragment {
                val fragment = HomeFragment()
                fragment.sharedViewModel = sharedViewModel

                return fragment
            }
        }
    }
