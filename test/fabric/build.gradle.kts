dependencies {
    // implementation(projects.botariumDataCommon)
    // implementation(projects.botariumLookupCommon)
    // implementation(projects.botariumResourcesCommon)
    // implementation(projects.botariumTransferCommon)
    implementation(projects.botariumDataFabric)?.let { include(it) }
    implementation(projects.botariumLookupFabric)?.let { include(it) }
    implementation(projects.botariumResourcesFabric)?.let { include(it) }
    implementation(projects.botariumCoreFabric)?.let { include(it) }
}
