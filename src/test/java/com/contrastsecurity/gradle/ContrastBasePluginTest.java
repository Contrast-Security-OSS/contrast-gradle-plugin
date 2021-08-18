package com.contrastsecurity.gradle;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

/**
 * A simple unit test for the 'com.contrastsecurity.base' plugin.
 */
final class ContrastBasePluginTest {

  @Test
  public void pluginRegistersATask() {
    // Create a test project and apply the plugin
    Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply("com.contrastsecurity.base");

    // Verify the result
    assertNotNull(project.getTasks().findByName("greeting"));
  }
}
