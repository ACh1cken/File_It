package com.example.fileit.reminder

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*


class DatePickerFragment : DialogFragment(), OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val pref = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
    }
}