buildscript {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.20")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.20")
        classpath("com.android.tools.build:gradle:4.0.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.29.1-alpha")
    }
}

group = "tech.ctawave"
version = "0.0.1"

repositories {
    mavenCentral()
}
