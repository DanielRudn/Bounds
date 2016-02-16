# **Bounds** #
***

### What is Bounds? ###
A 2D endless runner where you move side to side to dodge obstacles in many different environments. (Game is not finished)

### How do I get set up? ###

* #### To use with Eclipse:
  1. Make sure Eclipse has the Gradle integration plugin in order to import the plugin which can be downloaded from ```http://dist.springsource.com/release/TOOLS/gradle (latest release)``` as well as the Eclipse ADT plugin which is located at the repo  ```https://dl-ssl.google.com/android/eclipse/```
  1. Make sure the Android SDK is downloaded from ```https://developer.android.com/sdk/index.html#Other```
  1. Pull this repository
  1. Inside ```/Bounds/project/``` create a ```local.properties``` file and inside write ```sdk.dir=/path/to/android/sdk/``` with NO quotes
  1. Open Eclipse with the Bounds folder as the workspace.
  1. Import the project ```File > Import... > Gradle > Gradle Project``` then select the project folder and click Build Model.
  1. For each seperate project (android, desktop, core) you need to configure the build path and include ```dLib.jar``` in the Libraries and Order and Export tabs
***