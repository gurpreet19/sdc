/*-
 * ============LICENSE_START=======================================================
 * SDC
 * ================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.openecomp.sdc.common.log.wrappers;

import org.apache.commons.lang3.StringUtils;
import org.openecomp.sdc.common.api.Constants;
import org.openecomp.sdc.common.log.elements.LoggerAudit;
import org.openecomp.sdc.common.log.elements.LoggerFactory;
import org.openecomp.sdc.common.log.enums.LogLevel;
import org.openecomp.sdc.common.log.enums.Severity;
import org.openecomp.sdc.common.util.ThreadLocalsHolder;
import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;


/**
 * Created by dd4296 on 12/19/2017.
 * <p>
 * audit log for asdc using the log library
 * this is adapted for filter classes
 */
public class LoggerSdcAudit extends LoggerSdcUtilBase {

    private static final String AUDIT_ON = "auditOn";
    private String className;
    private final LoggerAudit ecompLoggerAudit;

    public LoggerSdcAudit(Class<?> clazz) {
        this.className = clazz.getName();
        ecompLoggerAudit = LoggerFactory.getMdcLogger(LoggerAudit.class, org.slf4j.LoggerFactory.getLogger(clazz));
    }

    public void startLog(ContainerRequestContext requestContext) {
        ecompLoggerAudit.clear()
                .startTimer()
                .setPartnerName(getPartnerName(
                        requestContext.getHeaderString("user-agent"),
                        requestContext.getHeaderString("USER_ID"),
                        getUrl(requestContext)))
                .setServiceName(getServiceName(requestContext))
                .setKeyRequestId(ThreadLocalsHolder.getUuid());
        MDC.put(AUDIT_ON, "true");
    }

    public static boolean isFlowBeingTakenCare() {
        String auditOn = MDC.get(AUDIT_ON);
        return !StringUtils.isEmpty(auditOn) && "true".equals(auditOn);
    }

    //this function clears the MDC data that relevant for this class
    public void clearMyData() {
        ecompLoggerAudit.clear();
    }

    public void log(String remoteAddress,
                    ContainerRequestContext requestContext,
                    Response.StatusType statusInfo,
                    LogLevel logLevel,
                    Severity securityLevel,
                    String message) {

        try {

            String msg = ecompLoggerAudit.getAuditMessage() == null
                    ? message : ecompLoggerAudit.getAuditMessage();
            ecompLoggerAudit.stopTimer()
                    .setRemoteHost(remoteAddress)
                    .setResponseCode(convertHttpCodeToErrorCode(statusInfo.getStatusCode()))
                    .setStatusCode(Integer.toString(statusInfo.getStatusCode()))
                    .setResponseDesc(statusInfo.getReasonPhrase())
                    .setInstanceUUID(requestContext.getHeaderString(Constants.X_ECOMP_INSTANCE_ID_HEADER))
                    .setOptServiceInstanceId(requestContext.getHeaderString(Constants.X_ECOMP_SERVICE_ID_HEADER))
                    .setOptClassName(className)
                    .setOptAlertSeverity(securityLevel)
                    .setOptCustomField1(requestContext.getMethod() + ": " + getUrl(requestContext))
                    .setOptCustomField2(Integer.toString(statusInfo.getStatusCode()))
                    .log(logLevel, msg);
        } catch (Exception e) {
            log.warn("Failed to write to Audit Log. Original Message: {}", message, e);
        } finally {
            MDC.put(AUDIT_ON, "false");
        }
    }
}
