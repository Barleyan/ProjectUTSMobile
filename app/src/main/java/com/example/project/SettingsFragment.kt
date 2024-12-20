package com.barleyan.managementoko

import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menginflate layout fragment_settings yang berisi tampilan pengaturan
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Mengambil referensi SwitchMaterial untuk pengaturan tema gelap
        val switchTheme: SwitchMaterial = view.findViewById(R.id.switch_theme)

        // Mendapatkan SharedPreferences untuk menyimpan dan membaca pengaturan
        val sharedPreferences = requireActivity().getSharedPreferences("settings", 0)

        // Membaca status mode gelap (dark mode) dari SharedPreferences (nilai default adalah false)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)

        // Menyinkronkan status switch dengan nilai dark mode yang disimpan
        switchTheme.isChecked = isDarkMode

        // Menangani perubahan status switch tema
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            // Membuat editor untuk menyimpan perubahan ke SharedPreferences
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            if (isChecked) {
                // Mengaktifkan mode gelap jika switch diaktifkan
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("dark_mode", true)  // Menyimpan status dark mode sebagai true
            } else {
                // Menonaktifkan mode gelap jika switch dimatikan
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("dark_mode", false)  // Menyimpan status dark mode sebagai false
            }

            // Menerapkan perubahan ke SharedPreferences
            editor.apply()
        }

        // Mengembalikan tampilan fragment
        return view
    }
}