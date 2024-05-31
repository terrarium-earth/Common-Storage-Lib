dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibCoreCommon)
    }
    include(implementation(projects.commonStorageLibDataFabric)!!)
    include(implementation(projects.commonStorageLibLookupFabric)!!)
    include(implementation(projects.commonStorageLibResourcesFabric)!!)
    include(implementation(projects.commonStorageLibCoreFabric)!!)
}
