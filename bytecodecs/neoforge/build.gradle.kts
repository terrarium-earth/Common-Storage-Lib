dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.botariumBytecodecsCommon)
    }
    implementation(projects.botariumDataNeoforge)
    implementation(projects.botariumLookupNeoforge)
    implementation(projects.botariumResourcesNeoforge)
    implementation(projects.botariumNeoforge)
}