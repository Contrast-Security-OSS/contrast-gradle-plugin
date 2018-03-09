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
| appName     | False    |         | Application name                                        |
| apiUrl      | True     |         | API Url to your TeamServer instance                     |
| serverName  | False    |         | Name of server you set with -Dcontrast.server           |
| minSeverity | False    | Medium  | Minimum severity level to verify                        |
| jarPath     | False    |         | Path to contrast.jar if you already have one downloaded |

# How To Guide
* Install Gradle via Homebrew ```brew install gradle ```


* The easiest way to setup a project is to clone out sample application.  This application has been migrated from Maven to Gradle, and relies on MongoDB, so we will install that and setup it's database path.
```
git clone https://github.com/Contrast-Security-OSS/Contrast-Sample-Gradle-Application.git
brew install mongodb
sudo mkdir -p /data/db
brew services start mongodb
```

* Open up the Contrast-Sample-Gradle-Application/build.gradle file.  Scroll to the very bottom and you should find the following contrastConfiguration. All of these values can be found in TeamServer already **except** for appName and serverName.
```
contrastConfiguration {
    username = "username"
    apiKey = "apiKey"
    serviceKey = "serviceKey"
    apiUrl = "apiUrl"
    orgUuid = "orgUuid"
    appName = "editLATER"
    serverName = "editLATER"
    minSeverity = "Medium"
}
```
* Once username, apiKey, serviceKey, apiUrl, and orgUuid have been configured we can install the contrast jar file by calling the `contrastInstall` task. This will install **contrast.jar** within the projects build directory.
```
cd path/to/Contrast-Sample-Gradle-Application
gradle build contrastInstall
```

* The next step is to run the application with the java agent.
```
cd path/to/Contrast-Sample-Gradle-Application/build
java -javaagent:contrast.jar -Dcontrast.appname=mytestapp -Dcontrast.server=mytestserver -jar libs/Contrast-Sample-Gradle-Application-0.0.1-SNAPSHOT.jar
```
Now, verify that your application is running here: That the test application is running at http://localhost:8080

Next, verify that the applicaiton shows up on Teamserver.

* In the VehicleMPG projects build.gradle we will now edit the contrastConfiguration to specify the appName and serverName from Teamserver
```
contrastConfiguration {
    username = "alreadySetup"
    apiKey = "alreadySetup"
    serviceKey = "alreadySetup"
    apiUrl = "alreadySetup"
    orgUuid = "alreadySetup"
    appName = "mytestapp"
    serverName = "mytestserver"
    minSeverity = "Medium"
}
```
*  Run the verification task at any time to check for vulnerabilties.
```
gradle build contrastVerify -x test
```

## Configuration Reference
```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.contrastsecurity:ContrastGradlePlugin:1.0-SNAPSHOT"
  }
}

apply plugin: 'com.contrastsecurity.contrastplugin'
contrastConfiguration {
    username = "demo"
    apiKey = "demo"
    serviceKey = "demo"
    apiUrl = "http://localhost:19080/Contrast/api"
    orgUuid = "ASDF-LKJH-POIU-MNBV-LKJH"
    appName = "appNameFromTeamServer"
    serverName = "serverNameFromTeamServer"
    minSeverity = "Medium"
}
```

## Local Development and Testing

To build and test the Gradle plugin locally:

* `gradle build`

To build an 'uber jar'

* `gradle shadowJar`

To build and 'uber jar' and install into Maven local:

* `gradle publishToMavenLocal`
