package com.example.solargrid.dashboard

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.solargrid.R
import com.example.solargrid.common.CaptureActivityPortrait
import com.example.solargrid.station.GridOperatorPowerStationSelection
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class GridOperatorHomeFragment : Fragment() {

    // Register Activity Result Launcher for ZXing QR Scanner
    private val qrScannerLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            val qrToken = result.contents
            finalizeEnergyTransfer(qrToken)
        } else {
            Toast.makeText(requireContext(), "QR Scan cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_grid_operator_home,
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

        // Nearby Stations Button -> Navigates to Grid Operator Power Station Selection Map
        view.findViewById<View>(R.id.nearbyStationsButton)?.setOnClickListener {
            val intent = Intent(requireContext(), GridOperatorPowerStationSelection::class.java)
            startActivity(intent)
        }

        // Scan QR Code Button -> Opens Camera QR Scanner
        view.findViewById<View>(R.id.scanQrButton)?.setOnClickListener {
            startQrScanner()
        }
    }

    private fun startQrScanner() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Scan Prosumer Energy Transfer QR Code")
            setCameraId(0)
            setBeepEnabled(true)
            setBarcodeImageEnabled(false)
            setOrientationLocked(true)
            setCaptureActivity(CaptureActivityPortrait::class.java)
        }
        qrScannerLauncher.launch(options)
    }

    /** Sends qrToken to POST /api/reservations/finalize-transfer and displays success dialog */
    private fun finalizeEnergyTransfer(qrToken: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_transfer_success)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setGravity(Gravity.CENTER)

        val scannedTokenText = dialog.findViewById<TextView>(R.id.scannedTokenText)
        val closeBtn = dialog.findViewById<View>(R.id.closeTransferDialogButton)

        scannedTokenText.text = qrToken

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(
            (320 * resources.displayMetrics.density).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}
