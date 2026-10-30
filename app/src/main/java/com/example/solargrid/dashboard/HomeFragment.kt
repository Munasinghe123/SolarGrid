package com.example.solargrid.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.solargrid.R
import com.example.solargrid.station.PowerStationSelection

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_home,
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

        val bookEnergyButton = view.findViewById<View>(R.id.bookEnergyButton)
        bookEnergyButton.setOnClickListener {
            val intent = Intent(
                requireContext(),
                PowerStationSelection::class.java
            )
            startActivity(intent)
        }
    }
}
