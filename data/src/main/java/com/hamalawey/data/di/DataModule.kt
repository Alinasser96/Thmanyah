package com.hamalawey.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hamalawey.data.dto.ContentDto
import com.hamalawey.data.network.ApiService
import com.hamalawey.data.network.ContentTypeAdapter
import com.hamalawey.data.repository.AppRepositoryImpl
import com.hamalawey.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(ContentDto::class.java, ContentTypeAdapter()) // Register the custom deserializer
            .create()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        // Set the log level. BODY logs requests, responses, headers, and bodies.
        // For production, use NONE or BASIC to avoid logging sensitive info.
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // <--- Add the logging interceptor
            .connectTimeout(30, TimeUnit.SECONDS) // Optional: Set timeouts
            .readTimeout(30, TimeUnit.SECONDS)    // Optional: Set timeouts
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-v2-b2sit6oh3a-uc.a.run.app/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule { // Hilt Binds for interfaces

    @Binds
    @Singleton
    abstract fun bindAppRepository(
        appRepositoryImpl: AppRepositoryImpl
    ): AppRepository
}