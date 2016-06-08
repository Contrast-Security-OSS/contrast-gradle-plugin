package com.aspectsecurity.contrast

import com.contrastsecurity.exceptions.UnauthorizedException
import com.contrastsecurity.models.AgentType
import com.contrastsecurity.sdk.ContrastSDK
import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction


class InstallContrastAgent extends DefaultTask {

    private static final String AGENT_NAME = "contrast.jar";
    private ContrastPluginExtension extension;
    protected String jarPath;

    @TaskAction
    def exec () {
        extension = ContrastGradlePlugin.extension;
        installJavaAgent(this.getProject(), ContrastGradlePlugin.contrastSDK)
        ContrastGradlePlugin.verifyDateTime = new Date();
    }


    private File installJavaAgent(Project project, ContrastSDK connection) {
        byte[] javaAgent;
        File agentFile;

        if (StringUtils.isEmpty(jarPath)) {
            //getLog().info("No jar path was configured. Downloading the latest contrast.jar...");
            println "No jar path was configured.  Downloading the latest contrast.jar..."
            try {
                javaAgent = connection.getAgent(AgentType.JAVA, extension.orgUuid);
            } catch (IOException e) {
                //throw new MojoExecutionException("Unable to download the latest java agent.", e);
                println "Unable to download the latest java agent."
            } catch (UnauthorizedException e) {
                println "Unable to retrieve the latest java agent due to authorization."
                //throw new MojoExecutionException("Unable to retrieve the latest java agent due to authorization.", e);
            }

            // Save the jar to the 'build' directory
            agentFile = new File(project.getBuildDir().toString() + File.separator + AGENT_NAME);

            try {
                FileUtils.writeByteArrayToFile(agentFile, javaAgent);
            } catch (IOException e) {
                println "Unable to save the latest java agent."
                //throw new MojoExecutionException("Unable to save the latest java agent.", e);
            }

            println "Saved the latest java agent to " + agentFile.getAbsolutePath()
            //getLog().info("Saved the latest java agent to " + agentFile.getAbsolutePath());

        } else {
            //getLog().info("Using configured jar path " + jarPath);
            println "Using configured jar path " + jarPath
            agentFile = new File(jarPath);

            if (!agentFile.exists()) {
                // throw new MojoExecutionException("Unable to load the local Java agent from " + jarPath);
            }

            getLog().info("Loaded the latest java agent from " + jarPath);

        }

        return agentFile;
    }



}

