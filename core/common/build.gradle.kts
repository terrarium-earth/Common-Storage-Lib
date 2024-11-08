tasks.jar {
    manifest {
        attributes["Fabric-Loom-Remap"] = true
    }
}

loom {
    accessWidenerPath.set(file("src/main/resources/common_storage_lib.accesswidener"))
}

dependencies {
    api(projects.commonStorageLibDataCommon)
    api(projects.commonStorageLibLookupCommon)
    api(projects.commonStorageLibResourcesCommon)
}
