dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibCommon)
    }
    implementation(projects.commonStorageLibDataNeoforge)
    implementation(projects.commonStorageLibLookupNeoforge)
    implementation(projects.commonStorageLibResourcesNeoforge)
    implementation(projects.commonStorageLibNeoforge)

    runtimeOnly(group = "curse.maven", name = "mekanism-268560", version = "6327955")
}