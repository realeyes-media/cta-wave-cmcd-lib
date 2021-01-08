plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("dagger.hilt.android.plugin")
}

group = "tech.ctawave"
version = "0.0.1"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "tech.ctawave.exoApp"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kapt {
        generateStubs = true
    }

}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.1")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.lifecycle:lifecycle-runtime:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    // cmcd library
    implementation(project(":cmcd"))
    // exoplayer
    implementation("com.google.android.exoplayer:exoplayer-core:2.12.0")
    implementation("com.google.android.exoplayer:exoplayer-hls:2.12.0")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.12.0")
    // json serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.7.0")
    // retrofit - api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // dagger-hilt - dependency injection
    implementation("com.google.dagger:hilt-android:2.29.1-alpha")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha02")
    kapt("com.google.dagger:hilt-android-compiler:2.29.1-alpha")
    kapt("androidx.hilt:hilt-compiler:1.0.0-alpha02")
    // room - database
    kapt("androidx.room:room-compiler:2.2.5")
    implementation("androidx.room:room-runtime:2.2.5")
    implementation("androidx.room:room-ktx:2.2.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    // redux - state management
    implementation("org.reduxkotlin:redux-kotlin-threadsafe-jvm:0.5.5")
}

kapt {
    correctErrorTypes = true
}

