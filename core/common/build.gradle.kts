tasks.jar {
    manifest {
        attributes["Fabric-Loom-Remap"] = true
    }
}

dependencies {
    implementation(projects.commonStorageLibDataCommon)
    implementation(projects.commonStorageLibLookupCommon)
    implementation(projects.commonStorageLibResourcesCommon)
}
