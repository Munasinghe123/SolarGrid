package com.example.solargrid.booking

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.solargrid.R
import com.example.solargrid.dashboard.ProcumerDashboard
import com.example.solargrid.station.PowerStationSelection
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookEnergySlot : AppCompatActivity() {

    private var selectedDateString: String = ""
    private var selectedStartTime: String = ""
    private var selectedEndTime: String = ""
    private val dateCardViews = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_book_energy_slot)

        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val header = findViewById<View>(R.id.dashboardHeader)
            header?.setPadding(
                header.paddingLeft,
                systemBars.top + (12 * resources.displayMetrics.density).toInt(),
                header.paddingRight,
                header.paddingBottom
            )
            insets
        }

        // Back button
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            startActivity(Intent(this, PowerStationSelection::class.java))
            finish()
        }

        // Change station close icon
        findViewById<TextView>(R.id.changeStationButton)?.setOnClickListener {
            startActivity(Intent(this, PowerStationSelection::class.java))
            finish()
        }

        // Station name from intent
        val stationName = intent.getStringExtra("station_name") ?: "Mahavali Sub"
        findViewById<TextView>(R.id.selectedStationName).text = stationName

        // Populate Upcoming 7 Days Date Selector
        setupUpcoming7DaysSelector()

        // Start Time Picker
        val startTimeButton = findViewById<TextView>(R.id.startTimeButton)
        startTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val amPm = if (selectedHour >= 12) "PM" else "AM"
                    val hour12 = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                    selectedStartTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour12, selectedMinute, amPm)
                    startTimeButton.text = selectedStartTime
                    startTimeButton.setTextColor(Color.WHITE)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }

        // End Time Picker
        val endTimeButton = findViewById<TextView>(R.id.endTimeButton)
        endTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val amPm = if (selectedHour >= 12) "PM" else "AM"
                    val hour12 = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                    selectedEndTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour12, selectedMinute, amPm)
                    endTimeButton.text = selectedEndTime
                    endTimeButton.setTextColor(Color.WHITE)
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }

        // Book Energy Slot Button
        findViewById<View>(R.id.bookEnergySlotButton).setOnClickListener {
            if (selectedStartTime.isEmpty() || selectedEndTime.isEmpty()) {
                Toast.makeText(this, "Please select start time and end time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Reservation confirmed for $stationName on $selectedDateString!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ProcumerDashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupUpcoming7DaysSelector() {
        val row1 = findViewById<LinearLayout>(R.id.datesRow1)
        val row2 = findViewById<LinearLayout>(R.id.datesRow2)

        row1?.removeAllViews()
        row2?.removeAllViews()
        dateCardViews.clear()

        val dayNumberFormat = SimpleDateFormat("dd", Locale.getDefault())
        val monthNameFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        val dayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val fullDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val inflater = LayoutInflater.from(this)

        for (i in 0 until 7) {
            val cal = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, i)
            }

            val dayNumber = dayNumberFormat.format(cal.time)
            val monthName = monthNameFormat.format(cal.time)
            val dayName = dayNameFormat.format(cal.time)
            val dateKey = fullDateFormat.format(cal.time)

            if (i == 0) {
                selectedDateString = dateKey
            }

            val cardView = inflater.inflate(R.layout.date_card_item, if (i < 5) row1 else row2, false)

            cardView.findViewById<TextView>(R.id.dateDayNumber).text = dayNumber
            cardView.findViewById<TextView>(R.id.dateMonthName).text = monthName
            cardView.findViewById<TextView>(R.id.dateDayName).text = dayName

            if (i == 0) {
                cardView.setBackgroundResource(R.drawable.date_card_selected)
            } else {
                cardView.setBackgroundResource(R.drawable.date_card_bg)
            }

            val index = i
            cardView.setOnClickListener {
                selectedDateString = dateKey
                dateCardViews.forEachIndexed { idx, v ->
                    if (idx == index) {
                        v.setBackgroundResource(R.drawable.date_card_selected)
                    } else {
                        v.setBackgroundResource(R.drawable.date_card_bg)
                    }
                }
            }

            dateCardViews.add(cardView)

            if (i < 5) {
                row1?.addView(cardView)
            } else {
                row2?.addView(cardView)
            }
        }
    }
}
