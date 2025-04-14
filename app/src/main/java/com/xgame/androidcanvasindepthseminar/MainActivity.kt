package com.xgame.androidcanvasindepthseminar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xgame.androidcanvasindepthseminar.auto_infinity_scroll.AutoInfinityScrollAnimActivity
import com.xgame.androidcanvasindepthseminar.databinding.ActivityMainBinding
import com.xgame.androidcanvasindepthseminar.qr_scan.QRScanActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnQRScanView.setOnClickListener {
            startActivity(Intent(this@MainActivity, QRScanActivity::class.java))
        }

        binding.btnAutoScrollInfinity.setOnClickListener {
            startActivity(Intent(this@MainActivity, AutoInfinityScrollAnimActivity::class.java))
        }

        binding.btnChooserGame.setOnClickListener {

        }
    }
}