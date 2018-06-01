package com.contrastsecurity

import org.apache.commons.configuration.PropertiesConfiguration
import org.apache.commons.configuration.PropertiesConfigurationLayout
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction

import java.text.SimpleDateFormat

class InstallContrastAgent extends DefaultTask {

    boolean ignoreFailures

    @TaskAction
    def exec() {
        ContrastGradlePlugin.appVersionQualifier = new SimpleDateFormat("yyyyMMddHHmm").format(new Date())

        addContrastArgLine()

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

    private void addContrastArgLine() {
        String gradleProperties = "gradle.properties"
        String jvmArgs = "org.gradle.jvmargs"

        File file = new File(gradleProperties)
        file.createNewFile()

        PropertiesConfiguration config = new PropertiesConfiguration()
        PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout(config)
        layout.load(new InputStreamReader(new FileInputStream(file)))

        String property = config.getProperty(jvmArgs)
        if (property == null) {
            config.setProperty(jvmArgs, ContrastGradlePlugin.buildArgLine(project))
            layout.save(new FileWriter(gradleProperties, false))
        } else {
            if (!argLineContainsContrastArgLine(property)) {
                config.setProperty(jvmArgs, property + " " + ContrastGradlePlugin.buildArgLine(project))
                layout.save(new FileWriter(gradleProperties, false))
            }
        }
    }

    private static boolean argLineContainsContrastArgLine(String argLine) {
        if (!argLine.contains("-javaagent") && !argLine.contains("-Dcontrast.override.appname")
                && !argLine.contains("-Dcontrast.server") && !argLine.contains("-Dcontrast.env")
                && !argLine.contains("-Dcontrast.override.appversion")) {
            return false
        }
        return true
    }
}
