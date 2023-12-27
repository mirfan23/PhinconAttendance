package com.example.phinconproject.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phinconproject.databinding.LocationItemBinding
import com.example.phinconproject.models.AttendanceModel
import java.util.*


class AttendanceInAdapter : RecyclerView.Adapter<AttendanceInAdapter.AttendanceViewHolder>() {

    private val getAttendance = ArrayList<AttendanceModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setHistory(location: ArrayList<AttendanceModel>) {
        getAttendance.clear()
        getAttendance.addAll(location)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding =
            LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val (_, _, loc_name, address, _, check_in, _) = getAttendance[position]
        holder.binding.locName.text =
            StringBuilder().append("Check In").append(" - ").append(loc_name).append(" - ")
                .append(check_in)
        holder.binding.locAddress.text = address
    }

    override fun getItemCount() = getAttendance.size

    class AttendanceViewHolder(var binding: LocationItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

class AttendanceOutAdapter : RecyclerView.Adapter<AttendanceOutAdapter.AttendanceViewHolder>() {

    private val getAttendance = ArrayList<AttendanceModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setHistory(location: ArrayList<AttendanceModel>) {
        getAttendance.clear()
        getAttendance.addAll(location)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding =
            LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val (_, _, loc_name, address, _, _, check_out) = getAttendance[position]
        holder.binding.locName.text =
            StringBuilder().append("Check Out").append(" - ").append(loc_name).append(" - ")
                .append(check_out)
        holder.binding.locAddress.text = address
    }

    override fun getItemCount() = getAttendance.size

    class AttendanceViewHolder(var binding: LocationItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}