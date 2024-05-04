tasks.jar {
    manifest {
        attributes["Fabric-Loom-Remap"] = true
    }
}

dependencies {
    implementation(projects.botariumDataCommon)
    implementation(projects.botariumLookupCommon)
    implementation(projects.botariumResourcesCommon)
}
