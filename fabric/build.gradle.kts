architectury {
    platformSetupLoomIde()
}

val common: Configuration by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
}

dependencies {
    /*
    common(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    testImplementation(project(":common", configuration = "namedElements"))
     */

    val minecraftVersion: String by project
    val fabricLoaderVersion: String by project
    val fabricApiVersion: String by project
    val modMenuVersion: String by project

    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modApi(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricApiVersion)

    modApi(group = "com.terraformersmc", name = "modmenu", version = modMenuVersion)
}
