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
public class AlarmRemover {

    private static final Logger logger = LoggerFactory.getLogger(AlarmRemover.class);

    @Inject
    private DpsProxyProvider dpsProxyProvider;

    public List<Long> getAllAlarmToRemove(final String type) {
        logger.info("start of getAllAlarmToRemove");
        dpsProxyProvider.getDataPersistenceService().setWriteAccess(false);
        final DataBucket liveBucket = dpsProxyProvider.getLiveBucket();
        final QueryBuilder queryBuilder = dpsProxyProvider.getDataPersistenceService().getQueryBuilder();
        final Query<TypeRestrictionBuilder> typeQuery = queryBuilder.createTypeQuery(OSS_FM, OPEN_ALARM);
        final List<Restriction> restrictions = new ArrayList<Restriction>();
        restrictions.add(typeQuery.getRestrictionBuilder().equalTo("probableCause", type));
        final Restriction finalRestriction = typeQuery.getRestrictionBuilder().allOf(restrictions.toArray(new Restriction[restrictions.size()]));
        typeQuery.setRestriction(finalRestriction);
        final QueryExecutor queryExecutor = liveBucket.getQueryExecutor();
        final Iterator<PersistenceObject> poIt = queryExecutor.execute(typeQuery);
        final List<Long> list = new ArrayList<>();
        while (poIt.hasNext()) {
            try {
                final PersistenceObject po = poIt.next();
                list.add(po.getPoId());
            } catch (final Exception e) {
                logger.error("Error while getting alarm...", e);
            }
        }
        logger.info("end of getAllAlarmToRemove");
        return list;
    }

    public boolean removeAlarm(final Long poId) {
        final double startTime = System.currentTimeMillis();
        final DataBucket liveBucket = dpsProxyProvider.getLiveBucket();
        final PersistenceObject po = liveBucket.findPoById(poId);
        if (po != null) {
            liveBucket.deletePo(po);
        }
        final double endTime = System.currentTimeMillis();
        logger.debug("ElapsedTime: {} and removed of alarm with PO: {}", (endTime - startTime) / 1000, poId);
        return true;
    }

    public boolean removeAlarm(final List<AlarmTestingInfo> atis) {
        final double startTime = System.currentTimeMillis();
        int counter = 0;
        String type = "None";
        for (final AlarmTestingInfo ati : atis) {
            type = ati.getType();
            if (ati.getPoid() != -2) {
                if (this.removeAlarm(ati.getPoid())) {
                    counter++;
                }
            } else {
                if (this.removePersistenceObject(ati)) {
                    counter++;
                }
            }
        }
        final double endTime = System.currentTimeMillis();
        logger.info("ElapsedTime: {} while removing \"{}\" alarms with type: {}", (endTime - startTime) / 1000, counter, type);
        return true;
    }

    boolean removePersistenceObject(final AlarmTestingInfo alarmTestingInfo) {
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
        if (poListIterator.hasNext()) {
            final PersistenceObject po = poListIterator.next();
            try {
                liveBucket.deletePo(po);
                return true;
            } catch (final Exception e) {
                logger.error("Error while removing alarm...", e);
            }
        }
        return false;
    }

}
