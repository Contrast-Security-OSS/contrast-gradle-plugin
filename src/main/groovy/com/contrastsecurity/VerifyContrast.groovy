package com.contrastsecurity

import com.contrastsecurity.exceptions.UnauthorizedException
import com.contrastsecurity.http.FilterForm
import com.contrastsecurity.http.ServerFilterForm
import com.contrastsecurity.models.Application
import com.contrastsecurity.models.Applications
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
    //

    @TaskAction
    def exec() {
        extension = ContrastGradlePlugin.extension
        contrast = ContrastGradlePlugin.contrastSDK
        verifyForVulnerabilities()
    }

    public void verifyForVulnerabilities(){
        println "Checking for new vulnerabilities..."

        String applicationId = getApplicationId(contrast, extension.appName)

        long serverId = getServerId(contrast, applicationId)

        FilterForm form = new FilterForm();
        form.setSeverities(getSeverityList(extension.minSeverity));
        form.setStartDate(ContrastGradlePlugin.verifyDateTime);

        println("Sending vulnerability request to TeamServer.");

        Traces traces;

        try {
            traces = contrast.getTracesWithFilter(extension.orgUuid, applicationId, "servers", Long.toString(serverId), form);
        } catch (IOException e) {
            throw new GradleException("Unable to retrieve the traces.", e)
        } catch (UnauthorizedException e) {
            throw new GradleException("Unable to connect to TeamServer.", e)
        }

        if (traces != null && traces.getCount() > 0) {
            println(traces.getCount() + " new vulnerability(s) were found! Printing vulnerability report.")
            for (Trace trace : traces.getTraces()) {
                println(generateTraceReport(trace))
            }

            throw new GradleException("Your application is vulnerable. Please see the above report for new vulnerabilities.")
        } else {
            println("No new vulnerabilities were found!");
        }

        println("Finished verifying your application.");
    }

    /** Retrieves the server id by server name
     *
     * @param sdk Contrast SDK object
     * @param applicationId application id to filter on
     * @return Long id of the server
     * @throws GradleException
     */
    private long getServerId(ContrastSDK sdk, String applicationId) throws GradleException {
        ServerFilterForm serverFilterForm = new ServerFilterForm();
        serverFilterForm.setApplicationIds(Arrays.asList(applicationId));
        serverFilterForm.setQ(extension.serverName);

        Servers servers;
        long serverId;

        try {
            servers = sdk.getServersWithFilter(extension.orgUuid, serverFilterForm);
        } catch (IOException e) {
            throw new GradleException("Unable to retrieve the servers.", e);
        } catch (UnauthorizedException e) {
            throw new GradleException("Unable to connect to TeamServer.", e);
        }

        if (!servers.getServers().isEmpty()) {
            serverId = servers.getServers().get(0).getServerId();
        } else {
            throw new GradleException("Server with name '" + extension.serverName + "' not found.");
        }

        return serverId;
    }

    /** Retrieves the application id by application name; else null
     *
     * @param sdk Contrast SDK object
     * @param applicationName application name to filter on
     * @return String of the application
     * @throws GradleException
     */
    private String getApplicationId(ContrastSDK sdk, String applicationName) throws GradleException {

        Applications applications;

        try {
            applications = sdk.getApplications(extension.orgUuid);
        } catch (IOException e) {
            throw new GradleException("Unable to retrieve the applications.", e);
        } catch (UnauthorizedException e) {
            throw new GradleException("Unable to connect to TeamServer.", e);
        }

        for (Application application : applications.getApplications()) {
            if (applicationName.equals(application.getName())) {
                return application.getId();
            }
        }

        throw new GradleException("Application with name '" + applicationName + "' not found.");
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
