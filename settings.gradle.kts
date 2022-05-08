pluginManagement {
    val kotlinVersion: String by settings
    val forgeGradleVersion: String by settings

    repositories {
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net")
    }

    plugins {
        id("com.github.johnrengelman.shadow") version "7.1.2"
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "net.minecraftforge.gradle" -> useModule("net.minecraftforge.gradle:ForgeGradle:$forgeGradleVersion")
            }
        }
    }
}