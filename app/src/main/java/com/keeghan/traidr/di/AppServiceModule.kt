package com.keeghan.traidr.di

import android.preference.PreferenceManager
import com.keeghan.traidr.network.TradirApi
import com.keeghan.traidr.repository.UserRepository
import com.keeghan.traidr.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppServiceModule {

    @Provides
    @Singleton
    fun provideOkhttp(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClient.addInterceptor(logging)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
        return okHttpClient
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient.Builder): TradirApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
            .create(TradirApi::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(api: TradirApi): UserRepository =
        UserRepository(api)


    @Provides
    @Named("mainDispatcher")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Named("ioDispatcher")
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO


}