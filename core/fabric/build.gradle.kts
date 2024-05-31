dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibLookupCommon)
        compileOnly(projects.commonStorageLibResourcesCommon)
    }
    include(implementation(projects.commonStorageLibLookupFabric)!!)
    include(implementation(projects.commonStorageLibResourcesFabric)!!)

    include(modApi(group = "teamreborn", name = "energy", version = "3.0.0")) {
        exclude(group = "net.fabricmc", module = "fabric-api")
    }
}
