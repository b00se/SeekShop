import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)

    alias(libs.plugins.hilt)
}

kotlin {
    sourceSets.all {
        kotlin.srcDir("build/generated/ksp/${name}/kotlin")
    }
}

android {
    namespace = "com.example.seekshop"
    compileSdk = 35

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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.1.20"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {

    coreLibraryDesugaring(libs.desugar.jdk.libs)

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
    implementation(libs.androidx.navigation)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.play.services.location)
    implementation(libs.accompanist.permissions)
//    implementation(libs.kotlinx.metadata.jvm)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.javapoet)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android.compiler)
    ksp(libs.hilt.android.compiler)

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
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.hilt.android.compiler)
    kspAndroidTest(libs.hilt.android.compiler)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}