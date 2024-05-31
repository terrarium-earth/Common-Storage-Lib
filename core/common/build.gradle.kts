tasks.jar {
    manifest {
        attributes["Fabric-Loom-Remap"] = true
    }
}

dependencies {
    implementation(projects.commonStorageLibLookupCommon)
    implementation(projects.commonStorageLibResourcesCommon)
}
