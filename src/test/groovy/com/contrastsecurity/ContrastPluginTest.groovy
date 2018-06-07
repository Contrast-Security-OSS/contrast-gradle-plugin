package com.contrastsecurity

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import java.text.SimpleDateFormat

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue


class ContrastPluginTest {

    //Ensures that the plugin is applicable
    @Test
    public void contrastPluginAddPlugin(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply "com.contrastsecurity.contrastplugin"
        assertTrue(project.getPlugins().hasPlugin("com.contrastsecurity.contrastplugin"))
    }

    //Ensures that the plugin allows the contrastInstall task to be called
    @Test
    public void contrastPluginAddsInstallTask() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply "com.contrastsecurity.contrastplugin"
        project.afterEvaluate {
            assertTrue(project.tasks.getByName("contrastInstall") instanceof InstallContrastAgent)
        }
    }

    //Ensures that the plugin allows the contrastVerify task to be called
    @Test
    public void contrastPluginAddsVerifyTask(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply "com.contrastsecurity.contrastplugin"
        project.afterEvaluate {
            assertTrue(project.tasks.getByName("contrastVerify") instanceof VerifyContrast)
        }
    }

    //Ensures that the plugin creates the contrastConfiguration extension to allow clients to configure their settings
    @Test
    public void contrastPluginEnablesContrastConfigurationExtension(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply "com.contrastsecurity.contrastplugin"
        assertTrue(project.getExtensions().getByName("contrastConfiguration") instanceof ContrastPluginExtension)

    }


    @Test
    public void testBuildArgLine() {

        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply "com.contrastsecurity.contrastplugin"
        project.contrastConfiguration.setJarPath("/demo/jar/path")
        project.contrastConfiguration.setAppName("WebGoat")
        project.contrastConfiguration.setServerName("linux")

        ContrastGradlePlugin.appVersionQualifier = "1.0.0"

        String argLine = ContrastGradlePlugin.buildArgLine(project)
        String actualArgLine = "-javaagent:/demo/jar/path -Dcontrast.override.appname=WebGoat -Dcontrast.server=linux -Dcontrast.env=qa -Dcontrast.override.appversion=WebGoat-1.0.0"

        assertEquals(argLine, actualArgLine)
    }

    @Test
    public void testComputeAppVersionQualifier() {

        String travisBuildNumber = System.getenv("TRAVIS_BUILD_NUMBER")
        String circleBuildNum = System.getenv("CIRCLE_BUILD_NUM")

        String actualAppVersionQualifier = ""
        if(travisBuildNumber != null) {
            actualAppVersionQualifier = travisBuildNumber
        } else if (circleBuildNum != null) {
            actualAppVersionQualifier = circleBuildNum
        } else {
            actualAppVersionQualifier = new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
        }

        String appVersionQualifier = ContrastGradlePlugin.computeAppVersionQualifier()

        assertEquals(appVersionQualifier, actualAppVersionQualifier)
    }

}
