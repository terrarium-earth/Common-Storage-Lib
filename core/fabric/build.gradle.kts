dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibDataCommon)
        compileOnly(projects.commonStorageLibLookupCommon)
        compileOnly(projects.commonStorageLibResourcesCommon)
    }
    include(api(projects.commonStorageLibDataFabric)!!)
    include(api(projects.commonStorageLibLookupFabric)!!)
    include(api(projects.commonStorageLibResourcesFabric)!!)

    include(modApi(group = "teamreborn", name = "energy", version = "4.1.0")) {
        exclude(group = "net.fabricmc", module = "fabric-api")
    }
}

tasks.remapJar {
    injectAccessWidener.set(true)
}