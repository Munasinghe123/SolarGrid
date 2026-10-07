package com.example.solargrid

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_profile,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.backButton)?.setOnClickListener {
            findNavController().navigate(R.id.nav_home)
        }

        // Edit Profile Modal
        view.findViewById<TextView>(R.id.editProfileButton)?.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_edit_profile)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window?.setGravity(Gravity.CENTER)

            val nameInput = dialog.findViewById<EditText>(R.id.editNameInput)
            val emailInput = dialog.findViewById<EditText>(R.id.editEmailInput)
            val saveButton = dialog.findViewById<TextView>(R.id.saveButton)
            val cancelButton = dialog.findViewById<TextView>(R.id.cancelButton)

            nameInput.setText("Sankalpa Prosumer")
            emailInput.setText("sankalpa@solargrid.com")

            saveButton.setOnClickListener {
                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
            dialog.window?.setLayout(
                (300 * resources.displayMetrics.density).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

        // Deactivate Account Modal
        view.findViewById<TextView>(R.id.deactivateAccountButton)?.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_deactivate_account)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window?.setGravity(Gravity.CENTER)

            val confirmButton = dialog.findViewById<TextView>(R.id.confirmDeactivateButton)
            val cancelButton = dialog.findViewById<TextView>(R.id.cancelDeactivateButton)

            confirmButton.setOnClickListener {
                Toast.makeText(requireContext(), "Account deactivated successfully.", Toast.LENGTH_LONG).show()
                dialog.dismiss()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
            dialog.window?.setLayout(
                (300 * resources.displayMetrics.density).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }
}
