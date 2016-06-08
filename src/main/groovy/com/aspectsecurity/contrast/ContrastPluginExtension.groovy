package com.aspectsecurity.contrast;

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
    String minSeverity
    String jarPath

    //TODO remove when finished, just for testing
    @Override
    String toString(){
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

