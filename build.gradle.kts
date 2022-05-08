import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.userdev.UserDevExtension
import org.gradle.jvm.tasks.Jar
import java.text.SimpleDateFormat
import java.util.Date

buildscript {
    repositories {
        // These repositories are only for Gradle plugins, put any other repositories in the repository block further below
        maven("https://maven.minecraftforge.net")
        mavenCentral()
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:5.1.+") {
            isChanging = true
        }
    }
}
// Only edit below this line, the above code adds and enables the necessary things for Forge to be setup.
plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
}
apply(plugin = "net.minecraftforge.gradle")


version = "1.0"
group = "com.example" // http://maven.apache.org/guides/mini/guide-naming-conventions.html
base.archivesBaseName = "examplemod"

// Mojang ships Java 17 to end users in 1.18+, so your mod should target Java 17.
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

println("Java: ${System.getProperty("java.version")}, JVM: ${System.getProperty("java.vm.version")} (${System.getProperty("java.vendor")}), Arch: ${System.getProperty("os.arch")}")
configure<UserDevExtension> {
    mappings("official", "1.18.2")

    runs {
        create("client") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", "examplemod")

            mods {
                create("examplemod") {
                    source(sourceSets.main.get())
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", "examplemod")

            mods {
                create("examplemod") {
                    source(sourceSets.main.get())
                }
            }
        }

        create("gameTestServer") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", "examplemod")

            mods {
                create("examplemod") {
                    source(sourceSets.main.get())
                }
            }
        }

        create("data") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")

            args("--mod", "examplemod", "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))

            mods {
                create("examplemod") {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

sourceSets.main { resources.srcDir("src/generated/resources") }

repositories {
    jcenter()
}

dependencies {
    "minecraft"("net.minecraftforge:forge:1.18.2-40.1.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks {
    named<Jar>("jar") {
        manifest {
            attributes(mapOf(
                "Specification-Title" to "examplemod",
                "Specification-Vendor" to "examplemodsareus",
                "Specification-Version" to "1", // We are version 1 of ourselves
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to "examplemodsareus",
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
