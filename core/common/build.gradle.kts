tasks.jar {
    manifest {
        attributes["Fabric-Loom-Remap"] = true
    }
}


dependencies {
    api(projects.commonStorageLibDataCommon)
    api(projects.commonStorageLibLookupCommon)
    api(projects.commonStorageLibResourcesCommon)
}
