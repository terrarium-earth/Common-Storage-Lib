dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibCoreCommon)
    }
    implementation(projects.commonStorageLibDataNeoforge)
    implementation(projects.commonStorageLibLookupNeoforge)
    implementation(projects.commonStorageLibResourcesNeoforge)
    implementation(projects.commonStorageLibCoreNeoforge)
}