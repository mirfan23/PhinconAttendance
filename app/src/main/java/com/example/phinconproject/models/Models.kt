package com.example.phinconproject.models

data class SliderItemModel(
    val imageResId: Int,
    val title: String,
    val description: String
)

data class UserCredentialModel(
    val email: String? = "",
    val username: String? = "",
    val password: String? = "",
)

data class LocationModel(
    val img: String? = "",
    val loc_name: String? = "",
    val address: String? = ""
)

data class AttendanceModel(
    val attendanceID: String? ="",
    val date: String? ="",
    val loc_name: String? = "",
    val address: String? = "",
    val username: String? ="",
    val check_in: String? = "",
    val check_out: String? = "",
)
