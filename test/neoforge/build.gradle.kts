dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.botariumCommon)
    }
    implementation(projects.botariumNeoforge)
}