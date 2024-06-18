dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibCommon)
    }
    implementation(projects.commonStorageLibDataNeoforge)
    implementation(projects.commonStorageLibLookupNeoforge)
    implementation(projects.commonStorageLibResourcesNeoforge)
    implementation(projects.commonStorageLibNeoforge)
}