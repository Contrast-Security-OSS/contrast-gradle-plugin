/**
 * Created by donaldpropst on 6/8/16.
 */
package com.aspectsecurity.contrast

import com.contrastsecurity.exceptions.UnauthorizedException
import com.contrastsecurity.http.FilterForm
import com.contrastsecurity.http.ServerFilterForm
import com.contrastsecurity.models.Servers
import com.contrastsecurity.models.Trace
import com.contrastsecurity.models.Traces
import com.contrastsecurity.sdk.ContrastSDK
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction


class VerifyContrast extends DefaultTask {

    private ContrastPluginExtension extension;
    private ContrastSDK contrast;


    @TaskAction
    def exec () {
        extension = ContrastGradlePlugin.extension
        contrast = ContrastGradlePlugin.contrastSDK
        verifyForVulnerabilities()
    }


    public void verifyForVulnerabilities(){

        println "Successfully authenticated to TeamServer."

        ServerFilterForm serverFilterForm = new ServerFilterForm()
        serverFilterForm.setApplicationIds(Arrays.asList(extension.appId))
        serverFilterForm.setQ(extension.serverName)

        Servers servers
        long serverId

        println "Sending server request to TeamServer."

        try {
            servers = contrast.getServersWithFilter(extension.orgUuid, serverFilterForm)
        } catch (IOException e) {
            throw new GradleException("Unable to retrieve the servers.")
        } catch (UnauthorizedException e) {
            throw new GradleException("Unable to connect to TeamServer.")
        }

        if (!servers.getServers().isEmpty()) {
            serverId = servers.getServers().get(0).getServerId()
        } else {
            throw new GradleException("Server with name '" + extension.serverName + "' not found.")
        }

        FilterForm form = new FilterForm()
        form.setSeverities(getSeverityList(extension.minSeverity))
        form.setStartDate(ContrastGradlePlugin.verifyDateTime)

        println "Sending vulnerability request to TeamServer."

        Traces traces;

        try {
            traces = contrast.getTracesWithFilter(extension.orgUuid, extension.appId, "servers", Long.toString(serverId), form);
        } catch (IOException e) {
            throw new GradleException("Unable to retrieve the traces.")
        } catch (UnauthorizedException e) {
            throw new GradleException("Unable to connect to TeamServer.")
        }

        if (traces != null && traces.getCount() > 0) {
            println traces.getCount() + " new vulnerability(s) were found! Printing vulnerability report."

            for (Trace trace: traces.getTraces()) {
                println generateTraceReport(trace)
            }
            throw new GradleException("Your application is vulnerable. Please see the above report for new vulnerabilities.")
        } else {
            println "No new vulnerabilities were found!"
        }

        println "Finished verifying your application."
    }


    /**
     * Creates a basic report for a Trace object
     * @param trace Trace object
     * @return String report
     */
    private String generateTraceReport(Trace trace) {
        StringBuilder sb = new StringBuilder();
        sb.append("Trace: ");
        sb.append(trace.getTitle());
        sb.append("\nTrace Uuid: ");
        sb.append(trace.getUuid());
        sb.append("\nTrace Severity: ");
        sb.append(trace.getSeverity());
        sb.append("\nTrace Likelihood: ");
        sb.append(trace.getLikelihood());
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Returns the sublist of severities greater than or equal to the configured severity level
     *
     * @param severity include severity to filter with severity list with
     * @return list of severity strings
     */
    private static List<String> getSeverityList(String severity) {
        return SEVERITIES.subList(SEVERITIES.indexOf(severity), SEVERITIES.size());
    }

    // Severity levels
    private static final List<String> SEVERITIES = Arrays.asList("Note", "Low", "Medium", "High", "Critical");

}