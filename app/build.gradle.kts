plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.multiplatform") version "1.9.22"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/Moetto/openhue-client")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

kotlin {
    js {
        browser {
        }
    }.binaries.executable()

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(libs.dev.fritz2)
                implementation(libs.dev.t3mu.openhue)
            }
        }
    }
}
