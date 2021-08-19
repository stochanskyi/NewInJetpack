package com.mars.newinjetpack.app.di.modules

import com.mars.newinjetpack.data.datastore.proto.UserDataStore
import com.mars.newinjetpack.data.datastore.proto.UserDataStoreImpl
import com.mars.newinjetpack.data.repository.user.UserRepository
import com.mars.newinjetpack.data.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationBindsModule {

    @Singleton
    @Binds
    fun bindUserDataStore(userDataStoreImpl: UserDataStoreImpl): UserDataStore

    @Singleton
    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}