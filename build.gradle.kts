import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.userdev.UserDevExtension
import org.gradle.jvm.tasks.Jar
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    id("net.minecraftforge.gradle")
}

val mcVersion: String by project
val forgeVersion: String by project
val modName: String by project
val modVersion: String by project

val modId = modName.toLowerCase()

version = modVersion
base.archivesName.set(modName)

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

configure<UserDevExtension> {
    mappings("official", mcVersion)

    runs {
        create("client") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", modId)

            mods {
                create(modId) {
                    source(sourceSets.main.get())
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", modId)

            mods {
                create(modId) {
                    source(sourceSets.main.get())
                }
            }
        }

        create("gameTestServer") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", modId)

            mods {
                create(modId) {
                    source(sourceSets.main.get())
                }
            }
        }

        create("data") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")

            args("--mod", modId, "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))

            mods {
                create(modId) {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

sourceSets.main { resources.srcDir("src/generated/resources") }

repositories {
    mavenCentral()
}

dependencies {
    "minecraft"("net.minecraftforge:forge:$mcVersion-$forgeVersion")

    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    named<Jar>("jar") {
        manifest {
            attributes(mapOf(
                "Specification-Title" to modName,
                "Specification-Vendor" to "nagisberry",
                "Specification-Version" to "1", // We are version 1 of ourselves
                "Implementation-Title" to modName,
                "Implementation-Version" to modVersion,
                "Implementation-Vendor" to "nagisberry",
                "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
            ))
        }
    }

    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        dependencies {
            include { it.moduleGroup == "org.jetbrains.kotlin" }
        }
        finalizedBy("reobfJar")
    }

    build { dependsOn("shadowJar") }
}
