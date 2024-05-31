tasks.jar {
    manifest {
        attributes["Fabric-Loom-Remap"] = true
    }
}

dependencies {
    api("com.teamresourceful:bytecodecs:1.1.0") {
        isTransitive = false
    }
}