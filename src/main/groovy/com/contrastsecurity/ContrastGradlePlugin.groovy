package com.contrastsecurity

import com.contrastsecurity.sdk.ContrastSDK
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.language.base.plugins.LifecycleBasePlugin


class ContrastGradlePlugin implements Plugin<Project> {

    static ContrastSDK contrastSDK;
    static String appVersionQualifier;

    private static final String EXTENSION_NAME = 'contrastConfiguration'

    @Override
    public void apply(Project target) {
        //allows for client to define their settings in their projects build.gradle
        ContrastPluginExtension extension = target.extensions.create(EXTENSION_NAME, ContrastPluginExtension)
        extension.appName = target.rootProject.name
        extension.serverName = InetAddress.localHost.hostName

        Task contrastLogin = target.tasks.create(name: 'contrastLogin', type: ConnectToContrast)

        Task contrastInstall = target.tasks.create(name: 'contrastInstall', type: InstallContrastAgent) {
            description = 'Installs a Contrast agent in your working copy.'
        }
        
        Task contrastVerify = target.tasks.create(name: 'contrastVerify', type: VerifyContrast) {
            dependsOn = [contrastLogin]
            description = 'Checks for new application vulnerabilities.'
            group = LifecycleBasePlugin.VERIFICATION_GROUP
        }

        target.afterEvaluate {
            if (!extension.jarPath?.trim()) {
                Task contrastDownload = target.tasks.create(name: 'contrastDownload', type: DownloadContrastAgent) {
                    dependsOn = [contrastLogin]
                    description = 'Downloads the latest version of the Contrast agent.'
                }
                extension.jarPath = contrastDownload.agentFile.absolutePath
                contrastInstall.dependsOn = [contrastDownload]
            }
        }
    }
}
