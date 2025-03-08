plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
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

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.okhttp3Square)
    implementation(libs.okhttp3Loggingsquareup)
    implementation(libs.retrofit2Square)
    implementation(libs.retrofit2Gson)
    implementation(libs.androidx.roomktx)
    implementation(libs.thridPicasso)
    implementation(libs.androidx.navigationCommon)
    implementation(libs.androidx.navigationFragment)
    implementation(libs.androidx.navigationUi)
    implementation(libs.androidx.roomktx)
    implementation(libs.androidx.roomcompiler)
    implementation(libs.androidx.roomruntime)
    implementation(libs.kotlinx.coroutinesCore)
    implementation(libs.kotlinx.coroutinesAndroid)
}