dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.botariumCommon)
    }
    include(implementation(projects.botariumDataFabric)!!)
    include(implementation(projects.botariumLookupFabric)!!)
    include(implementation(projects.botariumResourcesFabric)!!)
    include(implementation(projects.botariumFabric)!!)
}
