plugins {
    kotlin("multiplatform") version "1.4.0"
    id("com.android.application")
    id("kotlin-android-extensions")
}
group = "tech.ctawave"
version = "0.1-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}
kotlin {
    js {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }
    android()
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.3.1")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "tech.ctawave.cmcd"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "0.1"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
