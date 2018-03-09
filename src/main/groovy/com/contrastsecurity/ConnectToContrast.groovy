package com.contrastsecurity

import com.contrastsecurity.sdk.ContrastSDK
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction


class ConnectToContrast extends DefaultTask {

    @TaskAction
    def exec() {
        logger.debug('Connecting to Contrast TeamServer')

        ContrastPluginExtension extension = project.contrastConfiguration

        try {
            ContrastGradlePlugin.contrastSDK = extension.apiUrl?.trim()
                ? new ContrastSDK(extension.username, extension.serviceKey, extension.apiKey, extension.apiUrl)
                : new ContrastSDK(extension.username, extension.serviceKey, extension.apiKey)
        } catch (IllegalArgumentException e) {
            throw new GradleException('Unable to connect to TeamServer. Please check your Gradle settings.')
        }

        logger.debug('Successfully connected to Contrast TeamServer')
    }

}
