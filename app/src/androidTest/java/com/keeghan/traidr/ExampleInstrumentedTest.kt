package com.keeghan.traidr

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.keeghan.traidr.network.TradirApi
import com.keeghan.traidr.repository.UserRepository
import com.keeghan.traidr.utils.Auth
import com.keeghan.traidr.utils.Constants
import com.keeghan.traidr.viewmodels.LoginViewModel
import com.keeghan.traidr.viewmodels.SignUpViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
//
//    private lateinit var userRepository: UserRepository
//    private lateinit var viewModel: LoginViewModel
//    private lateinit var auth: Auth
//    val context = InstrumentationRegistry.getInstrumentation().targetContext
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private val testDispatcher = StandardTestDispatcher()
//
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Before
//    fun setup() {
//        val okHttpClient = OkHttpClient.Builder()
//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//        okHttpClient.addInterceptor(logging)
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .readTimeout(10, TimeUnit.SECONDS)
//            .writeTimeout(10, TimeUnit.SECONDS)
//
//        val api = Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient.build())
//            .build()
//            .create(TradirApi::class.java)
//
//        val auth = Auth(context)
//
//        userRepository = UserRepository(api)
//        viewModel = LoginViewModel(userRepository, auth, testDispatcher)
//    }
//
//
//    @Test
//    fun signing_up_new_User_works() {
//        //  val num = (100..99999).random()
//        viewModel.logInWithEmail("Eghan250@gmail.com", "Androider55")
//        assertTrue(viewModel.isSignInSuccess.value!!)
//        //  assertFalse(viewModel.isSignInSuccess.value!!)
//    }

}