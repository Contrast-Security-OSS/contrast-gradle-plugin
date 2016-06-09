package com.contrastsecurity;

/**
 * Created by donaldpropst on 6/7/16.
 */
class ContrastPluginExtension {
    String username
    String apiKey
    String serviceKey
    String apiUrl
    String orgUuid
    String appId
    String serverName
    String minSeverity = "Medium" //default
    String jarPath

    //TODO remove when finished, just for testing
    @Override
    String printExtensionValues(){
       println(username)
       println(apiKey)
       println(serviceKey)
       println(apiUrl)
       println(orgUuid)
       println(appId)
       println(serverName)
       println(minSeverity)
       println(jarPath)
    }

}

