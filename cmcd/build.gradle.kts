plugins {
    kotlin("multiplatform") version Versions.kotlin
    id("kotlin-android-extensions")
    id("com.android.library")
    id("maven-publish")
}

group = Project.group
version = Project.version

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
    android {
        publishLibraryVariants("release", "debug")
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(Dependencies.Test.common)
                implementation(Dependencies.Test.annotations)
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(Dependencies.Test.js)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.AndroidX.core)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependencies.Test.android)
            }
        }
    }
}
android {
    compileSdkVersion(Versions.compile_sdk)
    defaultConfig {
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = Project.group
            artifactId = Project.artifactId

            pom {
                name.set(Project.name)
                description.set(Project.description)
                url.set(Project.url)

                licenses {
                    license {
                        name.set(Project.license)
                    }
                }

                developers {
                    developer {
                        id.set(Project.developerId)
                        name.set(Project.developerName)
                        email.set(Project.developerEmail)
                    }
                }
            }
        }
    }

    repositories {
        maven {
            val releasesRepoUrl = uri(Project.realeyesMavenRelease)
            val snapshotsRepoUrl = uri(Project.realeyesMavenSnapshot)
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}
