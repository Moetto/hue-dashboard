import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

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
        val jsMain by getting {
            dependencies {
                implementation(libs.dev.fritz2)
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
