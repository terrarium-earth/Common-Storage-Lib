dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibDataCommon)
        compileOnly(projects.commonStorageLibLookupCommon)
        compileOnly(projects.commonStorageLibResourcesCommon)
    }
    include(implementation(projects.commonStorageLibDataNeoforge)!!)
    include(implementation(projects.commonStorageLibLookupNeoforge)!!)
    include(implementation(projects.commonStorageLibResourcesNeoforge)!!)

    modRuntimeOnly("curse.maven:energized-power-782147:5301706")
}
