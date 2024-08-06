package com.coolgirl.majko.di
import com.coolgirl.majko.commons.Constantas
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.ApiMajko
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun ApiClient(userDataStore: UserDataStore): ApiMajko {

    val token = runBlocking {
        userDataStore.getAccessToken().first() ?: null
    }

    val authorizationHeader = getAuthorizationHeader(token)

    val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", authorizationHeader)
            .build()
        chain.proceed(request)
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    return Retrofit.Builder()
        .baseUrl(Constantas.BASE_URI)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiMajko::class.java)
}

private fun getAuthorizationHeader(token: String?): String {
    return "Bearer $token"
}

