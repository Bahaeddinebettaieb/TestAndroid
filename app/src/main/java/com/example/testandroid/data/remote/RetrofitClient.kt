package com.example.testandroid.data.remote

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {
    private var retrofit: Retrofit? = null
    private var client: OkHttpClient? = null

    var gson = GsonBuilder()
        .setLenient()
        .create()

    @Singleton
    @Provides
    fun build(): APIs {
        if (retrofit == null) {
            if (client == null)
                client = OkHttpClient.Builder()
//                    .authenticator(TokenAuthenticator())
//                    .addInterceptor(HeaderInterceptor())
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .build()

            retrofit = Retrofit.Builder()
                .client(client!!)
//                .addConverterFactory(NullOnEmptyConverterFactory())
                .baseUrl(ApiEndPoints.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
        }
        return retrofit!!.create(APIs::class.java)
    }

    @Singleton
    @Provides
    fun providesRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(ApiEndPoints.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
    }

//    @Singleton
//    @Provides
//    fun providesAPI(retrofit: Retrofit.Builder): APIs {
//        return retrofit.build().create(APIs::class.java)
//    }

//    @Singleton
//    @Provides
//    fun providesUserRepository(userRepository: AuthRepositoryImpl): IAuthRepository {
//        return userRepository
//    }

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): OkHttpClient {
        val httpInterceptor = HttpLoggingInterceptor()
        httpInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(httpInterceptor)
            .build()
    }
}