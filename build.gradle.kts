// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()  // Warning: this repository is going to shut down soon
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {

        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")

        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual app_module build.gradle files
    }
}

//plugins {
//    alias(libs.plugins.androidApplication) apply false
//    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
//}