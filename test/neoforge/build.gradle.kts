dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.botariumCoreCommon)
    }
    implementation(projects.botariumLookupNeoforge)
    implementation(projects.botariumResourcesNeoforge)
    implementation(projects.botariumCoreNeoforge)
}