package com.holmapps.bussenbus.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.holmapps.bussenbus.BuildConfig
import com.holmapps.bussenbus.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object BusModule {
    private val SIZE_OF_CACHE = 1024.toLong() * 1024 * 10

    @Provides @Singleton fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder().baseUrl("http://www.rejseplanen.dk")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient).build()
    }

    @Provides @Singleton fun provideHttpCache(context: Context): Cache {
        return Cache(context.getDir("httpCache", Context.MODE_PRIVATE), SIZE_OF_CACHE)
    }

    @Provides @Singleton fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.readTimeout(15, TimeUnit.SECONDS)
        builder.writeTimeout(15, TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(true)
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            builder.addInterceptor(interceptor)
        }

        return builder.build()
    }

    @Provides @Singleton
    fun providesGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(BusResponse::class.java, BusDeserializer())
        gsonBuilder.registerTypeAdapter(RouteResponse::class.java, RouteDeserializer())
        return gsonBuilder.create()
    }

    @Singleton
    @Provides
    fun provideBusApi(retrofit: Retrofit): BusApi {
        return retrofit.create(BusApi::class.java)
    }
}