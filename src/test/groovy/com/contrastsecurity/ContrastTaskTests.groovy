package com.contrastsecurity

import org.apache.maven.execution.BuildFailure
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.gradle.testkit.runner.TaskOutcome.*;

class ContrastTaskTests {

    @Rule public final TemporaryFolder testProjectDir = new TemporaryFolder()
    private File buildFile

    @Before
    public void setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle")
    }

    //No username supplied
    @Test(expected = UnexpectedBuildFailure)
    public void testInstallTaskInvalidConfigurationNoUsername() {

        String buildFileContent = (getBuildScriptDependencies() +
                "apply plugin: 'com.contrastsecurity.contrastplugin' \n " +
                "contrastConfiguration { \n" +
                "username = ''\n" +
                "apiKey = 'demo' \n " +
                "serviceKey = 'demo' \n " +
                "apiUrl = 'http://localhost:19080/Contrast/api' \n " +
                "orgUuid = '632AAF07-557E-4B26-99A0-89F85D1748DB' \n " +
                "appName = '54afdf56-8b5f-4cf3-a9a9-cbfe2c134927' \n " +
                "serverName = 'ip-192-168-1-50.ec2.internal' \n " +
                "minSeverity = 'Medium' \n " +
                "}");

        writeFile(buildFile, buildFileContent)
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("contrastInstall")
                .build();
        assertEquals(result.task(":contrastInstall").getOutcome(), FAILED);
    }

    //No api key
    @Test(expected = UnexpectedBuildFailure)
    public void testInstallTaskInvalidConfigurationNoApiKey() {

        String buildFileContent = (getBuildScriptDependencies() +
                "apply plugin: 'com.contrastsecurity.contrastplugin' \n " +
                "contrastConfiguration { \n" +
                "username = 'contrast_admin'\n" +
                "apiKey = '' \n " +
                "serviceKey = 'demo' \n " +
                "apiUrl = 'http://localhost:19080/Contrast/api' \n " +
                "orgUuid = '632AAF07-557E-4B26-99A0-89F85D1748DB' \n " +
                "appName = '54afdf56-8b5f-4cf3-a9a9-cbfe2c134927' \n " +
                "serverName = 'ip-192-168-1-50.ec2.internal' \n " +
                "minSeverity = 'Medium' \n " +
                "}");

        writeFile(buildFile, buildFileContent)
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("contrastInstall")
                .build();
        assertEquals(result.task(":contrastInstall").getOutcome(), FAILED);
    }

    //No service key
    @Test(expected = UnexpectedBuildFailure)
    public void testInstallTaskInvalidConfigurationNoServiceKey() {

        String buildFileContent = (getBuildScriptDependencies() +
                "apply plugin: 'com.contrastsecurity.contrastplugin' \n " +
                "contrastConfiguration { \n" +
                "username = 'contrast_admin'\n" +
                "apiKey = 'demo' \n " +
                "serviceKey = '' \n " +
                "apiUrl = 'http://localhost:19080/Contrast/api' \n " +
                "orgUuid = '632AAF07-557E-4B26-99A0-89F85D1748DB' \n " +
                "appName = '54afdf56-8b5f-4cf3-a9a9-cbfe2c134927' \n " +
                "serverName = 'ip-192-168-1-50.ec2.internal' \n " +
                "minSeverity = 'Medium' \n " +
                "}");

        writeFile(buildFile, buildFileContent)
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("contrastInstall")
                .build();
        assertEquals(result.task(":contrastInstall").getOutcome(), FAILED);
    }

    //No api URL
    @Test(expected = UnexpectedBuildFailure)
    public void testInstallTaskInvalidConfigurationNoApiUrl() {

        String buildFileContent = (getBuildScriptDependencies() +
                "apply plugin: 'com.contrastsecurity.contrastplugin' \n " +
                "contrastConfiguration { \n" +
                "username = 'contrast_admin'\n" +
                "apiKey = 'demo' \n " +
                "serviceKey = 'demo' \n " +
                "apiUrl = '' \n " +
                "orgUuid = '632AAF07-557E-4B26-99A0-89F85D1748DB' \n " +
                "appName = '54afdf56-8b5f-4cf3-a9a9-cbfe2c134927' \n " +
                "serverName = 'ip-192-168-1-50.ec2.internal' \n " +
                "minSeverity = 'Medium' \n " +
                "}");

        writeFile(buildFile, buildFileContent)
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("contrastInstall")
                .build();
        assertEquals(result.task(":contrastInstall").getOutcome(), FAILED);
    }

    //No orgUuid
    @Test(expected = UnexpectedBuildFailure)
    public void testInstallTaskInvalidConfigurationNoOrgUuid() {

        String buildFileContent = (getBuildScriptDependencies() +
                "apply plugin: 'com.contrastsecurity.contrastplugin' \n " +
                "contrastConfiguration { \n" +
                "username = 'contrast_admin'\n" +
                "apiKey = '' \n " +
                "serviceKey = 'demo' \n " +
                "apiUrl = 'http://localhost:19080/Contrast/api' \n " +
                "orgUuid = '' \n " +
                "appName = '54afdf56-8b5f-4cf3-a9a9-cbfe2c134927' \n " +
                "serverName = 'ip-192-168-1-50.ec2.internal' \n " +
                "minSeverity = 'Medium' \n " +
                "}");

        writeFile(buildFile, buildFileContent)
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("contrastInstall")
                .build();
        assertEquals(result.task(":contrastInstall").getOutcome(), FAILED);
    }

    //No AppId
    @Test(expected = UnexpectedBuildFailure)
    public void testInstallTaskInvalidConfigurationNoAppId() {

        String buildFileContent = (getBuildScriptDependencies() +
                "apply plugin: 'com.contrastsecurity.contrastplugin' \n " +
                "contrastConfiguration { \n" +
                "username = 'contrast_admin'\n" +
                "apiKey = '' \n " +
                "serviceKey = 'demo' \n " +
                "apiUrl = 'http://localhost:19080/Contrast/api' \n " +
                "orgUuid = '632AAF07-557E-4B26-99A0-89F85D1748DB' \n " +
                "appName = '' \n " +
                "serverName = 'ip-192-168-1-50.ec2.internal' \n " +
                "minSeverity = 'Medium' \n " +
                "}");

        writeFile(buildFile, buildFileContent)
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("contrastInstall")
                .build();
        assertEquals(result.task(":contrastInstall").getOutcome(), FAILED);
    }

    //No Server name
    @Test(expected = UnexpectedBuildFailure)
    public void testInstallTaskInvalidConfigurationNoServerName() {

        String buildFileContent = (getBuildScriptDependencies() +
                "apply plugin: 'com.contrastsecurity.contrastplugin' \n " +
                "contrastConfiguration { \n" +
                "username = 'contrast_admin'\n" +
                "apiKey = '' \n " +
                "serviceKey = 'demo' \n " +
                "apiUrl = 'http://localhost:19080/Contrast/api' \n " +
                "orgUuid = '632AAF07-557E-4B26-99A0-89F85D1748DB' \n " +
                "appName = '54afdf56-8b5f-4cf3-a9a9-cbfe2c134927' \n " +
                "serverName = '' \n " +
                "minSeverity = 'Medium' \n " +
                "}");

        writeFile(buildFile, buildFileContent)
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("contrastInstall")
                .build();
        assertEquals(result.task(":contrastInstall").getOutcome(), FAILED);
    }



    //Testing helpers for writing the build.gradle file
    private void writeFile(File destination, String content) throws IOException {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(destination));
            output.write(content);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    private String getBuildScriptDependencies(){
        return ("buildscript { \n" +
                " repositories { \n" +
                "mavenLocal() \n" +
                " } \n" +
                " dependencies { \n" +
                " classpath('com.contrastsecurity:ContrastGradlePlugin:1.0-SNAPSHOT') \n" +
                " }\n " +
            " } \n");
    }

}
