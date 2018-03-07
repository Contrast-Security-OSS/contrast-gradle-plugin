package com.contrastsecurity

import com.contrastsecurity.models.AgentType
import com.contrastsecurity.sdk.ContrastSDK
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction


class DownloadContrastAgent extends DefaultTask {

    final File agentFile = new File(project.buildDir, 'contrast.jar')

    DownloadContrastAgent() {
        // TODO Add a check to make sure the JAR is up to date???
        outputs.upToDateWhen{
            agentFile.exists()
        }
    }

    @TaskAction
    def exec () {
        logger.debug('Downloading latest Contrast agent')

        ContrastSDK contrast = ContrastGradlePlugin.contrastSDK
        ContrastPluginExtension extension = project.contrastConfiguration

        try {
            byte[] javaAgent = contrast.getAgent(AgentType.JAVA, extension.orgUuid)
            FileUtils.writeByteArrayToFile(agentFile, javaAgent)
            logger.debug("Saved Contrast agent to ${agentFile.absolutePath}")
        } catch (IOException e) {
            logger.warn("Unable to save Contrast agent to ${agentFile.absolutePath} (${e.class.name}: ${e.message})")
            throw new StopExecutionException('Unable to download the latest java agent')
        }
    }
}
