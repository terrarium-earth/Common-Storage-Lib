tasks.jar {
    manifest {
        attributes["Fabric-Loom-Remap"] = true
    }
}

dependencies {
    implementation(projects.botariumLookupCommon)
    implementation(projects.botariumResourcesCommon)
}
