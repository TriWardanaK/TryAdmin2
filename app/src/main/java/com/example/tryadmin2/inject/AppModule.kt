package com.example.tryadmin2.inject

import com.example.tryadmin2.model.remote.ApiService
import com.example.tryadmin2.repository.AdminRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAdminRepository(apiService: ApiService): AdminRepository {
        return AdminRepository(apiService)
    }
}