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
    val neoforgeVersion: String by project

    neoForge(group = "net.neoforged", name = "neoforge", version = neoforgeVersion)
}
