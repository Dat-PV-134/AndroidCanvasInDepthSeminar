package com.xgame.androidcanvasindepthseminar.qr_scan

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.xgame.androidcanvasindepthseminar.R
import com.xgame.androidcanvasindepthseminar.databinding.ActivityQrscanBinding

class QRScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrscanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}