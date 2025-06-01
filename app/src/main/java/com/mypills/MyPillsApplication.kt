package com.mypills

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Application class principal do My Pills
 * 
 * Responsabilidades:
 * - Inicializar Hilt para DI
 * - Configurar WorkManager para background tasks
 * - Inicializar Timber para logs
 * - Setup inicial da aplicação
 */
@HiltAndroidApp
class MyPillsApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        
        initializeLogging()
        initializeWorkManager()
        initializeAppComponents()
    }

    /**
     * Configura logging da aplicação
     * Debug: Logs detalhados
     * Release: Apenas erros críticos
     */
    private fun initializeLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("My Pills Application iniciado - Modo Debug")
        } else {
            // Em produção, usar apenas logs de erro críticos
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    if (priority == android.util.Log.ERROR) {
                        // Aqui você pode enviar para Crashlytics ou similar
                        // crashlytics.recordException(t ?: Exception(message))
                    }
                }
            })
        }
    }

    /**
     * Inicializa WorkManager com configuração customizada
     * Usado para lembretes e sync de dados
     */
    private fun initializeWorkManager() {
        WorkManager.initialize(this, workManagerConfiguration)
        Timber.d("WorkManager inicializado")
    }

    /**
     * Inicializa componentes da aplicação
     * Analytics local, cache, etc.
     */
    private fun initializeAppComponents() {
        // Inicializar analytics local (sem tracking)
        // Configurar cache de imagens
        // Setup de configurações default
        Timber.d("Componentes da aplicação inicializados")
    }

    /**
     * Configuração do WorkManager
     * Usa HiltWorkerFactory para injeção de dependência nos Workers
     */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(
                if (BuildConfig.DEBUG) android.util.Log.DEBUG 
                else android.util.Log.ERROR
            )
            .build()
}