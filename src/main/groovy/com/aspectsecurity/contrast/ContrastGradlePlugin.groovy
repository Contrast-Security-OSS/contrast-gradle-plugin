/**
 * Created by donaldpropst on 6/7/16.
 */

package com.aspectsecurity.contrast

import com.contrastsecurity.exceptions.UnauthorizedException
import com.contrastsecurity.models.AgentType
import com.contrastsecurity.sdk.ContrastSDK
import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.wrapper.Install

/**
 * Created by donaldpropst on 6/7/16.
 */
class ContrastGradlePlugin implements Plugin<Project> {

    static ContrastPluginExtension extension;
    static Date verifyDateTime;
    static ContrastSDK contrastSDK;

    private final String EXTENSION_NAME = "contrastConfiguration"

    //TODO implement in the extension
    protected String jarPath;


    public void apply(Project target) {
         //allows for client to define their settings in their projects build.gradle
         target.extensions.create(EXTENSION_NAME, ContrastPluginExtension)
         target.afterEvaluate {
             extension = target.getExtensions().getByName(EXTENSION_NAME);

             contrastSDK = connectToTeamServer()
             target.task("install", type: InstallContrastAgent){
                 println "Successfully authenticated to Teamserver. \n Attempting to install the Java agent."
             }

             target.task("verify", type: VerifyContrast) << {

             }
         }
    }

    ContrastSDK connectToTeamServer() throws GradleException{
        println "Attempting to connect to configured TeamServer..."
        try {
            if (!StringUtils.isEmpty(extension.apiUrl)) {
                return new ContrastSDK(extension.username, extension.serviceKey, extension.apiKey, extension.apiUrl);
            } else {
                return new ContrastSDK(extension.username, extension.serviceKey, extension.apiKey);
            }
        } catch (IllegalArgumentException e) {
            println "Unable to connect to TeamServer. Please check your maven settings."
        }

    }






}




