dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.botariumCommon)
    }
    include(implementation(projects.botariumFabric)!!)
}
