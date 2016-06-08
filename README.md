# Contrast Gradle Plugin

Repository for the Contrast Gradle plugin. This plugin will download and install the Contrast Java agent during the initialize lifecycle phase. Then it will verify no new vulnerabilities were found before you call the verify goal.

## Goals

* `install`: installs a Contrast Java agent to your local project
* `verify`: checks for new vulnerabilities in your web application


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

## Example Configurations

```settings
apply plugin: 'contrastplugin'

contrastConfiguration {
    username = "contrast_admin"
    apiKey = "demo"
    serviceKey = "demo"
    apiUrl = "http://localhost:19080/Contrast/api"
    orgUuid = "632AAF07-557E-4B26-99A0-89F85D1748DB"
    appId = "12345678-1234-1234-1234-12345678912"
    serverName = "ip-192-168-1-50.ec2.internal"
    minSeverity = "Medium"
}
```