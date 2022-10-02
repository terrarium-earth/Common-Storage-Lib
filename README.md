# Botarium
Tiny lil library mod for cross platform stuff

To add this library to your project, do the following

```
repositories {
  maven {
    // location of the maven that hosts Mine and Team Resourceful's files
    name = "Resourceful Bees Maven"
    url = "https://nexus.resourcefulbees.com/repository/maven-public/"
  }
}
```

In an Architectury project, you would implement it like so:

Common
```
dependencies {
  modImplementation "earth.terrarium:botarium-common-{minecraft_version}:{botarium_version}"
}
```

Fabric
```
dependencies {
  include modImplementation "earth.terrarium:botarium-fabric-{minecraft_version}:{botarium_version}"
}
```

Forge
```
dependencies {
  include modImplementation "earth.terrarium:botarium-forge-{minecraft_version}:{botarium_version}"
}
```

If you use forge gradle, you can see how to do it here -> https://forge.gemwire.uk/wiki/Jar-in-jar

This library is not on curseforge and probably won't be, so jar in jaring is your only option!

## Versions
![Common](https://img.shields.io/maven-metadata/v?label=Common%20Version&metadataUrl=https%3A%2F%2Fnexus.resourcefulbees.com%2Frepository%2Fmaven-public%2Fearth%2Fterrarium%2Fbotarium-common-1.19.2%2Fmaven-metadata.xml) </br>
![Fabric](https://img.shields.io/maven-metadata/v?label=Fabric%20Version&metadataUrl=https%3A%2F%2Fnexus.resourcefulbees.com%2Frepository%2Fmaven-public%2Fearth%2Fterrarium%2Fbotarium-fabric-1.19.2%2Fmaven-metadata.xml) </br>
![Forge](https://img.shields.io/maven-metadata/v?label=Forge%20Version&metadataUrl=https%3A%2F%2Fnexus.resourcefulbees.com%2Frepository%2Fmaven-public%2Fearth%2Fterrarium%2Fbotarium-forge-1.19.2%2Fmaven-metadata.xml) </br>

