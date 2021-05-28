package org.fahrii.mangashi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.fahrii.mangashi.databinding.ActivityMainBinding
import org.fahrii.mangashi.ui.home.HomeActivity

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val SPLASH_TIME_OUT = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout()

        Handler(Looper.getMainLooper()).postDelayed({
            val mIntent = Intent(this, HomeActivity::class.java)
            startActivity(mIntent)
            finish()
        }, SPLASH_TIME_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun setLayout() {
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}