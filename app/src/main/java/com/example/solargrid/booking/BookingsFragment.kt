package com.example.solargrid.booking

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.solargrid.R

class BookingsFragment : Fragment() {

    private lateinit var tabActive: TextView
    private lateinit var tabHistory: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_bookings,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val header = view.findViewById<View>(R.id.dashboardHeader)
            header?.setPadding(
                header.paddingLeft,
                systemBars.top + (12 * resources.displayMetrics.density).toInt(),
                header.paddingRight,
                header.paddingBottom
            )
            insets
        }

        view.findViewById<ImageView>(R.id.backButton)?.setOnClickListener {
            findNavController().navigate(R.id.nav_home)
        }

        tabActive = view.findViewById(R.id.bookingHistoryButton)
        tabHistory = view.findViewById(R.id.pendingHistoryButton)

        // Load default active bookings fragment
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.historyFragmentContainer, ActiveBookingsFragment())
                .commit()
        }

        tabActive.setOnClickListener {
            selectTab(true)
            childFragmentManager.beginTransaction()
                .replace(R.id.historyFragmentContainer, ActiveBookingsFragment())
                .commit()
        }

        tabHistory.setOnClickListener {
            selectTab(false)
            childFragmentManager.beginTransaction()
                .replace(R.id.historyFragmentContainer, BookingHistoryFragment())
                .commit()
        }
    }

    private fun selectTab(isActive: Boolean) {
        if (isActive) {
            tabActive.setBackgroundResource(R.drawable.history_tab_selected)
            tabActive.setTextColor(Color.WHITE)
            tabHistory.setBackgroundResource(R.drawable.history_tab_unselected)
            tabHistory.setTextColor(Color.parseColor("#B8C8C0"))
        } else {
            tabHistory.setBackgroundResource(R.drawable.history_tab_selected)
            tabHistory.setTextColor(Color.WHITE)
            tabActive.setBackgroundResource(R.drawable.history_tab_unselected)
            tabActive.setTextColor(Color.parseColor("#B8C8C0"))
        }
    }
}
