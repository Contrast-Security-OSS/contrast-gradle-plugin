package com.contrastsecurity.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * A simple 'hello world' plugin.
 */
public class ContrastBasePlugin implements Plugin<Project> {

  public void apply(Project project) {
    // Register a task
    project.getTasks().register("greeting", task -> task.doLast(
        s -> System.out.println("Hello from plugin 'com.contrastsecurity.base'")));
  }
}
