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



## First Time Usage Clone Contrast SDK
This step installs the Contrast SDK into your local Maven repository
```
git clone https://github.com/Contrast-Security-OSS/contrast-sdk-java
cd contrastSDK
git checkout jenkins
mvn install
```

## Example Configurations
In your projects build.gradle file specify the following

**NOTE:** The jarPath and minSeverity are **not** required.
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
    username = "contrast_admin"
    apiKey = "demo"
    serviceKey = "demo"
    apiUrl = "http://localhost:19080/Contrast/api"
    orgUuid = "632AAF07-557E-4B26-99A0-89F85D1748DB"
    appName = "DonnieTestApp"
    serverName = "ip-192-168-1-50.ec2.internal"
    minSeverity = "Medium"
    jarPath = "/Users/donaldpropst/git/SamplePluginUse/build/contrast.jar"
}
```
## Install and Setup
`contrastInstall` installs the contrast.jar in your projects build folder. You can then use the jar to onboard an application.
```
gradle build contrastInstall
cd build
java -javaagent:contrast.jar -Dcontrast.server=yourServerName -Dcontrast.appname=specifyYourAppNameHere -jar yourproject.jar
```
## Verification
After an application has been onboarded the appName and serverName need to be specified in the contrastConfiguration extension of your build.gradle
`contrastVerify` will now check to make sure there aren't any new vulnerabilities.
```
gradle build contrastVerify
```


