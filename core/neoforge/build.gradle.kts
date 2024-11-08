dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibDataCommon)
        compileOnly(projects.commonStorageLibLookupCommon)
        compileOnly(projects.commonStorageLibResourcesCommon)
    }
    include(api(projects.commonStorageLibDataNeoforge)!!)
    include(api(projects.commonStorageLibLookupNeoforge)!!)
    include(api(projects.commonStorageLibResourcesNeoforge)!!)
}


tasks.remapJar {
    injectAccessWidener = true
    atAccessWideners.add("../common/src/main/resources/common_storage_lib.accesswidener")
}