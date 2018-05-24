package com.contrastsecurity

import com.contrastsecurity.exceptions.UnauthorizedException
import com.contrastsecurity.http.RuleSeverity
import com.contrastsecurity.http.ServerFilterForm
import com.contrastsecurity.http.TraceFilterForm
import com.contrastsecurity.models.*
import com.contrastsecurity.sdk.ContrastSDK
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

import java.text.SimpleDateFormat

class VerifyContrast extends DefaultTask {

    @TaskAction
    def exec() {
        logger.debug('Checking for new vulnerabilities')

        ContrastSDK contrast = ContrastGradlePlugin.contrastSDK
        ContrastPluginExtension extension = project.contrastConfiguration

        if (!ContrastGradlePlugin.appVersionQualifier) {
            ContrastGradlePlugin.appVersionQualifier = new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
        }

        String applicationId = getApplicationId(contrast, extension.orgUuid, extension.appName)

        long serverId = getServerId(contrast, extension.orgUuid, applicationId, extension.serverName)

        TraceFilterForm form = new TraceFilterForm();
        form.setSeverities(getSeverityList(extension.minSeverity))
        form.setAppVersionTags(Collections.singletonList(getAppVersion(extension.appName, ContrastGradlePlugin.appVersionQualifier)))
        form.setServerIds(Arrays.asList(serverId));

        logger.debug('Requesting vulnerability report from TeamServer')

        Traces traces

        try {
            traces = contrast.getTraces(extension.orgUuid, applicationId, form);
        } catch (IOException e) {
            throw new GradleException('Unable to retrieve the traces.', e)
        } catch (UnauthorizedException e) {
            throw new GradleException('Unable to connect to TeamServer.', e)
        }

        if (traces != null && traces.count > 0) {
            logger.lifecycle("${traces.count} new vulnerabilit${traces.count > 1 ? 'ies' : 'y'} were found!")
            logger.debug('Printing vulnerability report')
            for (Trace trace : traces.traces) {
                logger.lifecycle(generateTraceReport(trace))
            }

            throw new GradleException('Your application is vulnerable. Please see the above report for new vulnerabilities.')
        } else {
            logger.debug('No new vulnerabilities were found!')
        }

        logger.debug('Finished verifying your application.')
    }

    /** Retrieves the server id by server name
     *
     * @param sdk Contrast SDK object
     * @param orgUuid organization id to filter on
     * @param applicationId application id to filter on
     * @param serverName server name to filter on
     * @return Long id of the server
     * @throws GradleException
     */
    private long getServerId(ContrastSDK sdk, String orgUuid, String applicationId, String serverName) throws GradleException {
        ServerFilterForm serverFilterForm = new ServerFilterForm()
        serverFilterForm.setApplicationIds(Arrays.asList(applicationId))
        serverFilterForm.setQ(serverName)

        Servers servers

        try {
            servers = sdk.getServersWithFilter(orgUuid, serverFilterForm)
        } catch (IOException e) {
            throw new GradleException('Unable to retrieve the servers.', e)
        } catch (UnauthorizedException e) {
            throw new GradleException('Unable to connect to TeamServer.', e)
        }

        if (servers.servers.isEmpty()) {
            throw new GradleException("Server with name '${serverName}' not found.")
        }

        return servers.servers[0].serverId
    }

    /** Retrieves the application id by application name  else null
     *
     * @param sdk Contrast SDK object
     * @param orgUuid organization id to filter on
     * @param applicationName application name to filter on
     * @return String of the application
     * @throws GradleException
     */
    private String getApplicationId(ContrastSDK sdk, String orgUuid, String applicationName) throws GradleException {

        Applications applications

        try {
            applications = sdk.getApplications(orgUuid)
        } catch (IOException e) {
            throw new GradleException('Unable to retrieve the applications.', e)
        } catch (UnauthorizedException e) {
            throw new GradleException('Unable to connect to TeamServer.', e)
        }

        for (Application application : applications.applications) {
            if (applicationName.equals(application.name)) {
                return application.id
            }
        }

        throw new GradleException("Application with name '${applicationName}' not found.")
    }

    /**
     * Creates a basic report for a Trace object
     * @param trace Trace object
     * @return String report
     */
    private String generateTraceReport(Trace trace) {
        StringBuilder sb = new StringBuilder()
        sb.append("Trace: ${trace.title}${System.lineSeparator()}")
        sb.append("Trace Uuid: ${trace.uuid}${System.lineSeparator()}")
        sb.append("Trace Severity: ${trace.severity}${System.lineSeparator()}")
        sb.append("Trace Likelihood: ${trace.likelihood}${System.lineSeparator()}")
        return sb.toString()
    }

    /**
     * Returns the sublist of severities greater than or equal to the configured severity level
     *
     * @param severity include severity to filter with severity list with
     * @return list of severity strings
     */
    public static EnumSet<RuleSeverity> getSeverityList(String severity) {

        List<RuleSeverity> ruleSeverities = new ArrayList<RuleSeverity>();
        switch (severity) {
            case 'Note':
                ruleSeverities.add(RuleSeverity.NOTE);
            case 'Low':
                ruleSeverities.add(RuleSeverity.LOW);
            case 'Medium':
                ruleSeverities.add(RuleSeverity.MEDIUM);
            case 'High':
                ruleSeverities.add(RuleSeverity.HIGH);
            case 'Critical':
                ruleSeverities.add(RuleSeverity.CRITICAL);
        }

        return EnumSet.copyOf(ruleSeverities);
    }

    private String getAppVersion(String appName, String appVersionQualifier) {
        return appName + "-" + appVersionQualifier;
    }
}
