plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "com.example.testemercadolivre"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.testemercadolivre"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://api.mercadolibre.com/\"")
            buildConfigField("String", "CLIENT_ID", "\"1287736882365902\"")
            buildConfigField("String", "CLIENT_SECRET", "\"IWpkhB2nCwWWYl1G8c8xzrKI0z5Z63pG\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"https://api.mercadolibre.com/\"")
            buildConfigField("String", "CLIENT_ID", "\"1287736882365902\"")
            buildConfigField("String", "CLIENT_SECRET", "\"IWpkhB2nCwWWYl1G8c8xzrKI0z5Z63pG\"")
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
    configurations.implementation{
        exclude(group = "com.intellij", module = "annotations")
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.okhttp3Square)
    implementation(libs.okhttp3Loggingsquareup)
    implementation(libs.retrofit2Square)
    implementation(libs.retrofit2Gson)
    implementation(libs.androidx.navigationCommon)
    implementation(libs.androidx.navigationFragment)
    implementation(libs.androidx.navigationUi)
    implementation(libs.kotlinx.coroutinesCore)
    implementation(libs.kotlinx.coroutinesAndroid)
    implementation(libs.hilt.android)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.paging)
    implementation(libs.glide)
    implementation(libs.jsoup)
    implementation(libs.timber)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    testImplementation(libs.junit)
    testImplementation(libs.mockk.test)
    testImplementation(libs.mockk.jvm)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockwebserver.test)
    testImplementation(libs.truth.test)
    testImplementation(libs.hilt.test)
    testImplementation(libs.core.test)
}