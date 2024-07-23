package com.coolgirl.majko.di

import android.content.Context
import com.coolgirl.majko.commons.Constantas.BASE_URI
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.ApiMajko
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun ApiClient(): ApiMajko {
    val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .build()
        chain.proceed(request)
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    return Retrofit.Builder()
        .baseUrl(BASE_URI)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiMajko::class.java)

}


object AppModule{

    fun ApiClient(): ApiMajko {
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URI)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiMajko::class.java)

    }


    fun provideUserDataStore(context: Context): UserDataStore {
        return UserDataStore(context)
    }
}