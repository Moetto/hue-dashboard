import com.google.devtools.ksp.gradle.KspTaskMetadata
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("com.google.devtools.ksp") version "1.9.22-1.0.16"
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
        useCommonJs()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }.binaries.executable()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.dev.fritz2.core)
                implementation(libs.org.jetbrains.kotlinx.serialization)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.dev.t3mu.openhue)
                implementation(npm("postcss", "8.4.35"))
                implementation(npm("postcss-loader", "8.1.0"))
                implementation(npm("autoprefixer", "10.4.7"))
                implementation(npm("tailwindcss", "3.4.1"))
            }
        }
    }
}

tasks {
    withType(KotlinWebpack::class).forEach { t ->
        t.inputs.files(fileTree("src/jsMain/resources"))
    }

    val copyTailwindConfig by registering(Copy::class) {
        from("./tailwind.config.js")
        into("${rootProject.buildDir}/js/packages/${rootProject.name}-${project.name}")

        dependsOn(":kotlinNpmInstall")
    }

    val copyPostcssConfig by registering(Copy::class) {
        from("./postcss.config.js")
        into("${rootProject.buildDir}/js/packages/${rootProject.name}-${project.name}")

        dependsOn(":kotlinNpmInstall")
    }

    val jsProcessResources by getting {
        dependsOn(copyTailwindConfig)
        dependsOn(copyPostcssConfig)
    }
}

dependencies.kspCommonMainMetadata(libs.dev.fritz2.lenses)
kotlin.sourceSets.commonMain { tasks.withType<KspTaskMetadata> { kotlin.srcDir(destinationDirectory) } }