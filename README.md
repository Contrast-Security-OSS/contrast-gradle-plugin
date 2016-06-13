# Contrast Gradle Plugin

Repository for the Contrast Gradle plugin. This plugin will allow for a Contrast Java agent to be downloaded and then ensure that there are no new vulnerabilities found.

## Goals

* `contrastInstall`: installs a Contrast Java agent to your local project
* `contrastVerify`: checks for new vulnerabilities in your web application

## Configuration Options

| Parameter   | Required | Default | Description                                             |
|-------------|----------|---------|---------------------------------------------------------|
| username    | True     |         | Username in TeamServer                                  |
| serviceKey  | True     |         | Service Key found in Organization Settings              |
| apiKey      | True     |         | Api Key found in Organization Settings                  |
| orgUuid     | True     |         | Organization Uuid found in Organization Settings        |
| appId       | True     |         | Application Id of application                           |
| apiUrl      | True     |         | API Url to your TeamServer instance                     |
| serverName  | True     |         | Name of server you set with -Dcontrast.server           |
| minSeverity | False    | Medium  | Minimum severity level to verify                        |
| jarPath     | False    |         | Path to contrast.jar if you already have one downloaded |

# How To Guide
1. Install Gradle via Homebrew ```brew install gradle ```
2. Using the plugin in a project relies on 2 dependencies. The first is the Contrast Java SDK.  We will clone that from GitHub & install to our local Maven repository.
```
git clone https://github.com/Contrast-Security-OSS/contrast-sdk-java
cd contrast-sdk-java
mvn install
```
 3. The second dependency is the actual Contrast Gradle Plugin.  We will clone that from Bitbucket and publish it to the local Maven repository as well.
``` 
git clone git@bitbucket.org:contrastsecurity/contrast-gradle-plugin.git
cd contrast-gradle-plugin
gradle build install publishToMavenLocal
```

4. Now that we have all of our dependencies we can setup our project.  The easiest way to setup a project is to clone this sample application.  This application has been migrated from Maven to Gradle, and relies on MongoDB, so we will install that and setup it's database path.
```
git clone https://github.com/donniepropst/VehicleMPG
brew install mongodb
sudo mkdir -p /data/db
brew services start mongodb
```

5. Now we have an application that is ready to run.  Open up the VehicleMPG/build.gradle file.  Scroll to the very bottom and you should find the following contrastConfiguration. All of these values can be found in TeamServer already **except** for appName and serverName.  
```
contrastConfiguration {
    username = "username"
    apiKey = "apiKey"
    serviceKey = "serviceKey"
    apiUrl = "apiUrl"
    orgUuid = "orgUuid"
    appName = "editLATER"
    serverName = "editLATER"
    //minSeverity = "Optional"
    //jarPath = "Optional"
}
```
6. Once username, apiKey, serviceKey, apiUrl, and orgUuid have been configured we can install the contrast jar file by calling the `contrastInstall` task. This will install **contrast.jar** within the projects build directory.  
```
cd path/to/VehicleMPG 
gradle build -x test contrastInstall
```

7. The next step is to run the application with the java agent.  We will want to check 2 things **after** this step. 1) That the test application is running at `http://localhost:8080` & 2) that the application shows up within TeamServer.  
```
cd path/to/VehicleMPG/build
java -javaagent:contrast.jar -Dcontrast.appname=mytestapp -Dcontrast.server=mytestserver -jar libs/VehicleMPG-0.0.1-SNAPSHOT.jar
```
8. In your TeamServer verify that the application with the appname specified in the command above shows up. 
9. In the VehicleMPG projects build.gradle we will now edit the contrastConfiguration to specify the appName and serverName that we setup in the previous step.
```
contrastConfiguration {
    username = "alreadySetup"
    apiKey = "alreadySetup"
    serviceKey = "alreadySetup"
    apiUrl = "alreadySetup"
    orgUuid = "alreadySetup"
    appName = "mytestapp"
    serverName = "mytestserver"
    //minSeverity = "Optional"
    //jarPath = "Optional"
}
```
10.  We can now run the verification task at any time to check for vulnerabilties.
```
gradle build contrastVerify -x test
```
11. That's it. An application has been onboarded from start to finish and vulnerabilities can be checked at any point.

## Configuration
```
buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath("com.contrastsecurity:ContrastGradlePlugin:1.0-SNAPSHOT")
    }
}

apply plugin: 'contrastplugin'
contrastConfiguration {
    username = "demo"
    apiKey = "demo"
    serviceKey = "demo"
    apiUrl = "http://localhost:19080/Contrast/api"
    orgUuid = "ASDF-LKJH-POIU-MNBV-LKJH"
    appName = "appNameFromTeamServer"
    serverName = "serverNameFromTeamServer"
    minSeverity = "Medium"
    //jarPath = "/Users/donaldpropst/git/SamplePluginUse/build/contrast.jar"
}

```


