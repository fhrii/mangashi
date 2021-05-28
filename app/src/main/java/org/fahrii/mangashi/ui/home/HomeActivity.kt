package org.fahrii.mangashi.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.fahrii.mangashi.databinding.ActivityHomeBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setLayout() {
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}