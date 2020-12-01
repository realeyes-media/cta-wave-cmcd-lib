buildscript {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.20")
        classpath("com.android.tools.build:gradle:4.0.2")
    }
}

group = "tech.ctawave"
version = "0.0.6"

repositories {
    mavenCentral()
}
