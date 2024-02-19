plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.9.22"
}

repositories {
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
    jvm()
    js {
        browser()
    }.binaries.executable()
}
