// DO #1: подключаем зависимости
/**
 * Подключаем hilt и ksp (DI)
 * */
buildscript {
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
    }
}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}

val javaMajor = JavaVersion.valueOf(libs.versions.java.get())
    .majorVersion
    .toInt()

extra["javaMajor"] = javaMajor
