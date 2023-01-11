<div align="center">

# Botarium
<code>~ Tiny lil library mod for cross-platform stuff! ~</code>

</div>

---

To add this library to your project, do the following:

```groovy
repositories {
  maven {
    // Location of the maven that hosts Mine and Team Resourceful's files.
    name = "Resourceful Bees Maven"
    url = "https://nexus.resourcefulbees.com/repository/maven-public/"
  }
}
```

In an Architectury project, you would implement it like so:

Common
```groovy
dependencies {
  modImplementation "earth.terrarium:botarium-common-{minecraft_version}:{botarium_version}"
}
```

Fabric
```groovy
dependencies {
  modApi "earth.terrarium:botarium-fabric-{minecraft_version}:{botarium_version}"
}
```

Forge
```groovy
dependencies {
    modApi "earth.terrarium:botarium-forge-{minecraft_version}:{botarium_version}"
}
```

<b>This project is now on Curseforge! You can find it on [Modrinth](https://modrinth.com/mod/botarium) and [Curseforge](https://www.curseforge.com/minecraft/mc-mods/botarium)</b>

---

<div align="center">

![Common](https://img.shields.io/maven-metadata/v?label=Common%20Version&metadataUrl=https%3A%2F%2Fnexus.resourcefulbees.com%2Frepository%2Fmaven-public%2Fearth%2Fterrarium%2Fbotarium-common-1.19.3%2Fmaven-metadata.xml)
&nbsp;&nbsp;&nbsp;&nbsp;
![Fabric](https://img.shields.io/maven-metadata/v?label=Fabric%20Version&metadataUrl=https%3A%2F%2Fnexus.resourcefulbees.com%2Frepository%2Fmaven-public%2Fearth%2Fterrarium%2Fbotarium-fabric-1.19.3%2Fmaven-metadata.xml)
&nbsp;&nbsp;&nbsp;&nbsp;
![Forge](https://img.shields.io/maven-metadata/v?label=Forge%20Version&metadataUrl=https%3A%2F%2Fnexus.resourcefulbees.com%2Frepository%2Fmaven-public%2Fearth%2Fterrarium%2Fbotarium-forge-1.19.3%2Fmaven-metadata.xml)

</div>

---
