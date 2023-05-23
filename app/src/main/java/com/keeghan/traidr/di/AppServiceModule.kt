package com.keeghan.traidr.di

import com.keeghan.traidr.network.TradirApi
import com.keeghan.traidr.repository.UserRepository
import com.keeghan.traidr.utils.Constants.Companion.BASE_URL
import com.keeghan.traidr.utils.Constants.Companion.RETROFIT_CONNECT_TIMEOUT
import com.keeghan.traidr.utils.Constants.Companion.RETROFIT_READ_TIMEOUT
import com.keeghan.traidr.utils.Constants.Companion.RETROFIT_WRITE_TIMEOUT
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
            .connectTimeout(RETROFIT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(RETROFIT_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(RETROFIT_WRITE_TIMEOUT, TimeUnit.SECONDS)
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

    @Provides
    @Named("defaultDispatcher")
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

}