// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.hilt) apply false
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
//    alias(libs.plugins.hilt)
}