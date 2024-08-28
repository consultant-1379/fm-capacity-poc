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
package com.ericsson.oss.services.fm.testing;

import static com.ericsson.oss.services.fm.testing.utils.AlarmProcessorConstants.OPEN_ALARM;
import static com.ericsson.oss.services.fm.testing.utils.AlarmProcessorConstants.OSS_FM;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.itpf.datalayer.dps.query.Query;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryBuilder;
import com.ericsson.oss.itpf.datalayer.dps.query.QueryExecutor;
import com.ericsson.oss.itpf.datalayer.dps.query.Restriction;
import com.ericsson.oss.itpf.datalayer.dps.query.TypeRestrictionBuilder;
import com.ericsson.oss.service.fm.testing.model.AlarmTestingInfo;
import com.ericsson.oss.services.fm.testing.utils.DpsProxyProvider;

@Stateless
public class AlarmReader {

    private static final Logger logger = LoggerFactory.getLogger(AlarmReader.class);

    @Inject
    private DpsProxyProvider dpsProxyProvider;

    public void readAllAlarms(final long requestId) {
        final double startTime = System.currentTimeMillis();
        logger.info(">>>>>>>>>>>>>>> START READING all alarms with request id: {} ", requestId);
        dpsProxyProvider.getDataPersistenceService().setWriteAccess(false);
        final DataBucket liveBucket = dpsProxyProvider.getLiveBucket();
        final QueryBuilder queryBuilder = dpsProxyProvider.getDataPersistenceService().getQueryBuilder();
        final Query<TypeRestrictionBuilder> typeQuery = queryBuilder.createTypeQuery(OSS_FM, OPEN_ALARM);
        final QueryExecutor queryExecutor = liveBucket.getQueryExecutor();
        final Iterator<PersistenceObject> poListIterator = queryExecutor.execute(typeQuery);
        long counter = 0;
        while (poListIterator.hasNext()) {
            final PersistenceObject po = poListIterator.next();
            logger.debug("Open Alarm: {}", po.getAllAttributes());
            counter++;
        }
        final double endTime = System.currentTimeMillis();
        logger.info("elapsedTime={}, >>>>>>>>>>>>>>> STOP READING all alarms: {} with requestId: {} ", (endTime - startTime) / 1000, counter,
                requestId);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean isExistingAlarm(final AlarmTestingInfo alarmTestingInfo) {
        dpsProxyProvider.getDataPersistenceService().setWriteAccess(false);
        return (getExistingAlarm(alarmTestingInfo) != null);
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    PersistenceObject getExistingAlarm(final AlarmTestingInfo alarmTestingInfo) {
        final DataBucket liveBucket = dpsProxyProvider.getLiveBucket();
        final QueryBuilder queryBuilder = dpsProxyProvider.getDataPersistenceService().getQueryBuilder();
        final Query<TypeRestrictionBuilder> typeQuery = queryBuilder.createTypeQuery(OSS_FM, OPEN_ALARM);
        final List<Restriction> restrictions = new ArrayList<Restriction>();
        restrictions.add(typeQuery.getRestrictionBuilder().equalTo("probableCause", alarmTestingInfo.getType()));
        restrictions.add(typeQuery.getRestrictionBuilder().equalTo("alarmNumber", alarmTestingInfo.getId()));
        restrictions.add(typeQuery.getRestrictionBuilder().equalTo("fdn", alarmTestingInfo.getFdn()));
        restrictions.add(typeQuery.getRestrictionBuilder().equalTo("objectOfReference", alarmTestingInfo.getOor()));
        final Restriction finalRestriction = typeQuery.getRestrictionBuilder().allOf(restrictions.toArray(new Restriction[restrictions.size()]));
        typeQuery.setRestriction(finalRestriction);
        final QueryExecutor queryExecutor = liveBucket.getQueryExecutor();
        final Iterator<PersistenceObject> poListIterator = queryExecutor.execute(typeQuery);
        return poListIterator.hasNext() ? poListIterator.next() : null;
    }

}
