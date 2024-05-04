dependencies {
    //compileOnly(projects.botariumDataCommon)
    //compileOnly(projects.botariumLookupCommon)
    //compileOnly(projects.botariumResourcesCommon)
    include(implementation(projects.botariumDataNeoforge)!!)
    include(implementation(projects.botariumLookupNeoforge)!!)
    include(implementation(projects.botariumResourcesNeoforge)!!)

    modRuntimeOnly("curse.maven:energized-power-782147:5301706")
}
