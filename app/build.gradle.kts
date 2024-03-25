import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    kotlin("kapt")

    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.seekshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.seekshop"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localPropertiesFile = rootProject.file("local.properties")
        val localProperties = Properties()
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        // Add Kroger API keys to build config
        val clientId = localProperties.getProperty("KROGER_CLIENT_ID")
        val clientSecret = localProperties.getProperty("KROGER_CLIENT_SECRET")
        buildConfigField("String", "KROGER_CLIENT_ID", "\"$clientId\"")
        buildConfigField("String", "KROGER_CLIENT_SECRET", "\"$clientSecret\"")
    }

    flavorDimensions.add("environment")
    productFlavors {
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://api-ce.kroger.com/\"")
        }
        create("mock") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://mock-api.kroger.com/\"")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.startup)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.javapoet)
    kapt(libs.hilt.android.compiler)

    // Security
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)
    implementation(libs.tink.android)
    implementation(libs.security.crypto.datastore)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.mock.webserver)
    testImplementation(libs.truth)
    testImplementation(libs.retrofit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}