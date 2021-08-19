package com.mars.newinjetpack.data.datastore.proto

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.mars.newinjetpack.UserData
import com.mars.newinjetpack.data.datastore.proto.serializers.UserDataSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val DATA_STORE_FILE_NAME = "user_data.pb"

class UserDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserDataStore {

    private val Context.userPreferencesStore: DataStore<UserData> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = UserDataSerializer()
    )
    override val userDataFLow: Flow<UserData> = context.userPreferencesStore.data

    private fun getUser() = runBlocking { context.userPreferencesStore.data.firstOrNull() }

    override suspend fun setUserData(userData: UserData) {
        context.userPreferencesStore.updateData { userData }
    }

    override suspend fun getUserData(): UserData? {
        return userDataFLow.firstOrNull()
    }

    override suspend fun setUserName(userName: String) {
        context.userPreferencesStore.updateData {
            it.toBuilder().setUsername(userName).build()
        }
    }

    override suspend fun setEmail(email: String) {
        context.userPreferencesStore.updateData {
            it.toBuilder().setEmail(email).build()
        }
    }

    override suspend fun setUserId(id: String) {
        context.userPreferencesStore.updateData {
            it.toBuilder().setId(id).build()
        }
    }
}