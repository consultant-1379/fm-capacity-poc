/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson AB. The programs may be used and/or copied only with written
 * permission from Ericsson AB. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.services.fm.testing;

import com.ericsson.oss.itpf.sdk.core.annotation.EService;

@EService
public interface AlarmTesting {

    AlarmTestingDataResponse startNormalAlarmFlow();

    AlarmTestingDataResponse startPeakAlarmFlow();

    AlarmTestingDataResponse startStormAlarmFlow();

    AlarmTestingDataResponse stopNormalAlarmFlow();

    AlarmTestingDataResponse stopPeakAlarmFlow();

    AlarmTestingDataResponse stopStormAlarmFlow();

    AlarmTestingDataResponse startRemoveAllNormalAlarmFlow();

    AlarmTestingDataResponse stopRemoveAllNormalAlarmFlow();

    AlarmTestingDataResponse startRemoveAllPeakAlarmFlow();

    AlarmTestingDataResponse stopRemoveAllPeakAlarmFlow();

    AlarmTestingDataResponse startRemoveAllStormAlarmFlow();

    AlarmTestingDataResponse stopRemoveAllStormAlarmFlow();

    AlarmTestingDataResponse changeParallelAlarmNormalNumber(Integer parallelAlarmNormalNumber);

    AlarmTestingDataResponse changePackingAlarmNormalNumber(Integer packingAlarmNormalNumber);

    AlarmTestingDataResponse changeNormalAlarmThroughput(final Integer normalAlarmThroughput);

    AlarmTestingDataResponse changeNormalEnableCeaseAlarmPercentage(final Boolean normalEnableCeaseAlarmPercentage);

    AlarmTestingDataResponse changeNormalCeaseAlarmPercentage(final Integer normalCeaseAlarmPercentage);

    AlarmTestingDataResponse changeParallelAlarmPeakNumber(Integer parallelAlarmPeakNumber);

    AlarmTestingDataResponse changePackingAlarmPeakNumber(Integer packingAlarmPeakNumber);

    AlarmTestingDataResponse changePeakAlarmThroughput(final Integer peakAlarmThroughput);

    AlarmTestingDataResponse changePeakEnableCeaseAlarmPercentage(final Boolean peakEnableCeaseAlarmPercentage);

    AlarmTestingDataResponse changePeakCeaseAlarmPercentage(final Integer peakCeaseAlarmPercentage);

    AlarmTestingDataResponse changeParallelAlarmStormNumber(Integer parallelAlarmStormNumber);

    AlarmTestingDataResponse changePackingAlarmStormNumber(Integer packingAlarmStormNumber);

    AlarmTestingDataResponse changeStormAlarmThroughput(final Integer stormAlarmThroughput);

    AlarmTestingDataResponse changeStormEnableCeaseAlarmPercentage(final Boolean stormEnableCeaseAlarmPercentage);

    AlarmTestingDataResponse changeStormCeaseAlarmPercentage(final Integer stormCeaseAlarmPercentage);

    AlarmTestingDataResponse startRemoveNormalAlarmFlow();

    AlarmTestingDataResponse stopRemoveNormalAlarmFlow();

    AlarmTestingDataResponse changeThresholdAlarmNormalPercentage(final Integer thresholdAlarmNormalPercentage);

    AlarmTestingDataResponse changeThresholdAlarmNormalNumber(final Long thresholdAlarmNormalNumber);

    AlarmTestingDataResponse startRemovePeakAlarmFlow();

    AlarmTestingDataResponse stopRemovePeakAlarmFlow();

    AlarmTestingDataResponse changeThresholdAlarmPeakPercentage(final Integer thresholdAlarmPeakPercentage);

    AlarmTestingDataResponse changeThresholdAlarmPeakNumber(final Long thresholdAlarmPeakNumber);

    AlarmTestingDataResponse changePeakOnAlarmPeriod(final Integer peakOnAlarmPeriod);

    AlarmTestingDataResponse changePeakOffAlarmPeriod(final Integer peakOffAlarmPeriod);

    AlarmTestingDataResponse startRemoveStormAlarmFlow();

    AlarmTestingDataResponse stopRemoveStormAlarmFlow();

    AlarmTestingDataResponse changeThresholdAlarmStormPercentage(final Integer thresholdAlarmStormPercentage);

    AlarmTestingDataResponse changeThresholdAlarmStormNumber(final Long thresholdAlarmStormNumber);

    AlarmTestingDataResponse changeStormOnAlarmPeriod(final Integer stormOnAlarmPeriod);

    AlarmTestingDataResponse changeStormOffAlarmPeriod(final Integer stormOffAlarmPeriod);

    AlarmTestingDataResponse startReadAlarmFlow();

    AlarmTestingDataResponse stopReadAlarmFlow();

    AlarmTestingDataResponse changeReadParallelNumber(final Long readParallelNumber);

    AlarmTestingDataResponse changeReadPausePeriod(final Long readPausePeriod);

    AlarmTestingDataResponse getCurrentAlarmNumber();

}
