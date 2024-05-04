architectury {
    val enabledPlatforms: String by rootProject
    common(enabledPlatforms.split(","))
}

tasks.jar {
    manifest {
        attributes["Fabric-Loom-Remap"] = true
    }
}

dependencies {
    implementation(projects.botariumDataCommon)
    implementation(projects.botariumLookupCommon)
    implementation(projects.botariumResourcesCommon)
    implementation(projects.botariumCommon)
}
