package com.example.PhotoTree

import android.os.Bundle
import android.app.TimePickerDialog
import android.app.Dialog
import android.content.Context
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class TimePick : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    // Bundle sould be nullable, Bundle?
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(
            this.context as Context,
            activity as TimePickerDialog.OnTimeSetListener,
            hour,
            minute,
            true)
    }

    override fun onTimeSet(view: android.widget.TimePicker, hourOfDay: Int, minute: Int) {
        //
    }
}