loom {
    mods {
        create("testmod") {
            sourceSet(sourceSets.test.get())
        }
    }
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
    val forgeVersion: String by project

    forge(group = "net.minecraftforge", name = "forge", version = "$minecraftVersion-$forgeVersion")

    modLocalRuntime(group = "curse.maven", name = "energizedpower-782147", version = "4978046") {
        isTransitive = false
    }
}
