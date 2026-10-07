package com.example.solargrid

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.Locale

class BookEnergySlot : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_energy_slot)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Back button
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            startActivity(Intent(this, PowerStationSelection::class.java))
            finish()
        }

        // Station name from intent
        intent.getStringExtra("station_name")?.let { stationName ->
            findViewById<TextView>(R.id.selectedStationName).text = stationName
        }

        // Date Picker
        val dateButton = findViewById<TextView>(R.id.dateButton)
        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                dateButton.text = formattedDate
                dateButton.setTextColor(Color.WHITE)
            }, year, month, day).show()
        }

        // Start Time Picker
        val startTimeButton = findViewById<TextView>(R.id.startTimeButton)
        startTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                startTimeButton.text = formattedTime
                startTimeButton.setTextColor(Color.WHITE)
            }, hour, minute, true).show()
        }

        // End Time Picker
        val endTimeButton = findViewById<TextView>(R.id.endTimeButton)
        endTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                endTimeButton.text = formattedTime
                endTimeButton.setTextColor(Color.WHITE)
            }, hour, minute, true).show()
        }

        // Book Energy Slot Button
        findViewById<android.view.View>(R.id.bookEnergySlotButton).setOnClickListener {
            Toast.makeText(this, "Energy slot successfully booked!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, ProcumerDashboard::class.java))
            finish()
        }
    }
}
