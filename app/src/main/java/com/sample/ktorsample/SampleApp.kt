package com.sample.ktorsample

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * DO #2
 * Объявляет Application класс и определяет точки входа в приложение
 * */
@HiltAndroidApp
class SampleApp: Application() {
    /**
     * DO #4
     * Подключаем Timber под таргетом BuildConfig.DEBUG.
     *
     * Это необходимо чтобы использовать вместо стандартного логгера и не делать проверку типа
     * сборки в каждом модуле, таким образом BuildConfig генерируется только в одном app модуле,
     * логгер вызывается только на дебажных сборках, релизная сборка ничего не логирует и не
     * тратит перфоманс на логирование.
     *
     * Если BuildConfig не сгенерировался сразу, нужно самостоятельно произвести вызов билда
     * командой:
     * ```bash
     * ./gradlew :app:assembleDebug
     * ```
     * */
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}