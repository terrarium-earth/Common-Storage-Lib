import dev.architectury.plugin.ArchitectPluginExtension
import groovy.json.StringEscapeUtils
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask

plugins {
    java
    id("maven-publish")
    id("com.teamresourceful.resourcefulgradle") version "0.0.+"
    id("dev.architectury.loom") version "1.6-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT"
}

architectury {
    val minecraftVersion: String by project
    minecraft = minecraftVersion
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")

    val minecraftVersion: String by project
    val modId = rootProject.name

    val moduleType = project.layout.projectDirectory.asFile.parentFile.name
    val modLoader = project.layout.projectDirectory.asFile.name
    val isCommon = modLoader == "common"
    val commonPath = if (isCommon) project.name else ":${rootProject.name}-$moduleType-common"

    val isFabric = modLoader == "fabric"
    val fabricLoaderVersion: String by project
    val fabricApiVersion: String by project
    val modMenuVersion: String by project

    val isNeoForge = modLoader == "neoforge"
    val neoforgeVersion: String by project

    base {
        archivesName.set("${project.name}-$minecraftVersion")
    }

    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()
        runs {
            named("client") {
                name("Test Client")
                source(sourceSets.test.get())
            }
            named("server") {
                name("Test Server")
                source(sourceSets.test.get())
            }
        }
    }

    repositories {
        maven(url = "https://maven.architectury.dev/")
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://maven.resourcefulbees.com/repository/maven-public/")
        maven(url = "https://maven.neoforged.net/releases/")
        maven(url = "https://maven.msrandom.net/repository/root/")
        maven(url = "https://prmaven.neoforged.net/NeoForge/pr794") {
            content {
                includeModule("net.neoforged", "neoforge")
                includeModule("net.neoforged", "testframework")
            }
        }
        mavenLocal()
    }

    dependencies {

        "minecraft"("::$minecraftVersion")

        @Suppress("UnstableApiUsage")
        "mappings"(project.the<LoomGradleExtensionAPI>().layered {
            val parchmentVersion: String by project

            officialMojangMappings()

            parchment(create(group = "org.parchmentmc.data", name = "parchment-1.20.4", version = parchmentVersion))
        })

        if (isCommon) {
            "modCompileOnly"(group = "tech.thatgravyboat", name = "commonats", version = "2.0")
        } else {
            compileOnly(project(commonPath, configuration = "namedElements"))
        }

        if (isFabric) {
            "modImplementation"(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
            "modApi"(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricApiVersion)

            "modApi"(group = "com.terraformersmc", name = "modmenu", version = modMenuVersion)

            "include"("modApi"(group = "teamreborn", name = "energy", version = "4.0.0")) {
                exclude(group = "net.fabricmc", module = "fabric-api")
            }
        }

        if (isNeoForge) {
            "neoForge"(group = "net.neoforged", name = "neoforge", version = neoforgeVersion)
        }

        annotationProcessor(group = "net.msrandom", name = "multiplatform-processor", version = "1.0.5")
        compileOnly(group = "net.msrandom", name = "multiplatform-annotations", version = "1.0.0")
    }

    java {
        withSourcesJar()
    }

    tasks.jar {
        archiveClassifier.set("dev")
    }

    tasks.named<RemapJarTask>("remapJar") {
        archiveClassifier.set(null as String?)
    }

    tasks.processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        filesMatching(listOf("META-INF/mods.toml", "fabric.mod.json")) {
            expand("version" to project.version)
        }
    }

    if (!isCommon) {
        configure<ArchitectPluginExtension> {
            platformSetupLoomIde()
        }

        sourceSets {
            val commonSourceSets = project(commonPath).sourceSets
            val commonTest = commonSourceSets.getByName("test")
            val commonMain = commonSourceSets.getByName("main")

            getByName("test") {
                java.srcDirs(commonTest.java.srcDirs)
                resources.srcDirs(commonTest.resources.srcDirs)
            }

            getByName("main") {
                java.srcDirs(commonMain.java.srcDirs)
                resources.srcDirs(commonMain.resources.srcDirs)
            }
        }
    } else {
        tasks.compileJava {
            options.compilerArgs.add("-AgenerateExpectStubs")
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "${project.name}-$minecraftVersion"
                from(components["java"])

                pom {
                    // name.set("Botarium $modLoader")
                    url.set("https://github.com/terrarium-earth/$modId")

                    scm {
                        connection.set("git:https://github.com/terrarium-earth/$modId.git")
                        developerConnection.set("git:https://github.com/terrarium-earth/$modId.git")
                        url.set("https://github.com/terrarium-earth/$modId")
                    }

                    licenses {
                        license {
                            name.set("MIT")
                        }
                    }
                }
            }
        }
        repositories {
            maven {
                setUrl("https://maven.resourcefulbees.com/repository/terrarium/")
                credentials {
                    username = System.getenv("MAVEN_USER")
                    password = System.getenv("MAVEN_PASS")
                }
            }
        }
    }
}

resourcefulGradle {
    templates {
        register("embed") {
            val minecraftVersion: String by project
            val version: String by project
            val changelog: String = file("changelog.md").readText(Charsets.UTF_8)
            val fabricLink: String? = System.getenv("FABRIC_RELEASE_URL")
            val forgeLink: String? = System.getenv("FORGE_RELEASE_URL")

            source.set(file("templates/embed.json.template"))
            injectedValues.set(mapOf(
                "minecraft" to minecraftVersion,
                "version" to version,
                "changelog" to StringEscapeUtils.escapeJava(changelog),
                "fabric_link" to fabricLink,
                "forge_link" to forgeLink,
            ))
        }
    }
}