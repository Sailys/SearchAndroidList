package com.searchandroidlist.apicalling

import com.searchandroidlist.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    fun retrofitApiService(retrofit: Retrofit = getRetrofit()): RetrofitApi {
        return retrofit.create(RetrofitApi::class.java)
    }

    private fun getRetrofit(okHttpClient: OkHttpClient = getOkHttpClient()): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

//    private fun getOkHttpNetworkInterceptor(): Interceptor {
//        return object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response {
//                val newRequest =
//                    chain.request().newBuilder().addHeader(HEADER_API_KEY, API_KEY).build()
//                return chain.proceed(newRequest)
//            }
//        }
//    }

    private fun getHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun getOkHttpClient(
        okHttpLogger: HttpLoggingInterceptor = getHttpLogger(),
       // okHttpNetworkInterceptor: Interceptor = getOkHttpNetworkInterceptor()
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(okHttpLogger)
            .connectTimeout(2, TimeUnit.MINUTES) //Backend is really slow
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            //.addInterceptor(okHttpNetworkInterceptor)
            .build()
    }

}