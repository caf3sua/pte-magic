# Maven IDE profile
If you are using Maven, you need to activate the IDE profile in Eclipse. This is used for applying IDE-specific tweaks, which currently only includes applying the MapStruct annotation processor.

Right click on Project -> Properties -> Maven
In “Active Maven Profiles”, type dev,IDE
With this configuration, you will be using both the JHipster dev and IDE profiles.
