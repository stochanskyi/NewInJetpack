package com.mars.newinjetpack.presentation.datasaving

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mars.newinjetpack.databinding.ActivityDataSavingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataSavingActivity : AppCompatActivity() {

    private val viewModel: DataSavingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityDataSavingBinding.inflate(layoutInflater).run {
            setContentView(root)
            initListeners(this)
            initObservers(this)
        }
    }

    private fun initListeners(binding: ActivityDataSavingBinding) {
        binding.saveButton.setOnClickListener {
            viewModel.saveData(
                id = binding.userIdEditText.text.toString(),
                username = binding.userNameEditText.text.toString(),
                email = binding.emailEditText.text.toString()
            )
        }
    }

    private fun initObservers(binding: ActivityDataSavingBinding) {
        viewModel.userLiveData.observe(this) {
            binding.userIdEditText.setText(it.id)
            binding.userNameEditText.setText(it.userName)
            binding.emailEditText.setText(it.email)
        }
    }

}