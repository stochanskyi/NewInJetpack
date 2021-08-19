package com.mars.newinjetpack.data.repository.user

import com.mars.newinjetpack.UserData
import com.mars.newinjetpack.data.datastore.proto.UserDataStore
import com.mars.newinjetpack.data.repository.user.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataStore: UserDataStore
) : UserRepository {

    override val userFlow: Flow<User> = userDataStore.userDataFLow.map { it.parse() }

    override suspend fun setUserId(id: String) {
        userDataStore.setUserId(id)
    }

    override suspend fun setUserName(username: String) {
        userDataStore.setUserName(username)
    }

    override suspend fun setUserEmail(email: String) {
        userDataStore.setEmail(email)
    }

    private fun UserData.parse() = User(
        id = id,
        userName = username,
        email = email
    )
}