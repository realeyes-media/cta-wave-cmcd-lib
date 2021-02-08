plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
}
group = "tech.ctawave"
version = "0.0.2"
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
            webpackTask {
                // output.libraryTarget = "commonjs2"
                output.libraryTarget = "umd"
            }
            dceTask {
                keep("cmcdlib-cmcd.tech.ctawave.cmcd")
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
        versionName = "0.0.1"
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
                        name.set("RealEyes Media, Inc.")
                        email.set("info@realeyes.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/realeyes-media/cmcd")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}
tasks.named<org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile>("compileKotlinJs").configure {
    kotlinOptions.moduleKind = "commonjs"
}
