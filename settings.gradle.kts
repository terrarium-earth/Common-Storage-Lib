enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "common_storage_lib"

pluginManagement {
    repositories {
        maven(url = "https://maven.architectury.dev/")
        maven(url = "https://kneelawk.com/maven")
        maven(url = "https://maven.neoforged.net/releases/")
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://maven.resourcefulbees.com/repository/maven-public/")
        maven(url = "https://maven.msrandom.net/repository/root")
        gradlePluginPortal()
    }
}

includeSubmodule("data")
includeSubmodule("lookup")
includeSubmodule("resources")
includeSubmodule("core")
includeSubmodule("test")

fun includeSubmodule(name: String) {
    includePlatformModule(name, "common")
    includePlatformModule(name, "fabric")
    includePlatformModule(name, "neoforge")
}

fun includePlatformModule(name: String, platform: String) {
    include("$name/$platform")
    project(":$name/$platform").name = "${rootProject.name}-$name-$platform"
}
