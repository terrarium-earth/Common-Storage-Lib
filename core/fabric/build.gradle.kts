dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.botariumDataCommon)
        compileOnly(projects.botariumLookupCommon)
        compileOnly(projects.botariumResourcesCommon)
    }
    include(implementation(projects.botariumDataFabric)!!)
    include(implementation(projects.botariumLookupFabric)!!)
    include(implementation(projects.botariumResourcesFabric)!!)

    include(modApi(group = "teamreborn", name = "energy", version = "4.0.0")) {
        exclude(group = "net.fabricmc", module = "fabric-api")
    }
}
