plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

    //safe args
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.cuackapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.cuackapp"
        minSdk = 24
        targetSdk = 36
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
    viewBinding { enable = true }

}



dependencies {

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.compiler)
    implementation(libs.androidx.remote.creation.core)
    //implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    val navversion = "2.9.6"

    //Dragger - Hilt
    val daggerversion = "2.48"
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    //retrofit
    val retrofitversion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:${retrofitversion}")
    implementation("com.squareup.retrofit2:converter-gson:${retrofitversion}")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
}