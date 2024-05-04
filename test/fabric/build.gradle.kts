dependencies {
    // implementation(projects.botariumDataCommon)
    // implementation(projects.botariumLookupCommon)
    // implementation(projects.botariumResourcesCommon)
    // implementation(projects.botariumTransferCommon)
    include(implementation(projects.botariumDataFabric)!!)
    include(implementation(projects.botariumLookupFabric)!!)
    include(implementation(projects.botariumResourcesFabric)!!)
    include(implementation(projects.botariumFabric)!!)
}
