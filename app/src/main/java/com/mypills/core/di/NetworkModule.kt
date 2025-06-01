package com.mypills.core.di

import android.content.Context
import com.mypills.features.medications.data.api.ProductApiService
import com.mypills.features.shopping.data.api.PriceComparisonApi
import com.mypills.features.transport.data.api.TransportApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Qualifier @Retention(AnnotationRetention.BINARY) annotation class ProductApi

@Qualifier @Retention(AnnotationRetention.BINARY) annotation class TransportApi

@Qualifier @Retention(AnnotationRetention.BINARY) annotation class PriceApi

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level =
                                    if (com.mypills.BuildConfig.DEBUG) { // ✅ FIX: Adicionar import
                                        HttpLoggingInterceptor.Level.BODY
                                    } else {
                                        HttpLoggingInterceptor.Level.NONE
                                    }
                        }
                )
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    @ProductApi
    fun provideProductRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/api/v0/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    @TransportApi
    fun provideTransportRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://api.transport.example.com/v1/") // Replace with actual API
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    @PriceApi
    fun providePriceRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://api.prices.example.com/v1/") // Replace with actual API
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideProductApiService(@ProductApi retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTransportApiService(@TransportApi retrofit: Retrofit): TransportApiService {
        return retrofit.create(TransportApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePriceApiService(@PriceApi retrofit: Retrofit): PriceComparisonApi {
        return retrofit.create(PriceComparisonApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMedicationRepository(
            medicationDao: com.mypills.core.database.dao.MedicationDao
    ): com.mypills.features.medications.domain.repository.MedicationRepository {
        return com.mypills.features.medications.data.repository.MedicationRepositoryImpl(
                medicationDao
        )
    }

    @Provides
    @Singleton
    fun provideFinancialRepository(
            financesDao: com.mypills.core.database.dao.FinancesDao
    ): com.mypills.features.finances.domain.repository.FinancialRepository {
        return com.mypills.features.finances.data.repository.FinancialRepositoryImpl(financesDao)
    }

    @Provides
    @Singleton
    fun provideTransportRepository(
            transportDao: com.mypills.core.database.dao.TransportDao
    ): com.mypills.features.transport.domain.repository.TransportRepository {
        return com.mypills.features.transport.data.repository.TransportRepositoryImpl(transportDao)
    }

    @Provides
    @Singleton
    fun provideRemindersRepository(
            remindersDao: com.mypills.core.database.dao.RemindersDao
    ): com.mypills.features.reminders.domain.repository.RemindersRepository {
        return com.mypills.features.reminders.data.repository.RemindersRepositoryImpl(remindersDao)
    }

    @Provides
    @Singleton
    fun provideShoppingRepository(
            shoppingDao: com.mypills.core.database.dao.ShoppingDao
    ): com.mypills.features.shopping.domain.repository.ShoppingRepository {
        return com.mypills.features.shopping.data.repository.ShoppingRepositoryImpl(shoppingDao)
    }

    @Provides
    @Singleton
    fun provideAssistantRepository(
            assistantDao: com.mypills.core.database.dao.AssistantDao
    ): com.mypills.features.assistant.domain.repository.AssistantRepository {
        return com.mypills.features.assistant.data.repository.AssistantRepositoryImpl(assistantDao)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAIService(
            @ApplicationContext context: Context
    ): com.mypills.features.assistant.domain.service.AIService {
        return com.mypills.features.assistant.data.ai.TensorFlowLiteAIService(context)
    }
}
