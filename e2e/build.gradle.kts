import java.io.BufferedReader
import java.io.InputStreamReader

plugins {
    id("org.jetbrains.kotlin.multiplatform")
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

    sourceSets {
        val jvmTest by getting {
            dependencies {
                implementation(project(":app"))
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.junit)
                implementation(libs.kotlinx.serialization)
                implementation(libs.mockserver)
                implementation(libs.selenium)
                implementation(kotlin("reflect"))
            }
        }
    }
}

tasks {
    var serverProcess: Process? = null
    var serverPort: String? = null
    val runServer by registering(JavaExec::class) {
        workingDir = File("${project(":app").buildDir}/dist/js/productionExecutable")
        mainModule.set("jdk.httpserver")
        args("-p", if (project.hasProperty("server.port")) project.property("server.port") else "0")
        dependsOn(":app:assemble")
    }

    val setupTests by registering {
        doLast {
            serverProcess = ProcessBuilder()
                .directory(rootProject.rootDir)
                .command("./gradlew", "runServer")
                .start()
            val stdout = BufferedReader(InputStreamReader(serverProcess!!.inputStream))
            do {
                Regex("URL http://127.0.0.1:(\\d+)").find(stdout.readLine())?.let {
                    serverPort = it.groups[1]!!.value
                }
            } while (serverPort == null)
        }
    }

    val tearDownTests by registering {
        doLast {
            serverProcess?.destroy()
        }
    }

    withType<Test> {
        useJUnitPlatform()
        doFirst {
            serverPort?.let { systemProperty("server.port", it) } ?: throw Exception("Server port not set")
            systemProperty(
                "kotest.framework.parallelism",
                (Runtime.getRuntime().availableProcessors() - 1).coerceAtLeast(1)
            )
        }
        dependsOn(setupTests)
        finalizedBy(tearDownTests)
    }
}
