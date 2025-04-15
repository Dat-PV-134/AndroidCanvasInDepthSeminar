package com.xgame.androidcanvasindepthseminar.chooser_game

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.xgame.androidcanvasindepthseminar.R
import com.xgame.androidcanvasindepthseminar.databinding.ActivityChooserGameBinding

class ChooserGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooserGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChooserGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}