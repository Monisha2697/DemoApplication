plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
    }
hilt {
    enableAggregatingTask = false
}
android {

    namespace = "com.example.simpledemoapplication"
    compileSdk = 35
    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.simpledemoapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
allprojects {
    configurations.all {
        resolutionStrategy {
            force("com.squareup:javapoet:1.13.0")
        }
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.fragment.ktx)

    // Hilt
    implementation(libs.hilt.android)
   kapt(libs.hilt.compiler)

// Retrofit + Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

// OkHttp (optional for logging)
    implementation(libs.okhttp.logging)

// ViewModel and LiveData
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

// Kotlin Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.javapoet)
    implementation(libs.gson)

}




