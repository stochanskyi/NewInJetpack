package com.mars.newinjetpack.data.datastore.proto

import com.mars.newinjetpack.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataStore {
    val userDataFLow: Flow<UserData>

    suspend fun setUserData(userData: UserData)
    suspend fun getUserData(): UserData?

    suspend fun setUserName(userName: String)
    suspend fun setEmail(email: String)
    suspend fun setUserId(id: String)
}