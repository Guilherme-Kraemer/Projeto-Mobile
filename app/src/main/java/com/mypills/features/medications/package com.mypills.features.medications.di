package com.mypills.features.medications.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.mypills.features.medications.data.repository.MedicationRepositoryImpl
import com.mypills.features.medications.domain.repository.MedicationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MedicationModule {

    @Binds
    @Singleton
    abstract fun bindMedicationRepository(
        medicationRepositoryImpl: MedicationRepositoryImpl
    ): MedicationRepository
}
