dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.botariumLookupCommon)
        compileOnly(projects.botariumResourcesCommon)
    }
    include(implementation(projects.botariumLookupNeoforge)!!)
    include(implementation(projects.botariumResourcesNeoforge)!!)

    modRuntimeOnly("curse.maven:energized-power-782147:5301706")
}
