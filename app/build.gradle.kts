plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.farmacia_medicitas"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.farmacia_medicitas"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Base URL del backend (ajustar si usas dispositivo físico)
        // Nota: en Kotlin DSL el literal debe incluir comillas escapadas
        buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8000/\"")
        // Placeholder para controlar cleartext por buildType
        manifestPlaceholders["usesCleartextTraffic"] = true
    }

    buildTypes {
        debug {
            // Emulador/localhost
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8000/\"")
            manifestPlaceholders["usesCleartextTraffic"] = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Producción (ajusta tu dominio)
            buildConfigField("String", "BASE_URL", "\"https://tu-dominio-produccion/\"")
            manifestPlaceholders["usesCleartextTraffic"] = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        // Habilita la generación de BuildConfig para usar buildConfigField
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation("androidx.compose.foundation:foundation")
    implementation(libs.androidx.compose.material3)
    // Icons
    implementation("androidx.compose.material:material-icons-extended")
    // App Widgets (Glance)
    implementation("androidx.glance:glance-appwidget:1.1.0")
    // Navigation and Lifecycle for Compose
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
    // Coil para cargar imágenes por URL
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Retrofit + OkHttp
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)

    // Seguridad: SharedPreferences cifradas para guardar JWT
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Room (persistencia local)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Hilt (DI)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Hilt Navigation Compose
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
