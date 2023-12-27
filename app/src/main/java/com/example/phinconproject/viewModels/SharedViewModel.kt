package com.example.phinconproject.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phinconproject.models.AttendanceModel

class SharedViewModel: ViewModel() {
    private val _attendanceData = MutableLiveData<AttendanceModel>()
    private val _isCheckIn = MutableLiveData<Boolean>()

    val attendanceData: LiveData<AttendanceModel>
        get() = _attendanceData

    fun setAttendanceData(data: AttendanceModel) {
        _attendanceData.value = data
    }

    val isCheckIn: LiveData<Boolean>
        get() = _isCheckIn

    fun setCheckIn(isCheckedIn: Boolean) {
        _isCheckIn.value = isCheckedIn
    }
}