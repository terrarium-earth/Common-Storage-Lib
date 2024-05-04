dependencies {
    // implementation(projects.botariumDataCommon)
    // implementation(projects.botariumLookupCommon)
    // implementation(projects.botariumResourcesCommon)
    include(implementation(projects.botariumDataFabric)!!)
    include(implementation(projects.botariumLookupFabric)!!)
    include(implementation(projects.botariumResourcesFabric)!!)

    include(modApi(group = "teamreborn", name = "energy", version = "4.0.0")) {
        exclude(group = "net.fabricmc", module = "fabric-api")
    }
}
