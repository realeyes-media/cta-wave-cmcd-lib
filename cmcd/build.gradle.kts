plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
}

group = "tech.ctawave"
version = "0.0.7"

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
        binaries.executable()
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
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }
    }
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 0
        versionName = "0.0.7"
    }
    sourceSets {
        val main by getting {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "tech.ctawave"
            artifactId = "cmcd"

            pom {
                name.set("CMCD")
                description.set("Common Media Client Data SDK")
                url.set("https://github.com/realeyes-media/cmcd")

                licenses {
                    license {
                        name.set("MIT")
                    }
                }

                developers {
                    developer {
                        id.set("realeyesmedia")
                        name.set("RealEyes Media, LLC.")
                        email.set("info@realeyes.com")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("gcs://realeyes-maven/maven-releases")
        }
    }
}
