package com.contrastsecurity

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction


class InstallContrastAgent extends DefaultTask {

    boolean ignoreFailures

    @TaskAction
    def exec () {
        ContrastPluginExtension extension = project.contrastConfiguration

        File agentFile = new File(extension.jarPath)
        if (agentFile.exists()) {
            logger.debug("Loaded Java agent from ${extension.jarPath}")
        } else if (ignoreFailures) {
            throw new StopExecutionException("Unable to load Java agent from ${extension.jarPath}")
        } else {
            throw new GradleException("Unable to load Java agent from ${extension.jarPath}")
        }
    }
}
