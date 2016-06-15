package com.contrastsecurity

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue


class ContrastPluginTest {

    //Ensures that the plugin is applicable
    @Test
    public void contrastPluginAddPlugin(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply "contrastplugin"
        assertTrue(project.getPlugins().hasPlugin("contrastplugin"))
    }

    //Ensures that the plugin allows the contrastInstall task to be called
    @Test
    public void contrastPluginAddsInstallTask() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply "contrastplugin"
        project.afterEvaluate {
            assertTrue(project.tasks.getByName("contrastInstall") instanceof InstallContrastAgent)
        }
    }

    //Ensures that the plugin allows the contrastVerify task to be called
    @Test
    public void contrastPluginAddsVerifyTask(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply "contrastplugin"
        project.afterEvaluate {
            assertTrue(project.tasks.getByName("contrastVerify") instanceof VerifyContrast)
        }
    }

    //Ensures that the plugin creates the contrastConfiguration extension to allow clients to configure their settings
    @Test
    public void contrastPluginEnablesContrastConfigurationExtension(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply "contrastplugin"
        assertTrue(project.getExtensions().getByName("contrastConfiguration") instanceof ContrastPluginExtension)

    }

}
