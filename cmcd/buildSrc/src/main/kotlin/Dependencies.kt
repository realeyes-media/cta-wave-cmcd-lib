object Project {
    const val version = "0.0.1"

    const val group = "tech.ctawave.cmcd"
    const val groupId = "tech.ctawave.cmcd"
    const val artifactId = "cmcd"
    const val name = "CMCD"
    const val description = "Common Media Client Data SDK"
    const val url = "https://github.com/realeyes-media/cmcd"
    const val license = "MIT"

    const val realeyesMavenRelease = "gcs://realeyes-maven/maven-releases"
    const val realeyesMavenSnapshot = "gcs://realeyes-maven/maven-snapshots"

    const val developerId = "realeyesmedia"
    const val developerName = "RealEyes Media, LLC."
    const val developerEmail = "info@realeyes.com"
}

object Versions {
    const val min_sdk = 21
    const val target_sdk = 29
    const val compile_sdk = 29

    const val kotlin = "1.4.10"
    const val androidX = "1.3.1"
}

object Dependencies {
    object Stdlib {
        const val common = "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}"
        const val android = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        const val js = "org.jetbrains.kotlin:kotlin-stdlib-js:${Versions.kotlin}"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.androidX}"
    }

    object Test {
        const val kotlin = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
        const val common = "org.jetbrains.kotlin:kotlin-test-common:${Versions.kotlin}"
        const val annotations = "org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.kotlin}"
        const val js = "org.jetbrains.kotlin:kotlin-test-js:${Versions.kotlin}"
        const val android = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    }
}
