dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibDataCommon)
        compileOnly(projects.commonStorageLibLookupCommon)
        compileOnly(projects.commonStorageLibResourcesCommon)
    }
    include(api(projects.commonStorageLibDataNeoforge)!!)
    include(api(projects.commonStorageLibLookupNeoforge)!!)
    include(api(projects.commonStorageLibResourcesNeoforge)!!)

    modRuntimeOnly("curse.maven:energized-power-782147:5301706")
}
