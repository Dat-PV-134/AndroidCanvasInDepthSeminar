package com.xgame.androidcanvasindepthseminar.auto_infinity_scroll

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.xgame.androidcanvasindepthseminar.R
import com.xgame.androidcanvasindepthseminar.databinding.ActivityAutoInfinityScrollAnimBinding

class AutoInfinityScrollAnimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAutoInfinityScrollAnimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutoInfinityScrollAnimBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}