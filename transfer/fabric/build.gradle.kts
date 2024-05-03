dependencies {
    // implementation(projects.botariumDataCommon)
    // implementation(projects.botariumLookupCommon)
    // implementation(projects.botariumResourcesCommon)
    implementation(projects.botariumDataFabric)
    implementation(projects.botariumLookupFabric)
    implementation(projects.botariumResourcesFabric)

    include(modApi(group = "teamreborn", name = "energy", version = "4.0.0")) {
        exclude(group = "net.fabricmc", module = "fabric-api")
    }
}
