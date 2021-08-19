package com.mars.newinjetpack.data.repository.user

import com.mars.newinjetpack.data.repository.user.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userFlow: Flow<User>

    suspend fun setUserId(id: String)
    suspend fun setUserName(username: String)
    suspend fun setUserEmail(email: String)
}