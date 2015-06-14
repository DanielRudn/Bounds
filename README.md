# Bounds README #
***
### How do I get set up? ###

* #### To use with Eclipse:
  1. Make sure Eclipse has the Gradle integration plugin in order to import the plugin which can be downloaded from ```http://dist.springsource.com/release/TOOLS/gradle (latest release)```
  1. Pull this repository
  1. Inside ```/Bounds/project/``` create a ```local.properties``` file and inside write ```sdk.dir=/path/to/android/sdk/``` with NO quotes
  1. Open Eclipse with the Bounds folder as the workspace.
  1. ```File > Import... > Gradle > Gradle Project``` then select the project folder and click Build Model.
  1. For each seperate project (android, desktop, core) you need to configure the build path and include ```dLib.jar``` in the Libraries and Order and Export tabs
***
###### Created by Daniel Rudnitski ######