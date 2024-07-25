buildscript {
    dependencies {
        classpath(libs.google.services)
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}