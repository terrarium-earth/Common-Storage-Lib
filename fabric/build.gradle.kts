architectury {
    platformSetupLoomIde()
}

val common: Configuration by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
}

dependencies {
    common(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }

    testImplementation(project(":common", configuration = "namedElements"))

    val minecraftVersion: String by project
    val fabricLoaderVersion: String by project
    val fabricApiVersion: String by project
    val modMenuVersion: String by project

    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)
    modApi(group = "net.fabricmc.fabric-api", name = "fabric-api", version = "$fabricApiVersion+$minecraftVersion")

    modApi(group = "com.terraformersmc", name = "modmenu", version = modMenuVersion)

    "include"(modApi(group = "teamreborn", name = "energy", version = "3.0.0")) {
        exclude(group = "net.fabricmc", module = "fabric-api")
    }

    modLocalRuntime(group = "curse.maven", name = "reborncore-237903", version = "4958437")
    modLocalRuntime(group = "curse.maven", name = "techreborn-233564", version = "4958438")
}
