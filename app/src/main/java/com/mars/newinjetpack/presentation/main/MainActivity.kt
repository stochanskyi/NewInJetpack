package com.mars.newinjetpack.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mars.newinjetpack.databinding.ActivityMainBinding
import com.mars.newinjetpack.presentation.camera.CameraActivity
import com.mars.newinjetpack.presentation.datasaving.DataSavingActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraXButton.setOnClickListener { openCameraActivity() }
        binding.dataSavnigButton.setOnClickListener { openDataSavingActivity() }
    }

    private fun openCameraActivity() {
        startActivity(Intent(this, CameraActivity::class.java))
    }

    private fun openDataSavingActivity() {
        startActivity(Intent(this, DataSavingActivity::class.java))
    }
}