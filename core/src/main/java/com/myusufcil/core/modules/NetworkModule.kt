package com.myusufcil.core.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.myusufcil.core.constants.AppConstants
import com.myusufcil.core.networking.AppScheduler
import com.myusufcil.core.networking.Scheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.disposables.CompositeDisposable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        interceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(interceptor)
            .addInterceptor(logInterceptor)
            .connectionSpecs(mutableListOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
        return client.build()
    }

    @Provides
    @Singleton
    fun providesOkhttpCache(@ApplicationContext context: Context): Cache {
        return Cache(context.cacheDir, 20 * 1024 * 1024)
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun providesRxJavaCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun scheduler(): Scheduler {
        return AppScheduler()
    }

    @Provides
    @Singleton
    fun providesInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()

            if (chain.request().url.toUri().toString().contains("api/Soccer/GetMatchDetail")) {
                val credentials = Credentials.basic("admin!", "password!admin")
                request.addHeader("Authorization", credentials)
            }
            if (chain.request().url.host.contains("delivery.duhnet.tv")) {
                request.addHeader("", "")
            }

            if (chain.request().url.toString().contains("datasources/authors/")) {
                val response = chain.proceed(request.build())
                var rawJson = response.body?.string()
                rawJson = rawJson?.replace("\"author_articles\": {}", "\"author_articles\": []")
                response.newBuilder()
                    .body(rawJson?.let { ResponseBody.create(response.body?.contentType(), it) })
                    .build()
            } else {
                chain.proceed(request.build())
            }
        }
    }

    @Provides
    @Singleton
    @JvmStatic
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}