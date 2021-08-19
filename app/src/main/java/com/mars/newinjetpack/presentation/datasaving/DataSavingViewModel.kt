package com.mars.newinjetpack.presentation.datasaving

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mars.newinjetpack.data.repository.user.UserRepository
import com.mars.newinjetpack.data.repository.user.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataSavingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userLiveData: LiveData<User> = userRepository.userFlow.asLiveData()

    fun saveData(id: String, username: String, email: String) {
        viewModelScope.launch {
            id.takeIf { it.isNotBlank() }?.let { userRepository.setUserId(it) }
            username.takeIf { it.isNotBlank() }?.let { userRepository.setUserName(it) }
            email.takeIf { it.isNotBlank() }?.let { userRepository.setUserEmail(it) }
        }
    }

}