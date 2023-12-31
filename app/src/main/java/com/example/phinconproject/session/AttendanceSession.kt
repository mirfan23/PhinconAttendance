package com.example.phinconproject.session

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.phinconproject.R
import com.example.phinconproject.dashboard.HomeFragment


class AttendanceSession(context: Context) {
    companion object {
        private const val LOC_NAME_KEY = "location"
        private const val ADDR_KEY = "address"
        private const val IMG_KEY = "img"
        private const val DATE_KEY = "date"
        private const val ID_KEY = "id"
    }

    private val attendanceSession =
        context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE)

    fun saveLocName(loc_name: String) {
        val editor = attendanceSession.edit()
        editor.putString(LOC_NAME_KEY, loc_name)
        editor.apply()
    }

    fun passLocName(): String? {
        return attendanceSession.getString(LOC_NAME_KEY, "loc_name")
    }

    fun saveAddress(address: String) {
        val editor = attendanceSession.edit()
        editor.putString(ADDR_KEY, address)
        editor.apply()
    }

    fun passAddress(): String? {
        return attendanceSession.getString(ADDR_KEY, "address")
    }

    fun saveID(id: String) {
        val editor = attendanceSession.edit()
        editor.putString(ID_KEY, id)
        editor.apply()
    }

    fun passID(): String? {
        return attendanceSession.getString(ID_KEY, "attendanceID")
    }

    fun saveDate(date: String) {
        val editor = attendanceSession.edit()
        editor.putString(DATE_KEY, date)
        editor.apply()
    }

    fun passDate(): String? {
        return attendanceSession.getString(DATE_KEY, "date")
    }
}