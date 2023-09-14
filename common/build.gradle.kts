architectury {
    val enabledPlatforms: String by rootProject
    common(enabledPlatforms.split(","))
}

tasks.jar {
    manifest {
        attributes["Fabric-Loom-Remap"] = true
    }
}

dependencies {
    modCompileOnly(group = "tech.thatgravyboat", name = "commonats", version = "1.0")
}
