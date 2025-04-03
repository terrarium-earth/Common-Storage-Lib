dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibCommon)
    }
    implementation(projects.commonStorageLibDataFabric)
    implementation(projects.commonStorageLibLookupFabric)
    implementation(projects.commonStorageLibResourcesFabric)
    implementation(projects.commonStorageLibFabric)
}
