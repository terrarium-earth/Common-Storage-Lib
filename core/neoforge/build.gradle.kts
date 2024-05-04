dependencies {
    //compileOnly(projects.botariumDataCommon)
    //compileOnly(projects.botariumLookupCommon)
    //compileOnly(projects.botariumResourcesCommon)
    implementation(projects.botariumDataNeoforge)
    implementation(projects.botariumLookupNeoforge)
    implementation(projects.botariumResourcesNeoforge)

    modImplementation("curse.maven:energized-power-782147:5301706")
}
