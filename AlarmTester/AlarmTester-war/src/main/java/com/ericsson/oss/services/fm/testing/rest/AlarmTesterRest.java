/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.services.fm.testing.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.services.fm.testing.AlarmTesting;
import com.ericsson.oss.services.fm.testing.AlarmTestingDataResponse;

/**
 * Activates the message listeners on the provided JMS channels.
 */
@Path("/alarmTesting")
public class AlarmTesterRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmTesterRest.class);

    @EServiceRef
    AlarmTesting alarmTesting;

    @GET
    @Path("/startNormalAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startNormalAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: startNormalAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.startNormalAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/startPeakAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startPeakAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: startPeakAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.startPeakAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/startStormAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startStormAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: startStormAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.startStormAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/stopNormalAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopNormalAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: stopNormalAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.stopNormalAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/stopPeakAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopPeakAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: stopPeakAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.stopPeakAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/stopStormAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopStormAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: stopStormAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.stopStormAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeNormalAlarmThroughput")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeNormalAlarmThroughput(@QueryParam("normalAlarmThroughput") final Integer normalAlarmThroughput) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeNormalAlarmThroughput to {}", normalAlarmThroughput);
        final AlarmTestingDataResponse response = alarmTesting.changeNormalAlarmThroughput(normalAlarmThroughput);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeNormalEnableCeaseAlarmPercentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeNormalEnableCeaseAlarmPercentage(@QueryParam("normalEnableCeaseAlarmPercentage") final Boolean normalEnableCeaseAlarmPercentage) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeNormalEnableCeaseAlarmPercentage to {}", normalEnableCeaseAlarmPercentage);
        final AlarmTestingDataResponse response = alarmTesting.changeNormalEnableCeaseAlarmPercentage(normalEnableCeaseAlarmPercentage);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeNormalCeaseAlarmPercentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeNormalCeaseAlarmPercentage(@QueryParam("normalCeaseAlarmPercentage") final Integer normalCeaseAlarmPercentage) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeNormalCeaseAlarmPercentage to {}", normalCeaseAlarmPercentage);
        final AlarmTestingDataResponse response = alarmTesting.changeNormalCeaseAlarmPercentage(normalCeaseAlarmPercentage);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changePeakAlarmThroughput")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePeakAlarmThroughput(@QueryParam("peakAlarmThroughput") final Integer peakAlarmThroughput) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changePeakAlarmThroughput to {}", peakAlarmThroughput);
        final AlarmTestingDataResponse response = alarmTesting.changePeakAlarmThroughput(peakAlarmThroughput);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changePeakEnableCeaseAlarmPercentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePeakEnableCeaseAlarmPercentage(@QueryParam("peakEnableCeaseAlarmPercentage") final Boolean peakEnableCeaseAlarmPercentage) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changePeakEnableCeaseAlarmPercentage to {}", peakEnableCeaseAlarmPercentage);
        final AlarmTestingDataResponse response = alarmTesting.changePeakEnableCeaseAlarmPercentage(peakEnableCeaseAlarmPercentage);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changePeakCeaseAlarmPercentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePeakCeaseAlarmPercentage(@QueryParam("peakCeaseAlarmPercentage") final Integer peakCeaseAlarmPercentage) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changePeakCeaseAlarmPercentage to {}", peakCeaseAlarmPercentage);
        final AlarmTestingDataResponse response = alarmTesting.changePeakCeaseAlarmPercentage(peakCeaseAlarmPercentage);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changePeakOnAlarmPeriod")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePeakOnAlarmPeriod(@QueryParam("peakOnAlarmPeriod") final Integer peakOnAlarmPeriod) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changePeakOnAlarmPeriod to {}", peakOnAlarmPeriod);
        final AlarmTestingDataResponse response = alarmTesting.changePeakOnAlarmPeriod(peakOnAlarmPeriod);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changePeakOffAlarmPeriod")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePeakOffAlarmPeriod(@QueryParam("peakOffAlarmPeriod") final Integer peakOffAlarmPeriod) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changePeakOffAlarmPeriod to {}", peakOffAlarmPeriod);
        final AlarmTestingDataResponse response = alarmTesting.changePeakOffAlarmPeriod(peakOffAlarmPeriod);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeStormAlarmThroughput")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStormAlarmThroughput(@QueryParam("stormAlarmThroughput") final Integer stormAlarmThroughput) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeStormAlarmThroughput to {}", stormAlarmThroughput);
        final AlarmTestingDataResponse response = alarmTesting.changeStormAlarmThroughput(stormAlarmThroughput);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeStormEnableCeaseAlarmPercentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStormEnableCeaseAlarmPercentage(@QueryParam("stormEnableCeaseAlarmPercentage") final Boolean stormEnableCeaseAlarmPercentage) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeStormEnableCeaseAlarmPercentage to {}", stormEnableCeaseAlarmPercentage);
        final AlarmTestingDataResponse response = alarmTesting.changeStormEnableCeaseAlarmPercentage(stormEnableCeaseAlarmPercentage);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeStormCeaseAlarmPercentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStormCeaseAlarmPercentage(@QueryParam("stormCeaseAlarmPercentage") final Integer stormCeaseAlarmPercentage) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeStormCeaseAlarmPercentage to {}", stormCeaseAlarmPercentage);
        final AlarmTestingDataResponse response = alarmTesting.changeStormCeaseAlarmPercentage(stormCeaseAlarmPercentage);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeStormOnAlarmPeriod")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStormOnAlarmPeriod(@QueryParam("stormOnAlarmPeriod") final Integer stormOnAlarmPeriod) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeStormOnAlarmPeriod to {}", stormOnAlarmPeriod);
        final AlarmTestingDataResponse response = alarmTesting.changeStormOnAlarmPeriod(stormOnAlarmPeriod);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeStormOffAlarmPeriod")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStormOffAlarmPeriod(@QueryParam("stormOffAlarmPeriod") final Integer stormOffAlarmPeriod) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeStormOffAlarmPeriod to {}", stormOffAlarmPeriod);
        final AlarmTestingDataResponse response = alarmTesting.changeStormOffAlarmPeriod(stormOffAlarmPeriod);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/startRemoveAllNormalAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startRemoveAllNormalAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: startRemoveAllNormalAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.startRemoveAllNormalAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/stopRemoveAllNormalAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopRemoveAllNormalAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: stopRemoveAllNormalAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.stopRemoveAllNormalAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/startRemoveAllPeakAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startRemoveAllPeakAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: startRemoveAllPeakAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.startRemoveAllPeakAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/stopRemoveAllPeakAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopRemoveAllPeakAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: stopRemoveAllPeakAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.stopRemoveAllPeakAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/startRemoveAllStormAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startRemoveAllStormAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: startRemoveAllStormAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.startRemoveAllStormAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/stopRemoveAllStormAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopRemoveAllStormAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: stopRemoveAllStormAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.stopRemoveAllStormAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/startRemoveNormalAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startRemoveNormalFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: startRemoveNormalFlow");
        final AlarmTestingDataResponse response = alarmTesting.startRemoveNormalAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/stopRemoveNormalAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopRemoveNormalFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: stopRemoveNormalFlow");
        final AlarmTestingDataResponse response = alarmTesting.stopRemoveNormalAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeThresholdAlarmNormalPercentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeThresholdAlarmNormalPercentage(@QueryParam("thresholdAlarmNormalPercentage") final Integer thresholdAlarmNormalPercentage) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeThresholdAlarmNormalPercentage to {}", thresholdAlarmNormalPercentage);
        final AlarmTestingDataResponse response = alarmTesting.changeThresholdAlarmNormalPercentage(thresholdAlarmNormalPercentage);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeThresholdAlarmNormalNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeThresholdAlarmNormalNumber(@QueryParam("thresholdAlarmNormalNumber") final Long thresholdAlarmNormalNumber) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeThresholdAlarmNormalNumber to {}", thresholdAlarmNormalNumber);
        final AlarmTestingDataResponse response = alarmTesting.changeThresholdAlarmNormalNumber(thresholdAlarmNormalNumber);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/startRemovePeakAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startRemovePeakFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: startRemovePeakFlow");
        final AlarmTestingDataResponse response = alarmTesting.startRemovePeakAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/stopRemovePeakAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopRemovePeakFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: stopRemovePeakFlow");
        final AlarmTestingDataResponse response = alarmTesting.stopRemovePeakAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeThresholdAlarmPeakPercentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeThresholdAlarmPeakPercentage(@QueryParam("thresholdAlarmPeakPercentage") final Integer thresholdAlarmPeakPercentage) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeThresholdAlarmNormalPercentage to {})", thresholdAlarmPeakPercentage);
        final AlarmTestingDataResponse response = alarmTesting.changeThresholdAlarmPeakPercentage(thresholdAlarmPeakPercentage);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeThresholdAlarmPeakNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeThresholdAlarmPeakNumber(@QueryParam("thresholdAlarmPeakNumber") final Long thresholdAlarmPeakNumber) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeThresholdAlarmPeakNumber to {}", thresholdAlarmPeakNumber);
        final AlarmTestingDataResponse response = alarmTesting.changeThresholdAlarmPeakNumber(thresholdAlarmPeakNumber);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/startRemoveStormAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startRemoveStormFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: startRemoveStormFlow");
        final AlarmTestingDataResponse response = alarmTesting.startRemoveStormAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/stopRemoveStormAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopRemoveStormFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: stopRemoveStormFlow");
        final AlarmTestingDataResponse response = alarmTesting.stopRemoveStormAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeThresholdAlarmStormPercentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeThresholdAlarmStormPercentage(@QueryParam("thresholdAlarmStormPercentage") final Integer thresholdAlarmStormPercentage) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeThresholdAlarmNormalPercentage to {})", thresholdAlarmStormPercentage);
        final AlarmTestingDataResponse response = alarmTesting.changeThresholdAlarmStormPercentage(thresholdAlarmStormPercentage);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeThresholdAlarmStormNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeThresholdAlarmStormNumber(@QueryParam("thresholdAlarmStormNumber") final Long thresholdAlarmStormNumber) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeThresholdAlarmStormNumber to {}", thresholdAlarmStormNumber);
        final AlarmTestingDataResponse response = alarmTesting.changeThresholdAlarmStormNumber(thresholdAlarmStormNumber);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/startReadAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startReadAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: startReadAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.startReadAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/stopReadAlarmFlow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopReadAlarmFlow() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: stopReadAlarmFlow");
        final AlarmTestingDataResponse response = alarmTesting.stopReadAlarmFlow();
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeReadParallelNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeReadParallelNumber(@QueryParam("readParallelNumber") final Long readParallelNumber) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeReadParallelNumber to {}", readParallelNumber);
        final AlarmTestingDataResponse response = alarmTesting.changeReadParallelNumber(readParallelNumber);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeReadPausePeriod")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeReadPausePeriod(@QueryParam("readPausePeriod") final Long readPausePeriod) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeReadPausePeriod to {}", readPausePeriod);
        final AlarmTestingDataResponse response = alarmTesting.changeReadPausePeriod(readPausePeriod);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeParallelAlarmNormalNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeParallelAlarmNormalNumber(@QueryParam("parallelAlarmNormalNumber") final Integer parallelAlarmNormalNumber) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeParallelAlarmNormalNumber to {}", parallelAlarmNormalNumber);
        final AlarmTestingDataResponse response = alarmTesting.changeParallelAlarmNormalNumber(parallelAlarmNormalNumber);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changePackingAlarmNormalNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePackingAlarmNormalNumber(@QueryParam("packingAlarmNormalNumber") final Integer packingAlarmNormalNumber) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changePackingAlarmNormalNumber to {}", packingAlarmNormalNumber);
        final AlarmTestingDataResponse response = alarmTesting.changePackingAlarmNormalNumber(packingAlarmNormalNumber);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeParallelAlarmPeakNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeParallelAlarmPeakNumber(@QueryParam("parallelAlarmPeakNumber") final Integer parallelAlarmPeakNumber) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeParallelAlarmPeakNumber to {}", parallelAlarmPeakNumber);
        final AlarmTestingDataResponse response = alarmTesting.changeParallelAlarmPeakNumber(parallelAlarmPeakNumber);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changePackingAlarmPeakNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePackingAlarmPeakNumber(@QueryParam("packingAlarmPeakNumber") final Integer packingAlarmPeakNumber) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changePackingAlarmPeakNumber to {}", packingAlarmPeakNumber);
        final AlarmTestingDataResponse response = alarmTesting.changePackingAlarmPeakNumber(packingAlarmPeakNumber);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changeParallelAlarmStormNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeParallelAlarmStormNumber(@QueryParam("parallelAlarmStormNumber") final Integer parallelAlarmStormNumber) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changeParallelAlarmStormNumber to {}", parallelAlarmStormNumber);
        final AlarmTestingDataResponse response = alarmTesting.changeParallelAlarmStormNumber(parallelAlarmStormNumber);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/changePackingAlarmStormNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePackingAlarmStormNumber(@QueryParam("packingAlarmStormNumber") final Integer packingAlarmStormNumber) {
        LOGGER.info("********* ALARM TESTING REST REQUEST: changePackingAlarmStormNumber to {}", packingAlarmStormNumber);
        final AlarmTestingDataResponse response = alarmTesting.changePackingAlarmStormNumber(packingAlarmStormNumber);
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @GET
    @Path("/getCurrentAlarmNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentAlarmNumber() {
        LOGGER.info("********* ALARM TESTING REST REQUEST: getCurrentAlarmNumber...");
        final AlarmTestingDataResponse response = alarmTesting.getCurrentAlarmNumber();
        return Response.status(Response.Status.OK).entity(response).build();
    }

}
